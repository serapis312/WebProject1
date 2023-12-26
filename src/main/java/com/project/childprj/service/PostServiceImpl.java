package com.project.childprj.service;

import com.project.childprj.domain.post.*;
import com.project.childprj.domain.user.UserImage;
import com.project.childprj.domain.user.User;
import com.project.childprj.repository.PostRepository;
import com.project.childprj.repository.UserRepository;
import com.project.childprj.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;
    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;
    @Value("${app.upload.path}")
    private String uploadDir;

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(SqlSession sqlSession) {
        postRepository = sqlSession.getMapper(PostRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
    }


    @Override
    public int write(Post post, Map<String, MultipartFile> files) {
        // 현재 로그인한 작성자 정보
        User user = U.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다.
        user = userRepository.findById(user.getId());
        post.setUser(user);    // 글 작성자 세팅

        int cnt1 = postRepository.save(post);

        int cnt2 = isFileImageOrNull(files);

        // 첨부파일 추가 (이미지 파일만)
        addFiles(files, post.getId());

        if(cnt1 == 1 && cnt2 == 1) {
            return 1;
        }

        return 0;
    }

    @Override
    public UserImage findUserImageByUserId(Long user_id) {
        return postRepository.findUserImage(user_id);
    }

    // 특정 글 (id) 의 첨부파일(들) 추가 (이미지 파일만)
    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if(files == null) {
            return;
        }

        for(Map.Entry<String, MultipartFile> e : files.entrySet()){
            // name="upfile##" 인 경우만 첨부파일 등록 (이유, 다른 웹에디터와 섞이지 않도록..ex: summernote)
            if(!e.getKey().startsWith("upfile")) continue;

            // 이미지파일이 아니면 첨부파일 등록 안함
            BufferedImage bufferedImage = null;

            try {
                bufferedImage = ImageIO.read(e.getValue().getInputStream());

                if(bufferedImage == null){
                    continue;
                }

            } catch (IOException errors) {
                throw new RuntimeException(errors);
            }

            // 물리적인 파일 저장
            Attachment file = upload(e.getValue());

            // 성공하면 db 에도 저장
            if(file != null){
                file.setPost_id(id);   // FK 설정
                postRepository.saveImage(file);  // INSERT
            }

        }

    }

    // 물리적으로 파일 저장. 중복된 이름 rename 처리
    private Attachment upload(MultipartFile multipartFile) {
        Attachment attachment = null;

        // 담긴 파일이 없으면 pass~
        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename == null || originalFilename.length() == 0) return null;

        // 파일이 이미지가 아니면 pass~
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());

            if(bufferedImage == null){
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 원본 파일 명
        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // 저장될 파일 명
        String fileName = sourceName;

        // 파일이 중복되는지 확인
        File file = new File(uploadDir, fileName);

        if(file.exists()){  // 이미 존재하는 파일명,  중복된다면 다른 이름은 변경하여 파일 저장
            // a.txt => a_2378142783946.txt  : time stamp 값을 활용할거다!
            // "a" => "a_2378142783946" : 확장자 없는 경우

            int pos = fileName.lastIndexOf(".");
            if(pos > -1){  // 확장자 있는 경우
                String name = fileName.substring(0, pos);   // 파일 '이름'
                String ext = fileName.substring(pos + 1);  // 파일 '확장자'

                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {  // 확장자 없는 경우
                fileName += "_" + System.currentTimeMillis();
            }
        }

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());

        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        attachment = Attachment.builder()
                .fileName(fileName)  // 저장된 이름
                .sourceName(sourceName)  // 원본 이름
                .build();

        return attachment;
    }

    @Override
    @Transactional
    public int addRecommend(Long user_id, Long post_id) {
        List<Recommend> list = postRepository.findRecommend(post_id);

        for(Recommend recommend : list) {
            if(recommend.getUserId() == user_id) {
                return 0;
            }
        }

        int result1 = postRepository.addRecommend(user_id, post_id);
        int result2 = postRepository.incRecommendCnt(post_id);

        if(result1 == 1 && result2 == 1) {
            return 1;
        }

        return 0;
    }

    @Override
    @Transactional
    public Post detail(Long id) {
        postRepository.incViewCnt(id);
        Post post = postRepository.findById(id);

        if(post != null){
            // 첨부파일(들) 정보 가져오기
            List<Attachment> fileList = postRepository.findImageByPost(post.getId());
            post.setFileList(fileList);
        }

        return post;
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> listPost(Integer page, String orderWay, String searchTxt, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;   // 디폴트 1 page
        if(page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS; // 만약 session 에 없으면 기본값으로 동작
        session.setAttribute("page", page);  // 현재 페이지 번호 -> session 에 저장

        long cnt = 0;

        if(searchTxt == null || searchTxt.isEmpty()) {
            cnt = postRepository.countAll();
        } else {
            cnt = postRepository.countAllWhenSearch(searchTxt, searchTxt); // 검색 결과 총 개수
        }



        int totalPage = (int)Math.ceil(cnt / (double)pageRows);  // 총 몇 '페이지' 분량인가?

        // [페이징] 에 표시할 '시작페이지' 와 '마지막 페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 페이지의 글 목록
        List<Post> list = null;

        if(cnt > 0){  // 데이터가 최소 1개 이상 있는 경우만 페이징
            // page 값 보정
            if(page > totalPage) page = totalPage;

            // 몇번째 데이터부터 fromRow
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당 페이지의 글 목록 읽어오기 (정렬순, 검색순)
            if (orderWay.equals("newer") && searchTxt.isEmpty()) {
                list = postRepository.selectByNewer(fromRow, pageRows);
            }
            if (orderWay.equals("recommend") && searchTxt.isEmpty()){
                list = postRepository.selectByRecommend(fromRow, pageRows);
            }
            if (orderWay.equals("newer") && !searchTxt.isEmpty()) {
                list = postRepository.selectByNewerAndSearch(fromRow, pageRows, searchTxt, searchTxt);
                model.addAttribute("searchTxt", searchTxt);
            }
            if (orderWay.equals("recommend") && !searchTxt.isEmpty()){
                list = postRepository.selectByRecommendAndSearch(fromRow, pageRows, searchTxt, searchTxt);
                model.addAttribute("searchTxt", searchTxt);
            }

            model.addAttribute("orderWay", orderWay);
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지


        return list;
    }

    @Override
    public List<Post> list(Integer page, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;   // 디폴트 1 page
        if(page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS; // 만약 session 에 없으면 기본값으로 동작
        session.setAttribute("page", page);  // 현재 페이지 번호 -> session 에 저장

        long cnt = postRepository.countAll();  // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);  // 총 몇 '페이지' 분량인가?

        // [페이징] 에 표시할 '시작페이지' 와 '마지막 페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 페이지의 글 목록
        List<Post> list = null;

        if(cnt > 0){  // 데이터가 최소 1개 이상 있는 경우만 페이징
            // page 값 보정
            if(page > totalPage) page = totalPage;

            // 몇번째 데이터부터 fromRow
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당 페이지의 글 목록 읽어오기
            list = postRepository.selectFromRow(fromRow, pageRows);
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지


        return list;
    }

    @Override
    public Post selectById(Long id) {
        Post post = postRepository.findById(id);

        if(post != null){
            // 첨부파일(들) 정보 가져오기
            List<Attachment> fileList = postRepository.findImageByPost(post.getId());
            post.setFileList(fileList);
        }

        return post;
    }

    @Override
    public int update(Post post, Map<String, MultipartFile> files, Long[] delfile) {
        int cnt1 = postRepository.update(post);

        int cnt2 = isFileImageOrNull(files);

        // 새로운 첨부파일 추가
        addFiles(files, post.getId());

        // 삭제할 첨부파일들은 삭제하기
        if(delfile != null){
            for(Long fileId : delfile){
                Attachment file = postRepository.findImageById(fileId);
                if(file != null){
                    delFile(file);   // 물리적으로 파일 삭제
                    postRepository.deleteImage(file);   // DB 에서 삭제
                }
            }
        }

        if(cnt1 == 1 && cnt2 == 1) {
            return 1;
        }

        return 0;
    }

    // 첨부파일이 없거나 이미지일 경우 1 리턴 나머지 0 리턴
    private int isFileImageOrNull(Map<String, MultipartFile> files) {
        for(Map.Entry<String, MultipartFile> e : files.entrySet()) {
            if(U.isFileImage(e.getValue())) {
                return 1;
            }

            if(e.getValue().isEmpty()) {
                return 1;
            }
        }

        return 0;
    }

    private void delFile(Attachment image) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, image.getFileName());  // 물리적으로 저장된 파일들이 삭제 대상

        if(f.exists()){
            if(f.delete()){
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }

    }

    @Override
    public int deleteById(Long id) {
        int result = 0;

        Post post = postRepository.findById(id);  // 존재하는 데이터인지 읽어와보기
        if(post != null){

            // 물리적으로 저장된 첨부파일(들) 삭제
            List<Attachment> fileList = postRepository.findImageByPost(id);
            if(fileList != null && fileList.size() > 0){
                for(Attachment file : fileList){
                    delFile(file);
                }
            }

            // 글 삭제 (참조하는 첨부파일, 댓글 등도 같이 삭제 된다   ON DELETE CASCADE)
            result = postRepository.delete(post);
        }

        return result;

    }

    @Override
    public Attachment findImageById(Long id) {
        return postRepository.findImageById(id);
    }

    @Override
    public QryCommentList listComment(Long id) {
        QryCommentList list = new QryCommentList();

        List<Comment> comments = postRepository.findCommentByPost(id);

        list.setCount(comments.size());
        list.setList(comments);
        list.setStatus("OK");

        return list;
    }

    @Override
    public QryResult writeComment(Long post_id, Long user_id, String content) {
        User user = userRepository.findById(user_id);
        UserImage userImage = postRepository.findUserImage(user_id);

        Comment comment = Comment.builder()
                .user(user)
                .content(content)
                .post_id(post_id)
                .userImage(userImage)
                .build();

        postRepository.saveComment(comment);

        QryResult result = QryResult.builder()
                .count(1)
                .status("OK")
                .build();

        return result;
    }

    @Override
    public QryResult deleteComment(Long id) {
        int count = postRepository.deleteCommentById(id);

        String status = (count > 0) ? "OK" : "FAIL";

        QryResult result = QryResult.builder()
                .count(count)
                .status(status)
                .build();

        return result;
    }
}

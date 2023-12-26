$(function(){
    // 글 [삭제] 버튼
    $("#btnDel").click(function(){
        let answer = confirm("삭제하시겠습니까?");
        if(answer){
            $("form[name='frmDelete']").submit();
        }
    });

    // 추천하기
    $("#addRecommend").click(function(){
        let answer = confirm("추천하시겠습니까?");
        if(answer) {
            $("form[name='frmRecommend']").submit();
        }
    });

    // 현재 글의 id 값
    const id = $("input[name='id']").val().trim();

    // 현재 글의 댓글을 불러온다
    loadComment(id);

    // 댓글 작성 버튼 누르면 댓글 등록 하기.
    // 1. 어느글에 대한 댓글인지? --> 위에 id 변수에 담겨있다
    // 2. 어느 사용자가 작성한 댓글인지? --> logged_id 값
    // 3. 댓글 내용은 무엇인지?  --> 아래 content
    $("#btn_comment").click(function(){
        // 입력한 댓글
        const content = $("#input_comment").val().trim();

        // 검증
        if(!content){
            alert("댓글 입력을 하세요");
            $("#input_comment").focus();
            return;
        }


        // 전달할 parameter 들 준비
        const data = {
            "postId": id,
            "userId": logged_id,
            "content": content,
        };

        $.ajax({
            url: "/board/comment/write",
            type: "POST",
            data: data,
            cache: false,
            success: function(data, status){
                if(status == "success"){
                    if(data.status !== "OK"){
                        alert(data.status);
                        return;
                    }
                    loadComment(id);   // 댓글 목록 다시 업데이트
                    $("#input_comment").val('');   // 입력칸 리셋
                }
            },
        });
    });

});

// 특정 글 (post_id) 의 댓글 목록 읽어오기
function loadComment(post_id){
    $.ajax({
        url: "/board/comment/list?id=" + post_id,
        type: "GET",
        cache: false,
        success: function(data, status){
            if (status == "success"){
                // 서버측 에러 메세지가 있는 경우.
                if(data.status !== "OK"){
                    alert(data.status);
                    return;
                }

                buildComment(data);  // 댓글 화면 렌더링

                // ★ 댓글목록을 불러오고 난 뒤에 삭제에 대한 이벤트 리스너를 등록해야 한다
                addDelete();
            }
        },
    });
}

function buildComment(result){
    $("#cmt_cnt").text(result.count);   // 댓글 총 개수
    const out = [];

    result.data.forEach(comment => {
        let id = comment.id;
        let content = comment.content.trim();
        let createDate = comment.createDate;
        let userId = parseInt(comment.user.id);
        let nickname = comment.user.nickname;
        let userImage = comment.userImage;
        let fileUrl = "";
        if(userImage != null) {
            fileUrl = "/upload/" + comment.userImage.fileName;
        } else if(userImage == null) {
            fileUrl = "/images/이승규 증명사진(2021.08.30).jpg";
        }


        const delBtn = (logged_id !== userId) ? '' : `
            <i class="btn fa-solid fa-delete-left text-danger" data-bs-toggle="tooltip"
               data-cmtdel-id="${id}" title="삭제"></i>
            `;
        const row = `
            <tr>
                <th><img src="${fileUrl}" width="40px" class="comment-writer-pic"/></th>
                <th>${nickname}</th>
                <th>${content}${delBtn}</th>
                <th>${createDate}</th>
            </tr>
            `;
        out.push(row);
    });

    $("#cmt_list").html(out.join("\n"));

}

// 댓글 삭제 버튼이 눌렸을때, 해당 댓글 삭제하는 이벤트를 삭제버튼에 등록
function addDelete(){

    // 현재 글의 id (댓글 삭제후에도 댓글 목록 갱신 필요)
    const id = $("input[name='id']").val().trim();

    $("[data-cmtdel-id]").click(function(){
        if(!confirm("댓글을 삭제하시겠습니까?")) return;

        // 삭제할 댓글의 comment_id
        const comment_id = $(this).attr("data-cmtdel-id");

        $.ajax({
            url: "/board/comment/delete",
            type: "POST",
            cache: false,
            data: {"id": comment_id},
            success: function(data, status){
                if(status == "success"){
                    if(data.status !== "OK"){
                        alert(data.status);
                        return;
                    }

                    // 삭제후에도 다시 댓글 목록 불러와야 한다.
                    loadComment(id);
                }
            },
        });
    });

}
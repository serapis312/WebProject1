$(function(){
    // Summernode 추가
    $('.content-box').summernote({
        width: 1200,
        height: 300,
    });

    // [추가] 버튼 누르면 첨부파일 추가
    var i = 0;
    $("#btnAdd").click(function(){
        $("#files").append(`
            <div>
               <input class="form-control col-xs-3" type="file" name="upfile${i}" accept="image/*" required/>
               <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
            </div>`);
        i++;
    });

});

const $writeBtn = document.querySelector(".write-btn");
const writeArr = document.querySelectorAll(".write-valid");
const warnMsgArr = document.querySelectorAll(".warn-message");

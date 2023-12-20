const $price = document.querySelector(".market-writer-price");

// 가격 0 --> 나눔
if($price.innerText == "0"){
    $price.innerText = "나눔";
}

// 글 삭제
const clickDetailDelBtn = () => {
    let del = confirm("글을 삭제하시겠습니까?");
    if(del){
        document.forms["detailDelete"].submit();
    }
}

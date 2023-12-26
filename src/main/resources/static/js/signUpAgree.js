function checkAgree(){
    let agree = document.getElementsByName("check");

    for(e of agree){
        if(!e.checked){
            alert(`${e.value} 항목에 동의하셔야 합니다`);
            e.focus();
            return;
        }
    }

    document.forms.regist.submit();
}
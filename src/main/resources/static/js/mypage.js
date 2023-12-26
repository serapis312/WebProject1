$(function(){

    // 닉네임 변경
    $("#btnCancelChangeNickName").click(function(){
        $(".nickname-change-wrap").hide();
        $(".nickname").show();
        $(".nickname-change-btn").show();
    });

    $(".nickname-change-btn").click(function(){
        $(".nickname-change-wrap").show();
        $(".nickname").hide();
        $(".nickname-change-btn").hide();
    });

    if (flagNickname == 0) {
        $(".nickname-change-wrap").show();
        $(".nickname").hide();
        $(".nickname-change-btn").hide();
    } else if (flagNickname == 1) {
        $(".nickname-change-wrap").hide();
        $(".nickname").show();
        $(".nickname-change-btn").show();
    }


    // 비밀번호 변경
    $(".change-password-btn").click(function(){
        $(".change-pw-wrap").show();
        $(".change-password-btn").hide();
    });

    $(".change-pw-cancel").click(function(){
        $(".change-pw-wrap").hide();
        $(".change-password-btn").show();
    });


    if(flag == 1) {
        $(".change-pw-wrap").hide();
        $(".change-password-btn").show();
    } else if(flag == 0) {
        $(".change-pw-wrap").show();
        $(".change-password-btn").hide();
    }

    // 프로필사진 변경
    $("#btnChangeImage").click(function(){
        $("#upfile").show();
        $("#btnChangeImage").hide();
    });

    $("#btnCancelImage").click(function(){
        $("#upfile").hide();
        $("#btnChangeImage").show();
    });

});
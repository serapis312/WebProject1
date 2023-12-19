const $nicknameChangeBtn = document.querySelector(".nickname-change-btn");
const $nicknameChangeCompleteBtn = document.querySelector(".nickname-change-complete");
const $nickname = document.querySelector(".nickname");
const $nicknameInput = document.querySelector(".nickname-change-input");
const $completeBtn = document.querySelector(".nickname-change-complete");

const $changePwBtn = document.querySelector(".mypage-content-content > .change-password-btn");
const $changePwCompleteBtn = document.querySelector(".change-pw-complete");
const $changePwWrap = document.querySelector(".change-pw-wrap");

const $dropUser = document.querySelector(".drop-user");

const $body = document.querySelector("body");

const $btnCancelChangeNickName = document.querySelector("#btnCancelChangeNickName");
const $errorMessageNickName = document.querySelector("#errorMessage_nickName");

const $btnCancelChangePassword = document.querySelector("#btnCancelChangePassword");

// 닉네임 변경
const clickNickNameChangeBtn = () => {
    $nickname.style.display = `none`;
    $nicknameChangeBtn.style.display = `none`;
    $completeBtn.style.display = `block`;
    $nicknameInput.style.display = `block`;
    $btnCancelChangeNickName.style.display = `block`;
};

const clickBtnCancelChangeNickName = () => {
    $nickname.style.display = `block`;
    $nicknameChangeBtn.style.display = `block`;
    $completeBtn.style.display = `none`;
    $nicknameInput.style.display = `none`;
    $btnCancelChangeNickName.style.display = `none`;
    $errorMessageNickName.innerHTML= "";
};

if (flagNickName == 0) {
    $nickname.style.display = `none`;
    $nicknameChangeBtn.style.display = `none`;
    $completeBtn.style.display = `block`;
    $nicknameInput.style.display = `block`;
    $btnCancelChangeNickName.style.display = `block`;
} else if (flagNickName == 1) {
    $nickname.style.display = `block`;
    $nicknameChangeBtn.style.display = `block`;
    $completeBtn.style.display = `none`;
    $nicknameInput.style.display = `none`;
    $btnCancelChangeNickName.style.display = `none`;
}


// 비밀번호 변경
const clickPasswordChangeBtn = () => {
    console.log("비밀번호 변경");
    $changePwBtn.style.display = `none`;
    $changePwWrap.style.display = `block`;
};

const clickBtnCancelChangePassword = () => {
    console.log("비밀번호 변경 취소");
    $changePwBtn.style.display = `block`;
    $changePwWrap.style.display = `none`;
};

if(flag == 1) {
    $changePwBtn.style.display = `block`;
    $changePwWrap.style.display = `none`;
} else if(flag == 0) {
    $changePwBtn.style.display = `none`;
    $changePwWrap.style.display = `block`;
}

// 프사 변경

// 회원 탈퇴
const clickDropUserBtn = () => {
    const realDrop = confirm("정말 탈퇴하시겠습니까?");
    if (realDrop) {
        $modal.style.visibility = `visible`;
        $body.style.overflow = "hidden";
    }
};

$nicknameChangeBtn.addEventListener("click", clickNickNameChangeBtn);

$changePwBtn.addEventListener("click", clickPasswordChangeBtn);

$dropUser.addEventListener("click", clickDropUserBtn);

$btnCancelChangeNickName.addEventListener("click", clickBtnCancelChangeNickName)

$btnCancelChangePassword.addEventListener("click", clickBtnCancelChangePassword)

// 프로필사진 변경
$(function(){
    $("#btnChangeImage").click(function(){
        $("#upfile").show();
        $("#btnChangeImage").hide();
    });

    $("#btnCancelImage").click(function(){
        $("#upfile").hide();
        $("#btnChangeImage").show();
    });

});
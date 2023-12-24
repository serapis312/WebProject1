const inputArr = document.querySelectorAll(".login-input")
const loginBtn = document.querySelector(".login-btn")
const warnMsg = document.querySelector(".warn-message")

const loginValidation = () => {
    for(let i=0; i<2; i++) {
        let inputText = inputArr[i].value;
        if(inputText == "") {
            warnMsg.style.display = `block`;
        }
    }
}

loginBtn.addEventListener("click", loginValidation)
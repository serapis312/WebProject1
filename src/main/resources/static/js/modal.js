const $dropUserClose = document.querySelector(".dropUserCloseBtn");
const $modal = document.querySelector(".modal");
const $errorMessageDelete = document.querySelector("#errorMessage_delete");

const clickDropUserCloseBtn = () => {
    flagDelete = 1;
    $modal.style.visibility = `hidden`;
    $body.style.overflow = "unset";
    $errorMessageDelete.innerHTML = "";
};

if(flagDelete == 0) {
    $modal.style.visibility = `visible`;
    $body.style.overflow = "hidden";
} else if(flagDelete == 1) {
    $modal.style.visibility = `hidden`;
    $body.style.overflow = "unset";
}


$dropUserClose.addEventListener("click", clickDropUserCloseBtn);

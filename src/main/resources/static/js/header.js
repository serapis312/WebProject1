const textColor = ["#63da53", "#ff748b", "#ffc400", "#dbdbdb"];
const menuArr = document.querySelectorAll(".menuText");

const toggleArr = document.querySelectorAll(".raiseToggle");

// 메뉴
function clickMenu(index) {

    // 누르면 색 바뀜
    if (index == 0) {
        menuArr[0].style.color = textColor[index];
        menuArr[1].style.color = ""; // 설정 초기화!
        menuArr[2].style.color = ""; // 설정 초기화!

        toggleArr[0].style.display = `none`;
        toggleArr[1].style.display = `none`;
    }
    if (index == 1) {
        menuArr[1].style.color = textColor[index];
        menuArr[0].style.color = ""; // 설정 초기화!
        menuArr[2].style.color = ""; // 설정 초기화!

        toggleArr[0].style.display = `none`;
        toggleArr[1].style.display = `none`;
    }
    if (index == 2) {
        menuArr[2].style.color = textColor[index];
        menuArr[0].style.color = ""; // 설정 초기화!
        menuArr[1].style.color = ""; // 설정 초기화!

        toggleArr[0].style.display = `block`;
    }
    if (index == 3) {   // 설정 초기화
        menuArr[0].style.color = "";
        menuArr[1].style.color = "";
        menuArr[2].style.color = "";

        toggleArr[0].style.display = `none`;
        toggleArr[1].style.display = `none`;
    }
};

// 토글 기능
function toggleToMarket() {
    location.href = "/product/list";
};

function toggleToCommunity() {
    location.href = "/post/list";
};


// 메뉴 클릭 이벤트 리스너 추가
menuArr[0].addEventListener("click", clickMenu(0), true);
menuArr[1].addEventListener("click", clickMenu(1), true);
menuArr[2].addEventListener("click", clickMenu(2), true);

// 토글 클릭 이벤트 리스너 추가
toggleArr[0].addEventListener("click", toggleToMarket, true);
toggleArr[1].addEventListener("click", toggleToCommunity, true);

// 페이지 이동 후 토글스위치 변경
let service = location.pathname.split("/")[1]
if(service == "post") {
    toggleArr[1].style.display = `none`;
    toggleArr[0].style.display = `block`;
}
if(service == "product") {
    toggleArr[0].style.display = `none`;
    toggleArr[1].style.display = `block`;
}

// 찜목록이나 마이페이지에서는 헤더 설정 초기화
let service2 = location.pathname.split("/")[1] + "/" + location.pathname.split("/")[2]
if (service2 == "together/zzim" || service2 == "user/mypage") {
    clickMenu(3);
}


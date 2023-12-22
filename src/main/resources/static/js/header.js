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
};

// 토글 기능
function toggleToMarket() {
    toggleArr[0].style.display = `none`;
    toggleArr[1].style.display = `block`;
    location.href = "/product/list";
};

function toggleToCommunity() {
    toggleArr[1].style.display = `none`;
    toggleArr[0].style.display = `block`;
    location.href = "/post/list";
};


// 메뉴 클릭 이벤트 리스너 추가
menuArr[0].addEventListener("click", clickMenu(0), true);
menuArr[1].addEventListener("click", clickMenu(1), true);
menuArr[2].addEventListener("click", clickMenu(2), true);

// 토글 클릭 이벤트 리스너 추가
toggleArr[0].addEventListener("click", toggleToMarket, true);
toggleArr[1].addEventListener("click", toggleToCommunity, true);

// 찜목록이나 마이페이지에서는 헤더 설정 초기화
if (location == "http://localhost:8090/together/zzim" || location == "http://localhost:8090/user/mypage" || location == "http://localhost:8090/user/signIn") {
    clickMenu(3);
}


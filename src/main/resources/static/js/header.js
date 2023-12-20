const textColor = ["#63da53", "#ff748b", "#ffc400", "#dbdbdb"];
const menuArr = document.querySelectorAll(".menuText");
const $menuWrap = document.querySelector("menuTitleWrap");

const toggleArr = document.querySelectorAll(".raiseToggle");

// 메뉴
//const clickMenu = (index) => {
//    for (let i = 0; i < menuArr.length; i++) {
//        if (i == index) {
//            // 누르면 색 바뀜
//            menuArr[i].style.color = textColor[i];
//            // 같이키우기 누르면 토글 생성
//            if (i == 2) {
//                toggleArr[0].style.display = `block`;
//            } else {
//                toggleArr[0].style.display = `none`;
//                toggleArr[1].style.display = `none`;
//            }
//        } else {
//            menuArr[i].style.color = ""; // 설정 초기화!
//        }
//    }
//};

// 메뉴
const clickMenu = (index) => {
     for (let i = 0; i < menuArr.length; i++) {
        if (i == index) {
            // 누르면 색 바뀜
            menuArr[index].style.color = textColor[index];
        } else {
            menuArr[i].style.color = ""; // 설정 초기화!
        }
     }

    // 같이키우기 누르면 토글 생성
    if(index == 2) {
        toggleArr[0].style.display = `block`;
    } else {
        toggleArr[0].style.display = `none`;
        toggleArr[1].style.display = `none`;
    }
};


// 토글 제어
//const clickToggle = (index) => {
//    for (let i = 0; i < toggleArr.length; i++) {
//        if (i == index) {
//            toggleArr[i].style.display = `none`;
//        } else {
//            toggleArr[i].style.display = `block`;
//        }
//    }
//};

//for (let i = 0; i < 3; i++) {
//    menuArr[i].onclick = function () {
//        clickMenu(i);
//    };
//}

// 메뉴 클릭 이벤트 리스너 추가
menuArr[0].addEventListener("click", clickMenu(0));
menuArr[1].addEventListener("click", clickMenu(1));
menuArr[2].addEventListener("click", clickMenu(2));


//for (let i = 0; i < 2; i++) {
//    toggleArr[i].onclick = function () {
//        clickToggle(i);
//    };
//}

// 토글 기능
const toggleToMarket = () => {
    toggleArr[0].style.display = `none`;
    toggleArr[1].style.display = `block`;
    location.href = "/market/marketList";
};

const toggleToCommunity = () => {
    toggleArr[0].style.display = `block`;
    toggleArr[1].style.display = `none`;
    location.href = "/community/communityList";
};

// 토글 클릭 이벤트 리스너 추가
toggleArr[0].addEventListener("click", toggleToMarket);
toggleArr[1].addEventListener("click", toggleToCommunity);

// 찜목록이나 마이페이지에서는 헤더 설정 초기화
if (location == "http://localhost:8090/together/zzim" || location == "http://localhost:8090/mypage/main" || location == "http://localhost:8090/user/signIn") {
    clickMenu(3);
}
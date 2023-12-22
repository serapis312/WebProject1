$(function(){
    $("#orderWay").on("change", function(){
        if($("input[name='orderWay']").val() == "recommend") {
            $("#ordayWay").val("recommend").selected = true;
        }
        if($("input[name='orderWay']").val() == "newer") {
            $("#ordayWay").val("newer").selected = true;
        }

        $("form[name='searchFrm']").submit();
    });

    $("#btnSearch").click(function(){
        $("form[name='searchFrm']").submit();
    });



});
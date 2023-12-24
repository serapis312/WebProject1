$(function(){
    $("#orderWay").on("change", function(){
        $("form[name='searchFrm']").submit();
    });

    $("#btnSearch").click(function(){
        $("form[name='searchFrm']").submit();
    });

    if($("#inputOrderWay").val() == "newer") {
        $("#orderWay").val("newer").prop("selected", true);
    }
    if($("#inputOrderWay").val() == "recommend") {
        $("#orderWay").val("recommend").prop("selected", true);
    }



});

$(document).ready(function () {
    $('.btn-star').click(function () { 
       var id =  $(this).closest('div').attr('data-id');
       $.ajax({
        type: "method",
        url: "url",
        data: "data",
        dataType: "dataType",
        success: function (response) {
            
        }
       });
       .$.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "dataType",
        url: "url",
        data: "data",
        success: function (response) {
            
        }
       });
       .ja
    });
});
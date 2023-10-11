function doDeleteComment(board_id, post_id, comment_id)
{
    var http = new XMLHttpRequest();
    var url_to = window.location.origin + "/comments/"+comment_id;
    http.open("DELETE", url_to);
    http.setRequestHeader("content-type", "application/x-www-form-urlencoded");
    var http_body = ""
        + "board_id=" + board_id + "&"
        + "post_id=" + post_id
    ;
    http.addEventListener("readystatechange", function(){
        if(http.readyState === 4 && http.status === 200)
        {
            window.location.reload(true);
        }
    });
    http.send(http_body);

    console.log("send http body : " + http_body);
};

function cancleEditCommentBtn(comment_id)
{
    var targetName = "comment_edit_div_"+comment_id;
    console.log("cancleEditCommentBtn : target - "+targetName);

    var comment_edit_div = document.getElementsByName(targetName)[0];
    comment_edit_div.style.display = 'none';
    return ;
};

function clickEditCommentBtn(comment_id)
{
    var targetName = "comment_edit_div_"+comment_id;
    console.log("clickEditCommentBtn : target - " + targetName );

    var comment_edit_div = document.getElementsByName(targetName)[0];
    comment_edit_div.style.display = 'block';
    return ;
};
function doDeleteComment(comment_id)
{
    var http = new XMLHttpRequest();
    var url_to = window.location.origin + "/comments/"+comment_id;
    http.open("DELETE", url_to);
    http.addEventListener("readystatechange", function(){
        if(http.readyState === 4 && http.status === 200)
        {
            window.location.reload(true);
        }
    });
    http.send();
};

function clickEditCommentBtn(comment_id)
{
    //todo : 특정 div를 click 여부 인식 후 노출 하도록.
    return ;
}

function doEditComment(comment_id)
{
    var http = new XMLHttpRequest();
    var url_to = window.location.origin + "/comments/" + comment_id;
    
    var comment_str = document.getElementsByName("new_comment_"+comment_id)[0].nodeValue;
    console.log("new comment : "+ comment_str);
    http.open("POST", url_to);
    http.setRequestHeader("content-type", "application/x-www-form-urlencoded");

    var post_id = document.getElementsByName("hidden_comment_post_id_" + comment_id)[0].value;
    var parent_id = document.getElementsByName("hidden_comment_parent_id_" + comment_id)[0].value;

    var request_body = 
        "id=" + comment_id
        + "&" + "postId=" + post_id
        + "&" + "parentId=" + parent_id
        + "&" + "comment=" + comment_str
    ;
    
    http.send(request_body);
    return ;
}
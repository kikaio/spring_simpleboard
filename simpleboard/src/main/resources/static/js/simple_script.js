function doRoleUpdate(role_id)
{
    var http = new XMLHttpRequest();
    var url_to = window.location.origin + "/roles/" + role_id;

    var formData = new FormData();
    var role_id = document.getElementsByName('role_id')[0].value;
    var role_name = document.getElementsByName('role_name')[0].value;
    var role_desc = document.getElementsByName("role_desc")[0].value;

    console.log("role_id : "+ role_id);
    console.log("role_name : "+ role_name);
    console.log("role_desc : "+ role_desc);

    formData.append("name", role_name);
    formData.append("desc", role_desc);
    console.log("send req body : "+ formData.values);

    fetch(url_to, {
        method: 'POST'
        , cache : 'no-cache'
        , body : formData
    })
    .then(res=>{res.json})
    .then(data=>{console.log(data)})
    ;
};

function doUpdate(role_id)
{
    var http = new XMLHttpRequest();
    var url_to = window.location.origin + "/roles/" + role_id;
    http.open("POST", url_to);
    http.setRequestHeader("content-type", "application/x-www-form-urlencoded");

    var role_div = document.getElementById("role_div");
    var role_name = document.getElementsByName('name')[0].value;
    var role_desc = document.getElementsByName("desc")[0].value;
    var requset_body = 
        "name="+role_name
        + "&"
        + "desc="+role_desc
    ;
    http.addEventListener("readystatechange", function(){
        if(http.readyState === 4 && http.status === 200)
        {
            window.location.reload(true);
        }
    });
    console.log("send req body : "+requset_body);
    http.send(requset_body);
};
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

function clickReplyCommentBtn(comment_id)
{
    var targetName = "comment_reply_div_"+comment_id;
    console.log("clickEditCommentBtn : target - " + targetName );

    var comment_reply_div = document.getElementsByName(targetName)[0];
    comment_reply_div.style.display = 'block';
    return ;
};

function cancleReplyCommentBtn(comment_id)
{
    var targetName = "comment_reply_div_"+comment_id;
    console.log("cancleEditCommentBtn : target - "+targetName);

    var comment_reply_div = document.getElementsByName(targetName)[0];
    comment_reply_div.style.display = 'none';
    return ;
};

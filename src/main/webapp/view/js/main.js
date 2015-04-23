$("#send-message").submit(function (e) {
    e.preventDefault();
    console.log($(e.target).find("input").val());
    message = "<div>" + $(e.target).find("input").val() + "</div>";
    if ($(e.target).find("input").val()) {
        $("#messages").append(message);
    }
//    ajax({
//        url: 'send',
//        type: "post",
//        data: $(e.target).serialize(),
//        success: function (data) {
//
//        },
//        error: function () {
//
//        }
//    });
    $(e.target).find("input").val("")
});

$input = $("form div div input");
console.log($input);
$img = $($input).parent().parent().find("img");
$email = $($input).val();
$md5email = hex_md5($email);
console.log($email);
$gravatar_url = "http://gravatar.com/avatar/" + $md5email + "?s=37&d=retro";
    console.log($gravatar_url);
$img.attr("src", $gravatar_url);
console.log("hello");

$input.on("change", function (e) {
    console.log(e.target);
    $img = $(e.target).parent().parent().find("img");
    $email = $(e.target).val();
    $md5email = hex_md5($email);
//    console.log($md5email);
    $gravatar_url = "http://gravatar.com/avatar/" + $md5email + "?s=37&d=retro";
//    console.log($gravatar_url);
    $img.attr("src", $gravatar_url);
});
$(".add_participant a").on("click", function(e) {
//    console.log("wow");
    $("form").append("<div class=\"row collapse\"><div class=\"small-1 columns\" style=\"margin-right: 0\"><img src=\"http://gravatar.com/avatar/?s=37&d=mm\" alt=\"\"/></div><div class=\"small-10 columns\"><input type=\"email\" name=\"email\" placeholder=\"e-mail\"/></div><div class=\"small-1 columns\"><span class=\"postfix\">+<span></div></div>");
});
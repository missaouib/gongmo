var onReady = function() {

};

var save = function() {

    var $frm = $("form[name='frmRegister']");

    var params = {
        username: $frm.find("input[name=username").val(),
        name: $frm.find("input[name=name]").val(),
        email: $frm.find("input[name=email]").val()
    };

    $.ajax({
        url: "/account/ajax/save",
        method: "post",
        type: "json",
        contentType: "application/json",
        data: JSON.stringify(params),
        success: function (resp) {
            if(resp.code === 200) {
                location.reload();
            } else {
            }
        }
    });
};

$(document).ready(onReady)
    .on('click', '#btnSave', save);
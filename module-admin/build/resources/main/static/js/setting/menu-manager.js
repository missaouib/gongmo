var onReady = function() {
    $('.menu-detail-panel select,.menu-detail-panel input,.menu-detail-panel button').attr('disabled', true);
    $('#include-role option').hide();
};

$('.menu-panel')
    .on("select_node.jstree", function (e, selector) {
        var $btnNewMenu = $('.btn-new-menu');
        if (selector.node.parents.length <= 2) {
            $btnNewMenu.attr('disabled', false);
        } else {
            $btnNewMenu.attr('disabled', true);
        }

        if (selector.node.children.length > 0) {
            $('.uri-pattern .pattern-panel').hide();
            $('.uri-pattern .pattern-message').show();
        } else {
            $('.uri-pattern .pattern-panel').show();
            $('.uri-pattern .pattern-message').hide();
        }
        if (selector.node.parents.length === 2) {
            $('#parent-id').val("#");
        } else if (selector.node.parents.length > 2) {
            var p = $('.menu-panel').jstree("get_node", selector.node.parent);
            $('#parent-id').val(p.data.menu_id);
        }
        var menuId = selector.node.data.menu_id;
        $('#menu-id').val(menuId);

        var menuType = selector.node.data.menu_type;
        $('#menu-type').val(menuType);

        //초기화
        $('#include-role option').hide();
        $('#exclude-role option').show();
        if (menuId === undefined) {
            $('.menu-detail-panel select,.menu-detail-panel input,.menu-detail-panel button').attr('disabled', true);
            return;
        } else {
            $('.menu-detail-panel select,.menu-detail-panel input,.menu-detail-panel button').attr('disabled', false);
        }

        $('#title').val('');
        $('#uri').val('');
        $('#icon').val('');
        $("#active").iCheck('check');
        $("#chkShow").iCheck('check');
        $("#pattern-single").iCheck('check');
        $('#parent-uri').text("/")
        if (menuId === "temp") {
            $('#parent-uri').text(p.data.uri + (p.data.uri.length > 1 ? "/" : ""));
            return;
        }

        $.get("/setting/menu-manager/ajax/menu/" + menuId, function (resp) {
            if (resp.code === 200) {
                var body = resp.data;

                $('#title').val(body.menuName);
                var url = body.url;
                if (body.parent != null) {
                    $('#parent-uri').text(body.parent.url + (body.parent.url.length > 1 ? "/" : ""));
                    url = url.replace(body.parent.url + (body.parent.url.length > 1 ? "/" : ""), "");
                } else {
                    if (url.indexOf("/") === 0) {
                        url = url.substr(1);
                    }
                }

                $('#uri').val(url);
                $('#icon').val(body.iconClass);
                $('#sortOrder').val(body.sortOrder);

                if (body.activeYn === 'Y') {
                    $("#active").iCheck('check');
                } else {
                    $("#inactive").iCheck('check');
                }

                if (body.displayYn === 'Y') {
                    $("#chkShow").iCheck('check');
                } else {
                    $("#chkHide").iCheck('check');
                }

                if (body.antMatcherType === "Single") {
                    $("#pattern-single").iCheck('check');
                } else {
                    $("#pattern-all").iCheck('check');
                }

                $.each(body.roles, function (idx, role) {
                    $('#exclude-role option[value="' + role.roleName + '"]').hide();
                    $('#include-role option[value="' + role.roleName + '"]').show();
                    //$('#selected-role').append('<option value="' + role.roleName + '">' + role.description + '</option>');
                })

            } else {
                alert(resp.message);
            }
        });
    }).jstree({
    core: {
        multiple: false,
        check_callback: true,
        themes: {
            stripes: true,
            responsive: false,
            variant: 'small',
        }
    },
    types: {
        default: {icon: 'folder'},
        file: {icon: 'file'}
    },
    plugins: [
        "changed",
        "types",
        "wholerow",
    ]
});

var newMenu = function() {
    var $menuPanel = $(".menu-panel");

    if(confirm("하위메뉴 생성시 URI Pattern은 Single로 고정됩니다.")){
        if ($menuPanel.jstree("get_selected").length > 0) {
            var parent_id = $menuPanel.jstree("get_selected")[0];
            var parent_node = $menuPanel.jstree("get_node", parent_id);

            if (parent_node.parent === '#' || parent_node.parents.length === 2) {
                $menuPanel.jstree("create_node", parent_node, {
                    data: {menu_id: 'temp', menu_type: parent_node.data.menu_type},
                    type: 'file',
                    icon: 'glyphicon glyphicon-file',
                    text: 'new menu'
                }, "last", function (new_node) {
                    $menuPanel.jstree("open_node", $menuPanel.jstree("get_selected"));
                    $menuPanel.jstree('deselect_node', parent_node);
                    $menuPanel.jstree('select_node', new_node);
                });
            } else {
                alert("하위메뉴를 생성 할 수 없습니다.")
            }
        } else {
            alert("메뉴를 선택해 주세요");
        }
    }
};


var includeRole = function() {
    var role = $('#exclude-role option:checked').val();
    $('#exclude-role option[value="' + role + '"]').hide();
    $('#include-role option[value="' + role + '"]').show();
};

var excludeRole = function() {
    var role = $('#include-role option:checked').val();
    $('#exclude-role option[value="' + role + '"]').show();
    $('#include-role option[value="' + role + '"]').hide();
};

var save = function() {
    var title = $('#title').val();
    if (title === "") {
        alert('Title is required.');
        return;
    }

    var uri = $('#parent-uri').text() + $('#uri').val();

    var params = {
        menuName: title,
        url: uri,
        iconClass: $('#icon').val(),
        sortOrder: $('#sortOrder').val(),
        activeYn: $('input[name="optActive"]:checked').val(),
        displayYn: $('input[name="optShowHide"]:checked').val(),
        antMatcherType: $('input[name="optPattern"]:checked').val(),
        menuType: $('#menu-type').val()
    };

    var id = $('#menu-id').val();
    if (id !== 'temp') {
        params["id"] = id;
    }

    var isEmptyRole = true;
    $('#include-role option').filter(function () {
        return $(this).css('display') === 'block';
    }).each(function (idx, role) {
        params["roles[" + idx + "].roleName"] = $(role).val();
        params["roles[" + idx + "].description"] = $(role).text();
        isEmptyRole = false;
    });

    if (isEmptyRole) {
        alert('Role is required.');
        return;
    }

    var pid = $('#parent-id').val();
    if (pid !== "" && pid !== "#") {
        //param = Object.assign(param, {parent: {id: pid}})
        params["parent.id"] = pid;
    }

    $.ajax({
        url: "/setting/menu-manager/ajax/menu/save",
        method: "post",
        type: "json",
        contentType: "application/json",
        data: JSON.stringify(params),
        success: function (result) {
            location.reload();
        },
        error:function(){
            // alert("Error: " + resp.message);
        }
    });
};

$(document).ready(onReady)
    .on('click', '.btn-new-menu', newMenu)
    .on('click', '#btn-include-role', includeRole)
    .on('click', '#btn-exclude-role', excludeRole)
    .on('click', '.btn-save', save);

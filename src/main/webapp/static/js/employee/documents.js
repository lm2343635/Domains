var pageSize = 10;
var tid = request("tid");

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        TypeManager.getByCategory(TypeCategoryDocument, function(result) {
            if (!result.session) {
                return;
            }
            for (var i in result.data) {
                var type = result.data[i];
                $("#document-types").mengular(".document-type-template", {
                    tid: type.tid,
                    name: type.name
                });
            }
            $("#document-types").mengularClearTemplate();

            if (tid == null || tid == "") {
                tid = result.data[0].tid;
            }
            $("#" + tid).addClass("active");

            searchPublicDocuments(null, null, null, 1);
        });

    });

    $("#upload-document").fileupload({
        autoUpload: true,
        url: "/document/upload/public",
        dataType: "json",
        start: function (e) {
            $("#document-upload-modal").modal("show");
        },
        done: function (e, data) {
            if (data.result.status == 801) {
                sessionError();
                return;
            }
            if (data.result.status == 803) {
                $.messager.popup("刷新页面重试！");
                return;
            }
            loadPublicDocuments();
            setTimeout(function () {
                $("#document-upload-modal").modal("hide");
            }, 1000);
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $("#document-upload-progress .progress-bar").css("width", progress + "%");
            $("#document-upload-progress .progress-bar").text(progress + "%");
        }
    });

    $("#search-document-start, #search-document-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });

    $("#search-submit").click(function () {
        var filename = $("#search-document-filename").val();
        var start = $("#search-document-start").val();
        var end = $("#search-document-end").val();
        searchPublicDocuments(filename, start, end, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
        $("#search-submit").click();
    });

});

function searchPublicDocuments(filename, start, end, page) {
    DocumentManager.getSearchPublicCount(tid, filename, start, end, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#page-size").text(pageSize);

        var count = result.data;
        $("#page-count").text(count);
        $("#page-nav ul").empty();
        for (var i = 1; i < Math.ceil(count / pageSize + 1); i++) {
            var li = $("<li>").append($("<a>").attr("href", "javascript:void(0)").text(i));
            if (page == i) {
                li.addClass("active");
            }
            $("#page-nav ul").append(li);
        }

        $("#page-nav ul li").each(function (index) {
            $(this).click(function () {
                searchPublicDocuments(filename, start, end, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });
    });

    DocumentManager.searchPublic(tid, filename, start, end, page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            return;
        }
        $("#document-list").mengularClear();
        for (var i in result.data) {
            var document = result.data[i];

            $("#document-list").mengular(".document-list-template", {
                did: document.did,
                employee: document.employee,
                uploadAt: document.uploadAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                filename: document.filename,
                size: document.size
            });

            $("#" + document.did + " .document-list-remove").click(function () {
                var did = $(this).mengularId();
                var filename = $("#" + did + " .document-list-filename").text();
                $.messager.confirm("删除附件文件", "确定要删除" + filename + "吗？", function () {
                    DocumentManager.remove(did, function (result) {
                        if (!result.session) {
                            sessionError();
                            return;
                        }
                        if (!result.privilege) {
                            $.messager.popup("当前用户无权删除改附件！");
                            return;
                        }
                        $.messager.popup("删除成功！");
                        $("#" + did).remove();
                    });
                });
            });
        }
    });
}
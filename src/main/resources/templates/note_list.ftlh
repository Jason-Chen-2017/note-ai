<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Note AI</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="shortcut icon" href="/favicon.svg" type="image/x-icon">
</head>
<body>

<h1 class="text-center"><a href="/doc.html" target="_blank">Note AI</a></h1>

<script src="/webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<link href="//cdn.bootcss.com/datatables/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="//cdn.bootcss.com/datatables/1.10.10/css/jquery.dataTables_themeroller.css" rel="stylesheet">

<script src="//cdn.bootcss.com/datatables/1.10.10/js/dataTables.bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/datatables/1.10.10/js/jquery.dataTables.min.js"></script>

<div>
    <input id="topic" name="topic"
           class="input-lg col-sm-12"
           placeholder="帮我拟定”人工智能大模型“领域的100篇热门博客文章标题,此处只需要输入：人工智能大模型"></input>
</div>

<div class="text-center">
    <button id="aigc"
            onclick="aigc()"
            class="btn-lg btn-info col-sm-12">
        AIGC
    </button>
</div>


<table class="table table-striped table-responsive table-hover"
       id="dataTable"
       data-toggle="table"
       data-search="true">
    <#--    <thead>-->
    <#--    <tr>-->
    <#--        <th>Id</th>-->
    <#--        <th>Title</th>-->
    <#--        <th>Content</th>-->
    <#--        <th>CreatedAt</th>-->
    <#--    </tr>-->
    <#--    </thead>-->
    <#--    <tbody>-->
    <#--    <#list notes as note>-->
    <#--        <tr>-->
    <#--            <td>${note.id?c}</td>-->
    <#--            <td><a target="_blank" href="/notes/${note.id?c}">${note.title}</a></td>-->

    <#--            <td>-->
    <#--                <#if note.content?? && note.content?length lte 100>-->
    <#--                    ${note.content}-->
    <#--                <#else>-->
    <#--                    ${note.content[0..100]}...-->
    <#--                </#if>-->
    <#--            </td>-->

    <#--            <td>${note.createdAt?string('yyyy-MM-dd hh:mm:ss')}</td>-->
    <#--        </tr>-->
    <#--    </#list>-->
    <#--    </tbody>-->
</table>


<script>

    function aigc() {

        var topic = $("input[name='topic']").val();

        var selectedText = "现在你是一位人工智能专家,程序员,软件架构师,CTO，请以逻辑清晰、结构紧凑、简单易懂的专业的技术语言，请帮我拟定：" + topic + "领域的100篇热门博客文章标题。"

        var data = {prompt: selectedText};

        $.ajax({
            type: "POST",
            url: "/api/ai/aigc",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                // 保存成功后的处理逻辑
                console.log(response);
                location.href = "/notes";
            },
            error: function (xhr, status, error) {
                // 保存失败后的处理逻辑
                console.log(xhr.responseText);
            }
        });

    }


    $(function () {

        $('#dataTable').DataTable({
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "ordering": true,
            "info": true,
            "autoWidth": true,
            "aLengthMenu": [10, 20, 50, 100, 200],
            // "processing": true,
            "stateSave": true,
            responsive: true,
            fixedHeader: true,
            order: [[0, "desc"]],
            columns: [
                {title: "ID", data: "id"},
                {
                    title: "标题", data: "title", render: function (data, type, row) {
                        return "<a target='_blank' href='/notes/" + row.id + "' class='btn btn-link'>" + row.title + "</a>";
                    }
                },
                {
                    title: "内容", data: "content", render: function (data, type, row) {
                        var content = row.content;
                        if (content.length > 100) {
                            content = content.substr(0, 100) + "..."
                        }

                        return content;
                    }
                },
                {title: "创建时间", data: "createdAt"},
                {title: "更新时间", data: "updatedAt"}
            ],

            language: {
                "search": "<div style='margin-left:auto;margin-right:2px;'>_INPUT_ &nbsp;<span class='btn btn-default'>搜索</span></div>",

                paginate: { //分页的样式内容。
                    previous: "上一页",
                    next: "下一页",
                    first: "第一页",
                    last: "最后"
                },

                zeroRecords: "没有内容",//table tbody内容为空时，tbody的内容。
                //下面三者构成了总体的左下角的内容。
                info: "总计 _TOTAL_ 条,共 _PAGES_ 页，_START_ - _END_ 条",//左下角的信息显示，大写的词为关键字。
                "infoEmpty": "第 0 至 0 项记录，共 0 项",
                "infoFiltered": "(由 _MAX_ 项记录过滤)",
            },
            "serverSide": true, // 启用后端分页
            "ajax": {
                url: "/api/notes/findNotesByKeyword", // 后端接口地址
                type: "POST", // 请求方式
                data: function (d) {
                    // 将分页参数传递给后端接口(跟后端接口的参数名字要对应上)
                    d.pageNum = $("#dataTable").DataTable().page.info().page + 1;
                    d.pageSize = $("#dataTable").DataTable().page.info().length;
                    // 搜索框的输入参数
                    d.keyword = $('input[type="search"]').val();
                    // 框架会默认POST 一些其他参数

                }
            }
        })

    })

</script>

</body>
</html>


<#--draw: 3
columns[0][data]: id
columns[0][name]:
columns[0][searchable]: true
columns[0][orderable]: true
columns[0][search][value]:
columns[0][search][regex]: false
columns[1][data]: title
columns[1][name]:
columns[1][searchable]: true
columns[1][orderable]: true
columns[1][search][value]:
columns[1][search][regex]: false
columns[2][data]: content
columns[2][name]:
columns[2][searchable]: true
columns[2][orderable]: true
columns[2][search][value]:
columns[2][search][regex]: false
columns[3][data]: createdAt
columns[3][name]:
columns[3][searchable]: true
columns[3][orderable]: true
columns[3][search][value]:
columns[3][search][regex]: false
order[0][column]: 0
order[0][dir]: desc
start: 200
length: 10
search[value]:
search[regex]: false
pageNum: 21
pageSize: 10
keyword: -->
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

<link href="https://unpkg.com/bootstrap-table@1.21.4/dist/bootstrap-table.min.css" rel="stylesheet">
<script src="https://unpkg.com/bootstrap-table@1.21.4/dist/bootstrap-table.min.js"></script>

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
       id="table"
       data-toggle="table"
       data-search="true">
    <thead>
    <tr>
        <th>Id</th>
        <th>Title</th>
        <th>Content</th>
        <th>CreatedAt</th>
    </tr>
    </thead>
    <tbody>
    <#list notes as note>
        <tr>
            <td>${note.id?c}</td>
            <td><a href="/notes/${note.id?c}">${note.title}</a></td>

            <td>
                <#if note.content?? && note.content?length lte 100>
                    ${note.content}
                <#else>
                    ${note.content[0..100]}...
                </#if>
            </td>

            <td>${note.createdAt?string('yyyy-MM-dd hh:mm:ss')}</td>
        </tr>
    </#list>
    </tbody>
</table>


<script>

    function aigc() {

        var topic = $("input[name='topic']").val();

        var selectedText = "帮我拟定" + topic + "领域的100篇热门博客文章标题:"

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

</script>

</body>
</html>
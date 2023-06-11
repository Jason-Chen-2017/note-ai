<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Note AI</title>
    <link rel="stylesheet" href="/webjars/editor.md/1.5.0/css/editormd.min.css">
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.5/css/bootstrap.min.css">
</head>
<body>

<input type="hidden" name="id" value="${(note.id)!''}">
<input type="hidden" name="title" value="${(note.title)!''}">


<div class="btn-lg btn-default text-center">
    <a href="/notes">Back Note AI Home</a>
</div>


<h1>${(note.title)!''}</h1>


<div class="text-center">
    <button id="saveButton" onclick="saveButton()" class="btn-info btn-lg">保存</button>
    <button id="writeBlog" onclick="writeBlog()" class="btn-info btn-lg">AI根据选中标题自动写博客</button>
    <button id="complete" onclick="complete()" class="btn-info btn-lg">AI根据选中内容自动续写</button>
</div>

<div id="editor">
    <textarea name="content" id="content" cols="30" rows="10">${(note.content)!''}</textarea>
</div>


<script src="/webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="/webjars/marked/4.3.0/marked.min.js"></script>
<script src="/webjars/editor.md/1.5.0/editormd.min.js"></script>
<script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>


<script>

    var editor = editormd("editor", {
        width: "100%",
        height: "500px",
        path: "/webjars/editor.md/1.5.0/lib/",
        saveHTMLToTextarea: true,
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: "/api/image/upload"
    });

    editor.previewing();

    $(document).ready(function () {
        var content = $("#content").val();

        console.log(content);

        editor.setValue(content);

    })


    function saveButton() {

        var id = $("input[name='id']").val();
        var title = $("input[name='title']").val();
        var content = editor.getValue();
        var data = {id: id, title: title, content: content};


        $.ajax({
            type: "POST",
            url: "/api/notes/saveJson",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                // 保存成功后的处理逻辑
                console.log(response);
                window.location.href = '/notes/' + id;
            },
            error: function (xhr, status, error) {
                // 保存失败后的处理逻辑
                console.log(xhr.responseText);
            }
        });

    }


    function writeBlog() {

        // 获取当前鼠标选中文本
        var selectedText = window.getSelection().toString();
        if (selectedText) {
            console.log("当前鼠标选中文本: " + selectedText);
        }

        var data = {prompt: selectedText};

        $.ajax({
            type: "POST",
            url: "/api/ai/writeBlog",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                // 保存成功后的处理逻辑
                console.log(response);

                var replaceText = selectedText + response

                editor.replaceSelection(replaceText);

            },
            error: function (xhr, status, error) {
                // 保存失败后的处理逻辑
                console.log(xhr.responseText);
            }
        });

    }
    function complete() {

        // 获取当前鼠标选中文本
        var selectedText = window.getSelection().toString();
        if (selectedText) {
            console.log("当前鼠标选中文本: " + selectedText);
        }

        var data = {prompt: selectedText};

        $.ajax({
            type: "POST",
            url: "/api/ai/complete",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (response) {
                // 保存成功后的处理逻辑
                console.log(response);

                var replaceText = selectedText + response

                editor.replaceSelection(replaceText);

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
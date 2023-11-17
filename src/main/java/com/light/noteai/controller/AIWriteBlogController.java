package com.light.noteai.controller;

import com.light.noteai.LLMUtil;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.model.Prompt;
import com.light.noteai.service.NoteService;
import com.light.noteai.task.MyTask;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/ai/")
public class AIWriteBlogController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private MyTask myTask;

    @PostMapping("/writeBlog")
    public String writeBlog(@RequestBody Prompt prompt) {
        return LLMUtil.INSTANCE.WriteBlog(prompt.getPrompt());
    }

    @PostMapping("/complete")
    public String complete(@RequestBody Prompt prompt) {
        return LLMUtil.INSTANCE.Complete(prompt.getPrompt());
    }

    @PostMapping("/aigc")
    public String aigc(@RequestBody Prompt prompt) {

        String lines = LLMUtil.INSTANCE.Complete(prompt.getPrompt());

        for (String line : lines.split("\n")) {

            System.out.println(line);

            if (!line.trim().equals("")) {
                Notes note = new Notes();
                note.setTitle(line);
                note.setContent(line);
                Date createdAt = new Date();
                note.setCreatedAt(createdAt);
                note.setUpdatedAt(createdAt);
                noteService.save(note);
            }

        }

        return "done";
    }

    @GetMapping("/auto_aigc")
    public String auto_aigc() {

        new Thread(() -> {

            myTask.doAutoAIGC();

        }).start();

        return "done";
    }

    @GetMapping("/auto_title")
    public String auto_title() {

        new Thread(() -> {

            myTask.doAutoTitle();

        }).start();

        return "done";
    }

    @GetMapping("/WriteAllBlog")
    public String WriteAllBlog() {

        new Thread(() -> {

            myTask.WriteAllBlog();

        }).start();

        return "done";
    }


    String mdFilePath = "/home/me/tools/pycnblog/articles/";

    /**
     * @param date 20230616
     * @return
     */
    @GetMapping("/writeMD")
    public String writeMD(@RequestParam("date") String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyyMMdd").parse(date);

        new Thread(() -> {

            List<Notes> notes = noteService.getNotesByDate(d);
            for (Notes note : notes) {
                String title = note.getTitle();
                String content = note.getContent();

                // 如果标题跟内容不相同，表面是生成之后的文章了，文章内容写入md文件中
                if (!Objects.equals(title, content) && title.length() > 10) {
                    WriteMD(mdFilePath + d, title, content);
                }
            }

        }).start();

        return "done";
    }


    @GetMapping("/writeMDAll")
    public String writeMDAll() throws ParseException {

        new Thread(() -> {

            List<Notes> notes = noteService.getAllNotes();
            for (Notes note : notes) {
                String title = note.getTitle();
                String content = note.getContent();

//  在写文章的时候已经处理了，所以这里不用重复处理了。
//                // 文章内容每行的行首空格处理
//                content = LLMUtil.INSTANCE.trimHeadSpaces(content);
//                // 被\t,\n错误替换的latex公式修复
//                content = LLMUtil.INSTANCE.fixLatex(content);

                Date date = note.getUpdatedAt();

                String d = new SimpleDateFormat("yyyyMMdd").format(date);

                // 如果标题跟内容不相同，表面是生成之后的文章了，文章内容写入md文件中
                if (!Objects.equals(title, content) && title.length() > 10) {
                    WriteMD(mdFilePath + d, title, content);
                }
            }

        }).start();

        return "done";
    }

    // 把content字符串写入 ${mdFilePath}/${title}.md 文件中
    private void WriteMD(String mdFilePath, String title, String content) {

        title = processTitle(title);
        content = processContent(content);

        try {
            File fileDir = new File(mdFilePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File file = new File(mdFilePath, title + ".md");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @NotNull
    private static String processTitle(String title) {

        // 去除前后空格
        title = title.trim();

        // 123.标题, 把标题前面的数字和.去掉：
        // 1.标题A =>  标题A
        // 223.标题B => 标题B
        title = replaceStartDigitAndDot(title);
        // 去掉标题中的引号和 - 等特殊字符
        title = title.replaceAll("\"", "");
        title = title.replaceAll("“", "");
        title = title.replaceAll("、", "");
        title = title.replaceAll("\\*", "");
        title = title.replaceAll("”", "");
        title = title.replaceAll("-", "");
        title = title.replaceAll("》", "");
        title = title.replaceAll("《", "");
        title = title.replaceAll("]", "");
        title = title.replaceAll("\\[", "");

        // 避免超长标题
        if (title.length() > 99) {
            title = title.substring(0, 99);
        }
        return title;
    }

    @NotNull
    private static String processContent(String content) {
        // 文章内容每行的行首空格处理
//        content = LLMUtil.INSTANCE.trimHeadSpaces(content);

        String pattern = "(.*外链图片转存中.*)|(.*.png.*)|(.*.jpg.*)|(.*\\(https://.*)|.*(<img src=.*).*|(.*\\(http://.*)";
        // 将字符串分割为行
        String[] lines = content.split("\\r?\\n");

        // 筛选出不匹配正则表达式的行
        String[] filteredLines = Arrays.stream(lines)
                .filter(line -> !Pattern.matches(pattern, line))
                .toArray(String[]::new);

        // 将筛选后的行重新组合为字符串
        String result = String.join("\n", filteredLines);
        return result;
    }

    /**
     * 这个方法首先检查输入字符串是否为 null 或空字符串，如果是，则直接返回原始字符串。然后，它在字符串中查找第一个非数字字符的位置，这个位置就是标题中数字和点号的结尾位置。然后，它检查这个位置是否是一个点号，如果是，就将其忽略。最后，它返回从数字和点号结尾位置开始的子字符串。
     * <p>
     * 这个方法可以处理以下字符串：
     * <p>
     * "1.标题A" => "标题A"
     * "223.标题B" => "标题B"
     * "3.5.7.标题C" => "标题C"
     * "标题D" => "标题D"
     * "" => ""
     * null => null
     *
     * @param str
     * @return
     */
    public static String replaceStartDigitAndDot(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int index = 0;
        while (index < str.length() && Character.isDigit(str.charAt(index))) {
            index++;
        }
        if (index > 0 && index < str.length() && str.charAt(index) == '.') {
            index++;
        }
        return str.substring(index).trim();
    }

}

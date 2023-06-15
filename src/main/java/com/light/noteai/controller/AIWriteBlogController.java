package com.light.noteai.controller;

import com.light.noteai.ChatGLMUtil;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.model.Prompt;
import com.light.noteai.service.NoteService;
import com.light.noteai.task.MyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ai/")
public class AIWriteBlogController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private MyTask myTask;

    @PostMapping("/complete")
    public String complete(@RequestBody Prompt prompt) {
        return ChatGLMUtil.INSTANCE.Complete(prompt.getPrompt());
    }

    @PostMapping("/aigc")
    public String aigc(@RequestBody Prompt prompt) {

        String lines = ChatGLMUtil.INSTANCE.Complete(prompt.getPrompt());

        for (String line : lines.split("\n")) {

            System.out.println(line);

            if (!line.trim().equals("")) {
                Notes note = new Notes();
                note.setTitle(line);
                note.setContent(line);
                note.setCreatedAt(new Date());
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

    @GetMapping("/noteAI")
    public String noteAI() {

        new Thread(() -> {

            List<Notes> notes = noteService.getAllNotes();
            for (Notes note : notes) {
                String title = note.getTitle();
                String contentInitial = note.getContent();
                if (Objects.equals(title, contentInitial)) {
                    String content = ChatGLMUtil.INSTANCE.WriteBlog(title);
                    note.setContent(content);

                    System.out.println(content);

                    try {
                        noteService.save(note);
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }

        }).start();


        return "done";
    }


    String mdFilePath = "/home/me/tools/pycnblog/articles/";

    @GetMapping("/writeMD")
    public String writeMD() {

        new Thread(() -> {

            List<Notes> notes = noteService.getAllNotes();
            for (Notes note : notes) {
                String title = note.getTitle();
                String content = note.getContent();

                // 如果标题跟内容不相同，表面是生成之后的文章了，文章内容写入md文件中
                if (!Objects.equals(title, content) && title.length() > 10) {
                    WriteMD(mdFilePath, title, content);
                }
            }

        }).start();


        return "done";
    }

    // 把content字符串写入 ${mdFilePath}/${title}.md 文件中
    private void WriteMD(String mdFilePath, String title, String content) {

        // 123.标题, 把标题前面的数字和.去掉：
        // 1.标题A =>  标题A
        // 223.标题B => 标题B
        title = replaceStartDigitAndDot(title);
        // 去掉标题中的引号和 - 等特殊字符
        title = title.replaceAll("\"", "");
        title = title.replaceAll("-", "");
        title = title.replaceAll("》", "");
        title = title.replaceAll("《", "");

        // 避免超长标题
        if (title.length() > 55) {
            title = title.substring(0, 55);
        }

        try {
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

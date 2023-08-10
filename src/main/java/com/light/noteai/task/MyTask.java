package com.light.noteai.task;

import com.light.noteai.WizardLMUtil;
import com.light.noteai.constant.NoteAITopics;
import com.light.noteai.controller.AIWriteBlogController;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class MyTask {

    @Autowired
    private NoteService noteService;


    @Autowired
    private AIWriteBlogController aIWriteBlogController;


    @Scheduled(cron = "0 0 */12 * * ?") // 每隔12h执行一次
    public void AIGC() {
        // 定时任务:
        System.out.println("AIGC定时任务执行时间：" + new Date());

        doAutoAIGC();

    }

    public void doAutoAIGC() {
        for (String topic : NoteAITopics.topicsArray) {
            String prompt = "现在你是一位人工智能专家,程序员,软件架构师,CTO，请以逻辑清晰、结构紧凑、简单易懂的专业的技术语言（标题要非常吸引读者），请帮我拟定：" + topic + " 领域的10篇热门博客文章标题,每个标题放到单独的一行。只需要标题就可以了。不用多余的内容。";

            System.out.println(prompt);

//            String lines = ChatGLMUtil.INSTANCE.Complete(prompt);
            String lines = WizardLMUtil.INSTANCE.Complete(prompt);

            for (String line : lines.split("\n")) {

                System.out.println(line);
                line = line.trim();

                if (isGoodTitle(line)) {
                    Notes note = new Notes();
                    note.setTitle(line);
                    note.setContent(line);
                    Date date = new Date();
                    note.setCreatedAt(date);
                    note.setUpdatedAt(date);
                    try {
                        noteService.save(note);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    private static boolean isGoodTitle(String line) {
        line = line.trim();
        return line.length() > 10 &&
                !line.contains("热门博客文章标题") &&
                !line.contains("文章") &&
                !line.contains("作者") &&
                !line.contains("Title") &&
                !line.contains("祝愿大家") &&

                !line.contains("2020") &&
                !line.contains("于") &&
                !line.contains("如需了解") &&
                !line.contains("如果你认为") &&
                !line.contains("Best regards") &&
                !line.contains("备注") &&
                !line.contains("更新") &&
                !line.contains("Sent:") &&
                !line.contains("Thanks") &&
                !line.contains("发布时间") &&
                !line.contains("包括但不限于") &&
                !line.contains("返回顶部") &&
                !line.contains("本帖") &&
                !line.contains("import") &&
                !line.contains("=") &&

                !line.contains("知乎") &&
                !line.contains("QQ群") &&
                !line.contains("头条号") &&
                !line.contains("微信") &&
                !line.contains("简书") &&
                !line.contains("公众号") &&

                !line.contains("来自") &&
                !line.contains("本文") &&
                !line.contains("打印") &&
                !line.contains("谢谢") &&
                !line.contains("您好") &&

                !line.contains("侵权") &&
                !line.contains("意见或建议") &&
                !line.contains("欢迎") &&
                !line.contains("读者") &&
                !line.contains("https://") &&
                !line.contains("http://") &&
                !line.contains("Office") &&
                !line.contains("blog") &&
                !line.contains("article") &&
                !line.contains("png") &&
                !line.contains("#") &&
                !line.contains("@") &&
                !line.contains("<") &&
                !line.contains(">") &&
                !line.contains("）") &&
                !line.contains("（") &&
                !line.contains("、") &&
                !line.contains("|")
                ;
    }


    @Scheduled(cron = "0 0 */1 * * ?") // 每隔1h执行一次
    public void WriteAllBlog() {
        // 定时任务:
        System.out.println("WriteBlog 任务执行时间：" + new Date());

        List<Notes> notes = noteService.getAllNotes();
        // 随机乱序一下
        Collections.shuffle(notes);

        for (Notes note : notes) {

            String title = note.getTitle();
            Integer id = note.getId();
            Notes noteNewest = noteService.findById(id);
            String contentInitial = noteNewest.getContent();

            if (Objects.equals(title, contentInitial)) {

//                String content = ChatGLMUtil.INSTANCE.WriteBlog(title);
                String content = WizardLMUtil.INSTANCE.WriteBlog(title);

                System.out.println("标题:" + title);
                System.out.println("内容:" + content);
                System.out.println("URL: http://127.0.0.1:9000/notes/" + id);

                note.setContent(content);
                note.setUpdatedAt(new Date());

                try {
                    noteService.save(note);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }
    }


    @Scheduled(cron = "0 */10 * * * ?") // 每隔10分钟执行一次
    public void WriteMDFiles() {
        try {
            aIWriteBlogController.writeMDAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
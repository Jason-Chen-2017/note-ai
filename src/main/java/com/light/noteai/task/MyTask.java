package com.light.noteai.task;

import com.light.noteai.ChatGLMUtil;
import com.light.noteai.constant.NoteAITopics;
import com.light.noteai.controller.AIWriteBlogController;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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
        HashSet<String> topics = NoteAITopics.getTopics();

        List<String> topicsList = new ArrayList<>(topics);

        Collections.shuffle(topicsList);

        for (String topic : topicsList) {
            String prompt = "现在你是一位人工智能专家,程序员,软件架构师,CTO，请以逻辑清晰、结构紧凑、简单易懂的专业的技术语言（标题要非常吸引读者），请帮我拟定：" + topic + " 领域的20篇热门博客文章标题,不要重复,标题中不要有特殊符号。";

            System.out.println(prompt);

            String lines = ChatGLMUtil.INSTANCE.Complete(prompt);

            for (String line : lines.split("\n")) {

                System.out.println(line);

                if (line.trim().length() > 10) {
                    Notes note = new Notes();
                    note.setTitle(line);
                    note.setContent(line);
                    Date date = new Date();
                    note.setCreatedAt(date);
                    note.setUpdatedAt(date);
                    noteService.save(note);
                }
            }
        }
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

                String content = ChatGLMUtil.INSTANCE.WriteBlog(title);

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
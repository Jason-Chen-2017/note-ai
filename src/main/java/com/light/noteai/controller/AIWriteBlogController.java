package com.light.noteai.controller;

import com.light.noteai.ChatGLMUtil;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.model.Prompt;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ai/")
public class AIWriteBlogController {

    @Autowired
    private NoteService noteService;


    @PostMapping("/writeBlog")
    public String writeBlog(@RequestBody Prompt prompt) {
        return ChatGLMUtil.INSTANCE.WriteBlog(prompt.getPrompt());
    }

    @PostMapping("/complete")
    public String complete(@RequestBody Prompt prompt) {
        return ChatGLMUtil.INSTANCE.Complete(prompt.getPrompt());
    }

    @PostMapping("/aigc")
    public String aigc(@RequestBody Prompt prompt) {
        String lines = ChatGLMUtil.INSTANCE.Complete(prompt.getPrompt());

        for (String line : lines.split("\n")) {

            System.out.println(line);

            if(!line.trim().equals("")){
                Notes note = new Notes();
                note.setTitle(line);
                note.setContent(line);
                note.setCreatedAt(new Date());
                noteService.save(note);
            }

        }

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
                    noteService.save(note);
                }
            }

        }).start();


        return "done";
    }

}

package com.light.noteai.controller;

import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/getAllNotes")
    public List<Notes> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/getNotesByPage")
    public List<Notes> getNotesByPage(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize) {
        return noteService.getNotesByPage(pageNum, pageSize);
    }

    @PostMapping("/saveJson")
    public Notes saveJson(@RequestBody Notes note) {
        return noteService.save(note);
    }

    @PostMapping(value = "/saveForm", consumes = {"application/json", "application/x-www-form-urlencoded;charset=UTF-8"})
    public Notes saveForm(@ModelAttribute Notes note) {

        return noteService.save(note);

    }
}
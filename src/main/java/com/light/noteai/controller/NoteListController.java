package com.light.noteai.controller;

import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteListController {

    @Autowired
    private NoteService noteService;

    @GetMapping({"/",""})
    public ModelAndView list() {
        List<Notes> notes = noteService.getAllNotes();

        ModelAndView modelAndView = new ModelAndView("note_list");
        modelAndView.addObject("notes", notes);

        return modelAndView;
    }


}

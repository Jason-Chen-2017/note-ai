package com.light.noteai.controller;

import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notes")
public class ViewNoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ModelAndView view(@PathVariable("id") String id) {
        Notes note = noteService.findById(Integer.valueOf(id));

        ModelAndView modelAndView = new ModelAndView("view_note");
        modelAndView.addObject("note", note);

        return modelAndView;
    }


}

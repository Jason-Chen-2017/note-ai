package com.light.noteai.service;

import com.light.noteai.dal.mapper.NotesMapper;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.mapper.po.NotesExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoteService {
    @Autowired
    private NotesMapper notesMapper;

    public List<Notes> getAllNotes() {
        return notesMapper.findAll();
    }

    public List<Notes> getNotesByPage(int pageNum, int pageSize) {
        int startIndex = (pageNum - 1) * pageSize;
        return notesMapper.findByPage(startIndex, pageSize);
    }


    public Notes save(Notes note) {
        if (note.getId() == null) {
            notesMapper.create(note);
        } else {
            notesMapper.update(note);
        }
        return note;
    }
}
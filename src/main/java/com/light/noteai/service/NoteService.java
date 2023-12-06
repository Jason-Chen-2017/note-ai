package com.light.noteai.service;

import com.light.noteai.dal.mapper.NotesMapper;
import com.light.noteai.mapper.po.Notes;
import com.light.noteai.mapper.po.NotesExample;
import com.light.noteai.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class NoteService {
    @Autowired
    private NotesMapper notesMapper;

    public List<Notes> getAllNotes() {
        return notesMapper.findAll();
    }

    public List<Notes> getAllUnWrittenNotes() {
        return notesMapper.getAllUnWrittenNotes();
    }

    public List<Notes> getAllWrittenNotes() {
        return notesMapper.getAllWrittenNotes();
    }

    public List<Notes> getNotesByPage(int pageNum, int pageSize) {
        int startIndex = (pageNum - 1) * pageSize;
        return notesMapper.findByPage(startIndex, pageSize);
    }

    public List<Notes> findNotesByKeyword(String keyword, int pageNo, int pageSize) {
        int offset = (pageNo - 1) * pageSize;
        return notesMapper.findNotesByKeyword(keyword, offset, pageSize);
    }


    public Notes save(Notes note) {
        Date date = new Date();
        if (note.getId() == null) {
            note.setCreatedAt(date);
            note.setUpdatedAt(date);
            notesMapper.create(note);
        } else {
            note.setUpdatedAt(date);
            notesMapper.update(note);
        }
        return note;
    }

    public Notes findById(Integer id) {
        return notesMapper.selectByPrimaryKey(id);
    }

    public List<Notes> getNotesByDate(Date d) {
        Date dp = DateUtils.getNextDate(d);
        NotesExample exp = new NotesExample();
        exp.createCriteria().andCreatedAtBetween(d, dp);
        return notesMapper.selectByExampleWithBLOBs(exp);
    }

    public Integer getTotalNotesByKeyword(String keyword) {
        return notesMapper.getTotalNotesByKeyword(keyword);
    }
}
package com.light.noteai.converter;

import com.light.noteai.mapper.po.Notes;
import com.light.noteai.utils.DateUtils;
import com.light.noteai.vo.NotesVO;

import java.util.List;
import java.util.stream.Collectors;

public class NotesVOConverter {
    public static NotesVO convertVO(Notes notes) {
        NotesVO notesVO = new NotesVO();
        notesVO.setId(notes.getId());
        notesVO.setTitle(notes.getTitle());
        notesVO.setContent(notes.getContent());
        notesVO.setCreatedAt(DateUtils.formatDate(notes.getCreatedAt()));
        notesVO.setUpdatedAt(DateUtils.formatDate(notes.getUpdatedAt()));
        return notesVO;
    }

    public static List<NotesVO> convertVOList(List<Notes> notesList) {
        return notesList.stream().map(NotesVOConverter::convertVO).collect(Collectors.toList());
    }

}

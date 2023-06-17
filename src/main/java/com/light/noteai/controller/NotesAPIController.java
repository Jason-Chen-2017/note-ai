package com.light.noteai.controller;

import com.light.noteai.mapper.po.Notes;
import com.light.noteai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesAPIController {
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

    @PostMapping("/findNotesByKeyword")
    public PageNotes findNotesByKeyword(
            @RequestParam("draw") int draw,
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        List<Notes> data = noteService.findNotesByKeyword(keyword, pageNum, pageSize);
        Integer recordsTotal = noteService.getTotalNotesByKeyword("");
        Integer recordsFiltered = noteService.getTotalNotesByKeyword(keyword);
        PageNotes pageNotes = new PageNotes();

        pageNotes.recordsTotal = recordsTotal; // 总记录数
        pageNotes.recordsFiltered = recordsFiltered; // 经过过滤后的记录数
        pageNotes.data = data;
        pageNotes.draw = draw;
        return pageNotes;
    }


    /**
     * draw：DataTable 发送的请求序号，需要原样返回。
     * recordsTotal：总记录数，用于 DataTable 计算分页。
     * recordsFiltered：经过过滤后的记录数，用于 DataTable 计算分页。
     * data：当前页的记录数据，是一个数组，每个元素代表一条记录，包含需要显示的字段。
     */
    class PageNotes {
        List<Notes> data;
        Integer recordsTotal;
        Integer recordsFiltered;
        Integer draw;

        public Integer getDraw() {
            return draw;
        }

        public void setDraw(Integer draw) {
            this.draw = draw;
        }

        public List<Notes> getData() {
            return data;
        }

        public void setData(List<Notes> data) {
            this.data = data;
        }

        public Integer getRecordsTotal() {
            return recordsTotal;
        }

        public void setRecordsTotal(Integer recordsTotal) {
            this.recordsTotal = recordsTotal;
        }

        public Integer getRecordsFiltered() {
            return recordsFiltered;
        }

        public void setRecordsFiltered(Integer recordsFiltered) {
            this.recordsFiltered = recordsFiltered;
        }
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
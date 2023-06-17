package com.example.demo.Controller;

import com.example.demo.DTO.NotesDTO;
import com.example.demo.DTO.ListDTO;
import com.example.demo.Service.NotesService;
import com.example.demo.Util.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotesController extends BaseController{

    @Autowired
    private NotesService notesService;

    @PostMapping("/addNote")
    @ApiOperation(value = "Add Note",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> addNote(@Validated @RequestBody NotesDTO notesDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(notesService.addNote(notesDTO,bindingResult), "Note added successfully.");
    }

    @PostMapping("/updateNote")
    @ApiOperation(value = "Update Note",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateNote(@Validated @RequestBody NotesDTO notesDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(notesService.updateNote(notesDTO,bindingResult), "Note Update successfully.");
    }

    @PostMapping("/deleteNote")
    @ApiOperation(value = "Delete Note",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteNote(@Validated @RequestBody NotesDTO notesDTO, BindingResult bindingResult) throws Exception {
        notesService.deleteNote(notesDTO,bindingResult);
        return okSuccessResponse("Note Deleted successfully.");
    }

    @PostMapping("/openNote")
    @ApiOperation(value = "Open Note",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> openNote(@Validated @RequestBody NotesDTO notesDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(notesService.openNote(notesDTO,bindingResult),"Note Open successfully.");
    }

    @PostMapping("/listNotes")
    @ApiOperation(value = "List of Note",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> listNotes(@Validated @RequestBody ListDTO listDTO) throws Exception {
        return okSuccessResponse(notesService.listNotes(listDTO),"List of notes.");
    }
}

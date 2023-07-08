package com.example.demo.Service;

import com.example.demo.DTO.ListDTO;
import com.example.demo.DTO.NotesDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

@Service
public interface NotesService {
    Object addNote(NotesDTO notesDTO, BindingResult bindingResult);

    Object updateNote(NotesDTO notesDTO, BindingResult bindingResult);

    void deleteNote(NotesDTO notesDTO, BindingResult bindingResult);

    Object openNote(NotesDTO notesDTO, BindingResult bindingResult);

    Object listNotes(ListDTO listDTO);
}

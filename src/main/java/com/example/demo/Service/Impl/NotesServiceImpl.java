package com.example.demo.Service.Impl;

import com.example.demo.DTO.*;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Model.EntityNotes;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.NotesRepository;
import com.example.demo.Service.BaseService;
import com.example.demo.Service.NotesService;
import io.netty.util.internal.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class NotesServiceImpl extends BaseService implements NotesService {

    public NotesServiceImpl(ModelMapper mapper) {
        super(mapper);
    }


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NotesRepository notesRepository;

    @Override
    public Object addNote(NotesDTO notesDTO, BindingResult bindingResult) {
        if (!mongoTemplate.exists(Query.query(Criteria.where("userId").is(getUserId())), EntityUser.class)) {
            throw new BusinessValidationException("User not found");
        }
        EntityNotes entityNotes = new EntityNotes();
        if (StringUtil.isNullOrEmpty(notesDTO.getTitle())) {
            throw new BusinessValidationException("Title cannot be null or empty");
        }
        entityNotes = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(getUserId())
                .and("title").regex(notesDTO.getTitle(), "i")), EntityNotes.class);
        if (entityNotes != null) {
            throw new BusinessValidationException("This title notes already present");
        }
        if (StringUtil.isNullOrEmpty(notesDTO.getCategory().name())) {
            throw new BusinessValidationException("Please select the category of your note");
        }
        entityNotes = notesDTO.toModel(EntityNotes.class, getMapper());
        notesRepository.save(entityNotes);
        return entityNotes.toDTO(NotesResponseDTO.class, getMapper());
    }

    @Override
    public Object updateNote(NotesDTO notesDTO, BindingResult bindingResult) {
        EntityNotes entityNotes = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(notesDTO.getId())
                .and("userId").is(getUserId())), EntityNotes.class);
        if (entityNotes == null) throw new BusinessValidationException("Notes not found");
        EntityNotes entityNotes1 = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(getUserId())
                .and("title").regex(notesDTO.getTitle(), "i")
                .and("_id").ne(entityNotes.getId())), EntityNotes.class);
        if (entityNotes1 != null) {
            throw new BusinessValidationException("This title notes already present");
        }
        checkPassword(notesDTO, entityNotes);
        entityNotes.setNoteContent(notesDTO.getNoteContent());
        entityNotes.setTitle(notesDTO.getTitle());
        entityNotes.setCategory(notesDTO.getCategory());
        notesRepository.save(entityNotes);
        return entityNotes.toDTO(NotesResponseDTO.class, getMapper());
    }

    @Override
    public void deleteNote(NotesDTO notesDTO, BindingResult bindingResult) {
        EntityNotes entityNotes = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(notesDTO.getId())
                .and("userId").is(getUserId())), EntityNotes.class);
        if (entityNotes == null) throw new BusinessValidationException("Notes not found");
        checkPassword(notesDTO, entityNotes);
        entityNotes.setDeleted(true);
        notesRepository.save(entityNotes);

    }

    @Override
    public Object openNote(NotesDTO notesDTO, BindingResult bindingResult) {
        EntityNotes entityNotes = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(notesDTO.getId())
                .and("userId").is(getUserId())
                .and("isDeleted").is(false)), EntityNotes.class);
        if (entityNotes == null) throw new BusinessValidationException("Notes not found");
        checkPassword(notesDTO, entityNotes);
        return entityNotes.toDTO(NotesResponseDTO.class, getMapper());
    }

    @Override
    public Object listNotes(ListDTO listDTO) {
        Criteria criteria = Criteria.where("userId").is(getUserId())
                .and("isDeleted").is(false);
        if (!StringUtil.isNullOrEmpty(listDTO.getFilter())) {
            criteria.and("category").is(listDTO.getFilter());
        }
        if (!StringUtil.isNullOrEmpty(listDTO.getSearch())) {
            criteria.and("title").regex(listDTO.getSearch(), "i");
        }
        Query query = new Query(criteria);
        long totalCount = mongoTemplate.count(query, EntityNotes.class);
        if (totalCount > 1) {
            if (listDTO.getPageId() != null && listDTO.getLimit() != null) {
                int pageId = listDTO.getPageId();
                int limit = listDTO.getLimit();
                query.skip((long) pageId * limit).limit(limit);
            }
            if (listDTO.getSortBy() != null) {
                SortBy sortBy = listDTO.getSortBy();
                Sort sort = Sort.by(sortBy.getDirection(), sortBy.getProperty());
                query.with(sort);
            }
        }
        List<NotesResponseDTO> notesResponseDTOs = mongoTemplate.find(query, NotesResponseDTO.class, "entityNotes");
        return new SearchResultDTO<>(notesResponseDTOs,totalCount,listDTO.getLimit());
    }

    private void checkPassword(NotesDTO notesDTO, EntityNotes entityNotes) {
        if (entityNotes.isSecure()) {
            if (StringUtil.isNullOrEmpty(notesDTO.getMasterPassword())) {
                throw new BusinessValidationException("This note is secure with master password you need to provide master password.");
            }
            if (!isPasswordMatch(notesDTO.getMasterPassword(), getMasterPassword())) {
                throw new BusinessValidationException("Password is invalid");
            }
        }
    }
}

package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.SubjectRequest;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.Subject;
import com.quizapp.api.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found"));
    }

    public Subject createSubject(SubjectRequest request) {
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Integer id, SubjectRequest request) {
        Subject subject = getSubjectById(id);
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Integer id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }
}

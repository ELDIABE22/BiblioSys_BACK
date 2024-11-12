package com.example.bibliosys.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.Subject;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.subject.SubjectResponse;
import com.example.bibliosys.Services.impl.SubjectServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class SubjectController {
    @Autowired
    private SubjectServiceImpl subjectServiceImpl;

    @GetMapping("/subject")
    public ResponseEntity<List<Subject>> fetchAllSubjectsController() {
        List<Subject> subjects = subjectServiceImpl.fetchAllSubjectsService();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @PostMapping("/subject/new")
    public ResponseEntity<SubjectResponse> newSubjectController(@RequestBody Subject subject) {
        SubjectResponse subjectResponse = subjectServiceImpl.newSubjectService(subject);

        HttpStatus status = "Materia creada.".equals(subjectResponse.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(subjectResponse, status);
    }

    @PutMapping("/subject/update")
    public ResponseEntity<ApiResponse<Subject>> updateSubject(@RequestBody Subject subject) {
        ApiResponse<Subject> response = subjectServiceImpl.updateSubjectService(subject);

        HttpStatus status = "Materia actualizada.".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/subject/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSubject(@PathVariable Integer id) {
        ApiResponse<String> response = subjectServiceImpl.deleteSubjectService(id);

        HttpStatus status = "Materia eliminada.".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }
}

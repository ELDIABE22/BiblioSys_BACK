package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.Subject;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.subject.SubjectResponse;

public interface SubjectService {
    List<Subject> fetchAllSubjectsService();

    SubjectResponse newSubjectService(Subject subject);

    ApiResponse<Subject> updateSubjectService(Subject subject);

    ApiResponse<String> deleteSubjectService(Integer subjectId);
}

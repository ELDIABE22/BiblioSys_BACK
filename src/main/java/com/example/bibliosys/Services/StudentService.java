package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.Student;
import com.example.bibliosys.Models.response.ApiResponse;

public interface StudentService {
    List<Student> fetchAllStudentsService();

    ApiResponse<Student> newStudentService(Student student);

    ApiResponse<Student> updateStudentService(Student student);

    ApiResponse<Void> deleteStudentService(Integer studentId);
}

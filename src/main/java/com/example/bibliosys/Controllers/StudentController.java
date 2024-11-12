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

import com.example.bibliosys.Models.Student;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Services.impl.StudentServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class StudentController {
    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @GetMapping("/student")
    public ResponseEntity<List<Student>> fetchAllStudentsController() {
        List<Student> student = studentServiceImpl.fetchAllStudentsService();
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping("/student/new")
    public ResponseEntity<ApiResponse<Student>> newStudentController(@RequestBody Student student) {
        ApiResponse<Student> apiResponse = studentServiceImpl.newStudentService(student);

        HttpStatus status = "Estudiante agregado.".equals(apiResponse.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @PutMapping("/student/update")
    public ResponseEntity<ApiResponse<Student>> updateStudentController(@RequestBody Student student) {
        ApiResponse<Student> apiResponse = studentServiceImpl.updateStudentService(student);

        HttpStatus status = "Estudiante actualizado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudentController(@PathVariable Integer studentId) {
        ApiResponse<Void> apiResponse = studentServiceImpl.deleteStudentService(studentId);

        HttpStatus status = "Estudiante eliminado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }
}

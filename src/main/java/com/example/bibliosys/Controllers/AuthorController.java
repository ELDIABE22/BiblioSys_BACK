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

import com.example.bibliosys.Models.Author;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.author.AuthorResponse;
import com.example.bibliosys.Services.impl.AuthorServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class AuthorController {

    @Autowired
    private AuthorServiceImpl authorServiceImpl;

    @GetMapping("/author")
    public ResponseEntity<List<Author>> fetchAllAuthorsController() {
        List<Author> authors = authorServiceImpl.fetchAllAuthorsService();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PostMapping("/author/new")
    public ResponseEntity<AuthorResponse> newAuthorController(@RequestBody Author author) {
        return new ResponseEntity<>(authorServiceImpl.newAuthorService(author), HttpStatus.CREATED);
    }

    @PutMapping("/author/update")
    public ResponseEntity<ApiResponse<Author>> updateAuthor(@RequestBody Author author) {
        ApiResponse<Author> response = authorServiceImpl.updateAuthorService(author);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAuthor(@PathVariable Integer id) {
        ApiResponse<String> response = authorServiceImpl.deleteAuthorService(id);

        HttpStatus status = "Autor eliminado.".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

}

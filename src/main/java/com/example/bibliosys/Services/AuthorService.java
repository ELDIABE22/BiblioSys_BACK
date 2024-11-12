package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.Author;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.author.AuthorResponse;

public interface AuthorService {
    List<Author> fetchAllAuthorsService();

    AuthorResponse newAuthorService(Author author);

    ApiResponse<Author> updateAuthorService(Author author);

    ApiResponse<String> deleteAuthorService(Integer authorId);
}

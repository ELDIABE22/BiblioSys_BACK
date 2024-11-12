package com.example.bibliosys.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.request.book.BookRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.book.BookResponse;
import com.example.bibliosys.Services.impl.BookServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private BookServiceImpl bookServiceImpl;

    @GetMapping("/book")
    public ResponseEntity<List<BookResponse>> fetchAllBooksController() {
        List<BookResponse> books = bookServiceImpl.fetchAllBooksService();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/book/new")
    public ResponseEntity<ApiResponse<BookResponse>> newBookController(@RequestBody BookRequest bookRequest) {
        ApiResponse<BookResponse> apiResponse = bookServiceImpl.newBookService(bookRequest);

        HttpStatus status = "Libro creado exitosamente.".equals(apiResponse.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @PutMapping("/book/update")
    public ResponseEntity<ApiResponse<BookResponse>> updateBookController(@RequestBody BookRequest bookRequest) {
        ApiResponse<BookResponse> apiResponse = bookServiceImpl.updateBookService(bookRequest);

        HttpStatus status = "Libro actualizado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBookController(@PathVariable Integer bookId) {
        ApiResponse<Void> apiResponse = bookServiceImpl.deleteBookService(bookId);

        HttpStatus status = "Libro eliminado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

}

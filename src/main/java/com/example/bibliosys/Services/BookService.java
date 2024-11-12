package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.request.book.BookRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.book.BookResponse;

public interface BookService {
    List<BookResponse> fetchAllBooksService();

    ApiResponse<BookResponse> newBookService(BookRequest bookRequest);

    ApiResponse<BookResponse> updateBookService(BookRequest bookRequest);

    ApiResponse<Void> deleteBookService(Integer bookId);
}

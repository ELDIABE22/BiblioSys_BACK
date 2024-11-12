package com.example.bibliosys.Repository;

import com.example.bibliosys.Models.BookAuthor;
import org.springframework.data.repository.CrudRepository;

public interface BookAuthorRepository extends CrudRepository<BookAuthor, BookAuthor.BookAuthorId> {
}

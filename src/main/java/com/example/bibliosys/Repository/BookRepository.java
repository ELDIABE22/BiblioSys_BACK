package com.example.bibliosys.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Optional<Book> findById(Integer id);
}

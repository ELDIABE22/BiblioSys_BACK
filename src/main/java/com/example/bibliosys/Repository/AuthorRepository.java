package com.example.bibliosys.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {

}

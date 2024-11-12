package com.example.bibliosys.Repository;

import com.example.bibliosys.Models.BookSubject;
import org.springframework.data.repository.CrudRepository;

public interface BookSubjectRepository extends CrudRepository<BookSubject, BookSubject.BookSubjectId> {
}

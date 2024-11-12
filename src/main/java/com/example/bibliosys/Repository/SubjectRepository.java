package com.example.bibliosys.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    Subject findByNombre(String nombre);
}

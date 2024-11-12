package com.example.bibliosys.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    public Student findByTelefono(String telefono);

}

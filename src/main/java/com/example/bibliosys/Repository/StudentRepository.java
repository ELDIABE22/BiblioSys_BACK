package com.example.bibliosys.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.Student;
import com.example.bibliosys.Models.User;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    public Student findByTelefono(String telefono);

    Optional<Student> findById(Integer id);

}

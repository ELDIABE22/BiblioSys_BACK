package com.example.bibliosys.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bibliosys.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsuario(String usuario);
    Optional<User> findByCorreo(String correo);
}

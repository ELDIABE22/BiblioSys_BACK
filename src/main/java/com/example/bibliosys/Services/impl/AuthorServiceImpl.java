package com.example.bibliosys.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.Author;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.author.AuthorResponse;
import com.example.bibliosys.Repository.AuthorRepository;
import com.example.bibliosys.Services.AuthorService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

        @Autowired
        private EntityManager entityManager;

        @Autowired
        private AuthorRepository authorRepository;

        @Override
        public List<Author> fetchAllAuthorsService() {
                Iterable<Author> authorsIterable = authorRepository.findAll();
                List<Author> authors = new ArrayList<>();
                authorsIterable.forEach(authors::add);
                return authors;
        }

        @Override
        public AuthorResponse newAuthorService(Author author) {
                Author authorEntity = Author.builder()
                                .nombre(author.getNombre())
                                .build();

                Author authorSave = authorRepository.save(authorEntity);

                AuthorResponse authorResponse = AuthorResponse.builder()
                                .author(authorSave)
                                .message("Autor creado exitosamente")
                                .build();

                return authorResponse;
        }

        @Override
        public ApiResponse<Author> updateAuthorService(Author author) {
                Author existingAuthor = authorRepository.findById(author.getId()).orElse(null);

                if (existingAuthor == null) {
                        return ApiResponse.<Author>builder()
                                        .data(null)
                                        .message("Autor no encontrado.")
                                        .build();
                }

                existingAuthor.setNombre(author.getNombre());
                Author updatedAuthor = authorRepository.save(existingAuthor);

                return ApiResponse.<Author>builder()
                                .data(updatedAuthor)
                                .message("Autor actualizado.")
                                .build();
        }

        @Override
        public ApiResponse<String> deleteAuthorService(Integer authorId) {
                StoredProcedureQuery deleteAuthorQuery = entityManager.createStoredProcedureQuery("sp_EliminarAutor")
                                .registerStoredProcedureParameter("IdAutor", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                deleteAuthorQuery.setParameter("IdAutor", authorId);
                deleteAuthorQuery.execute();

                String mensajeSalida = (String) deleteAuthorQuery.getOutputParameterValue("MensajeSalida");

                if ("Autor no encontrado.".equals(mensajeSalida)
                                | "No se puede eliminar el autor. Primero elimine los libros asociados."
                                                .equals(mensajeSalida)) {
                        return ApiResponse.<String>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                return ApiResponse.<String>builder()
                                .data(null)
                                .message(mensajeSalida)
                                .build();
        }

}

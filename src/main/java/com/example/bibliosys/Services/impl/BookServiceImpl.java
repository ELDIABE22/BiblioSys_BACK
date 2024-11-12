package com.example.bibliosys.Services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.request.book.BookRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.book.BookAuthorResponse;
import com.example.bibliosys.Models.response.book.BookResponse;
import com.example.bibliosys.Models.response.book.BookSubjectResponse;
import com.example.bibliosys.Services.BookService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class BookServiceImpl implements BookService {

        @Autowired
        private EntityManager entityManager;

        @Override
        public List<BookResponse> fetchAllBooksService() {
                StoredProcedureQuery query = entityManager
                                .createStoredProcedureQuery("sp_ObtenerLibrosConAutoresYMaterias");

                @SuppressWarnings("unchecked")
                List<Object[]> results = query.getResultList();

                Map<Integer, BookResponse> bookMap = new HashMap<>();
                Set<Integer> authorIds = new HashSet<>();
                Set<Integer> subjectIds = new HashSet<>();

                for (Object[] row : results) {
                        Integer bookId = (Integer) row[0];
                        BookResponse book = bookMap.computeIfAbsent(bookId, id -> {
                                authorIds.clear();
                                subjectIds.clear();
                                return BookResponse.builder()
                                                .id(bookId)
                                                .titulo((String) row[1])
                                                .descripcion((String) row[2])
                                                .ISBN((String) row[3])
                                                .genero((String) row[4])
                                                .añoPublicacion((Integer) row[5])
                                                .foto((String) row[6])
                                                .estado((String) row[7])
                                                .autores(new ArrayList<>())
                                                .materias(new ArrayList<>())
                                                .build();
                        });

                        if (row[8] != null && authorIds.add((Integer) row[8])) {
                                BookAuthorResponse author = BookAuthorResponse.builder()
                                                .id((Integer) row[8])
                                                .nombre((String) row[9])
                                                .build();
                                book.getAutores().add(author);
                        }
                        if (row[10] != null && subjectIds.add((Integer) row[10])) {
                                BookSubjectResponse subject = BookSubjectResponse.builder()
                                                .id((Integer) row[10])
                                                .nombre((String) row[11])
                                                .build();
                                book.getMaterias().add(subject);
                        }
                }

                return new ArrayList<>(bookMap.values());
        }

        @Override
        public ApiResponse<BookResponse> newBookService(BookRequest bookRequest) {
                // 1. Ejecutar el procedimiento para insertar el libro y recuperar su ID.
                StoredProcedureQuery insertBookQuery = entityManager.createStoredProcedureQuery("sp_InsertarLibro")
                                .registerStoredProcedureParameter("Titulo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Descripcion", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("ISBN", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Genero", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Año_Publicacion", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Foto", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NewBookId", Integer.class, ParameterMode.OUT)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                insertBookQuery.setParameter("Titulo", bookRequest.getTitulo());
                insertBookQuery.setParameter("Descripcion", bookRequest.getDescripcion());
                insertBookQuery.setParameter("ISBN", bookRequest.getISBN());
                insertBookQuery.setParameter("Genero", bookRequest.getGenero());
                insertBookQuery.setParameter("Año_Publicacion", bookRequest.getAñoPublicacion());
                insertBookQuery.setParameter("Foto", bookRequest.getFoto());

                insertBookQuery.execute();
                Integer bookId = (Integer) insertBookQuery.getOutputParameterValue("NewBookId");
                String mensajeSalida = (String) insertBookQuery.getOutputParameterValue("MensajeSalida");

                if ("El ISBN ya está registrado para otro libro.".equals(mensajeSalida)) {
                        return ApiResponse.<BookResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                // 2. Agregar autores asociados al libro
                for (Integer autorId : bookRequest.getAutores()) {
                        StoredProcedureQuery insertBookAuthorQuery = entityManager
                                        .createStoredProcedureQuery("sp_InsertarLibroAutor")
                                        .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                        .registerStoredProcedureParameter("IdAutor", Integer.class, ParameterMode.IN);

                        insertBookAuthorQuery.setParameter("IdLibro", bookId);
                        insertBookAuthorQuery.setParameter("IdAutor", autorId);
                        insertBookAuthorQuery.execute();
                }

                // 3. Agregar materias asociadas al libro
                for (Integer materiaId : bookRequest.getMaterias()) {
                        StoredProcedureQuery insertBookSubjectQuery = entityManager
                                        .createStoredProcedureQuery("sp_InsertarLibroMateria")
                                        .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                        .registerStoredProcedureParameter("IdMateria", Integer.class, ParameterMode.IN);

                        insertBookSubjectQuery.setParameter("IdLibro", bookId);
                        insertBookSubjectQuery.setParameter("IdMateria", materiaId);
                        insertBookSubjectQuery.execute();
                }

                BookResponse bookResponse = BookResponse.builder()
                                .id(bookId)
                                .titulo(bookRequest.getTitulo())
                                .descripcion(bookRequest.getDescripcion())
                                .ISBN(bookRequest.getISBN())
                                .genero(bookRequest.getGenero())
                                .añoPublicacion(bookRequest.getAñoPublicacion())
                                .foto(bookRequest.getFoto())
                                .estado("Disponible")
                                .autores(new ArrayList<>(bookRequest.getAutores().stream()
                                                .map(autorId -> BookAuthorResponse.builder()
                                                                .id(autorId)
                                                                .build())
                                                .collect(Collectors.toSet())))
                                .materias(new ArrayList<>(bookRequest.getMaterias().stream()
                                                .map(materiaId -> BookSubjectResponse.builder()
                                                                .id(materiaId)
                                                                .build())
                                                .collect(Collectors.toSet())))
                                .build();

                return ApiResponse.<BookResponse>builder()
                                .data(bookResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<Void> deleteBookService(Integer bookId) {
                StoredProcedureQuery deleteBookQuery = entityManager.createStoredProcedureQuery("sp_EliminarLibro")
                                .registerStoredProcedureParameter("LibroId", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                deleteBookQuery.setParameter("LibroId", bookId);

                deleteBookQuery.execute();
                String mensajeSalida = (String) deleteBookQuery.getOutputParameterValue("MensajeSalida");

                if ("El libro no existe.".equals(mensajeSalida)) {
                        return ApiResponse.<Void>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                return ApiResponse.<Void>builder()
                                .data(null)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<BookResponse> updateBookService(BookRequest bookRequest) {
                StoredProcedureQuery insertBookQuery = entityManager.createStoredProcedureQuery("sp_ActualizarLibro")
                                .registerStoredProcedureParameter("LibroId", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevoTitulo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevaDescripcion", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevoISBN", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevoGenero", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevoAño_Publicacion", Integer.class,
                                                ParameterMode.IN)
                                .registerStoredProcedureParameter("NuevaFoto", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                insertBookQuery.setParameter("LibroId", bookRequest.getId());
                insertBookQuery.setParameter("NuevoTitulo", bookRequest.getTitulo());
                insertBookQuery.setParameter("NuevaDescripcion", bookRequest.getDescripcion());
                insertBookQuery.setParameter("NuevoISBN", bookRequest.getISBN());
                insertBookQuery.setParameter("NuevoGenero", bookRequest.getGenero());
                insertBookQuery.setParameter("NuevoAño_Publicacion", bookRequest.getAñoPublicacion());
                insertBookQuery.setParameter("NuevaFoto", bookRequest.getFoto());

                insertBookQuery.execute();
                String mensajeSalida = (String) insertBookQuery.getOutputParameterValue("MensajeSalida");

                if ("El libro no existe.".equals(mensajeSalida) | "El ISBN ya está registrado.".equals(mensajeSalida)) {
                        return ApiResponse.<BookResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                StoredProcedureQuery deleteBookAuthorQuery = entityManager
                                .createStoredProcedureQuery("sp_EliminarLibroAutor")
                                .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                StoredProcedureQuery deleteBookSubjectQuery = entityManager
                                .createStoredProcedureQuery("sp_EliminarLibroMateria")
                                .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                deleteBookAuthorQuery.setParameter("IdLibro", bookRequest.getId());
                deleteBookSubjectQuery.setParameter("IdLibro", bookRequest.getId());

                deleteBookAuthorQuery.execute();
                deleteBookSubjectQuery.execute();

                String mensajeBookAuthorSalida = (String) deleteBookAuthorQuery
                                .getOutputParameterValue("MensajeSalida");
                String mensajeBookSubjectSalida = (String) deleteBookSubjectQuery
                                .getOutputParameterValue("MensajeSalida");

                if ("Autores del libro eliminados.".equals(mensajeBookAuthorSalida)) {
                        // 2. Actualizar autores asociados al libro
                        for (Integer autorId : bookRequest.getAutores()) {
                                StoredProcedureQuery insertBookAuthorQuery = entityManager
                                                .createStoredProcedureQuery("sp_InsertarLibroAutor")
                                                .registerStoredProcedureParameter("IdLibro", Integer.class,
                                                                ParameterMode.IN)
                                                .registerStoredProcedureParameter("IdAutor", Integer.class,
                                                                ParameterMode.IN);

                                insertBookAuthorQuery.setParameter("IdLibro", bookRequest.getId());
                                insertBookAuthorQuery.setParameter("IdAutor", autorId);
                                insertBookAuthorQuery.execute();
                        }
                }

                if ("Materias del libro eliminadas.".equals(mensajeBookSubjectSalida)) {
                        // 3. Actualizar materias asociadas al libro
                        for (Integer materiaId : bookRequest.getMaterias()) {
                                StoredProcedureQuery insertBookSubjectQuery = entityManager
                                                .createStoredProcedureQuery("sp_InsertarLibroMateria")
                                                .registerStoredProcedureParameter("IdLibro", Integer.class,
                                                                ParameterMode.IN)
                                                .registerStoredProcedureParameter("IdMateria", Integer.class,
                                                                ParameterMode.IN);

                                insertBookSubjectQuery.setParameter("IdLibro", bookRequest.getId());
                                insertBookSubjectQuery.setParameter("IdMateria", materiaId);
                                insertBookSubjectQuery.execute();
                        }
                }

                BookResponse bookResponse = BookResponse.builder()
                                .id(bookRequest.getId())
                                .titulo(bookRequest.getTitulo())
                                .descripcion(bookRequest.getDescripcion())
                                .ISBN(bookRequest.getISBN())
                                .genero(bookRequest.getGenero())
                                .añoPublicacion(bookRequest.getAñoPublicacion())
                                .foto(bookRequest.getFoto())
                                .estado("Disponible")
                                .autores(new ArrayList<>(bookRequest.getAutores().stream()
                                                .map(autorId -> BookAuthorResponse.builder()
                                                                .id(autorId)
                                                                .build())
                                                .collect(Collectors.toSet())))
                                .materias(new ArrayList<>(bookRequest.getMaterias().stream()
                                                .map(materiaId -> BookSubjectResponse.builder()
                                                                .id(materiaId)
                                                                .build())
                                                .collect(Collectors.toSet())))
                                .build();

                return ApiResponse.<BookResponse>builder()
                                .data(bookResponse)
                                .message(mensajeSalida)
                                .build();
        }

}

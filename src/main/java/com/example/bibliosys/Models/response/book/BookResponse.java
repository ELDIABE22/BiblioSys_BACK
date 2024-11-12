package com.example.bibliosys.Models.response.book;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String ISBN;
    private String genero;
    private Integer añoPublicacion;
    private String foto;
    private String estado;
    private List<BookAuthorResponse> autores;
    private List<BookSubjectResponse> materias;
}

package com.example.bibliosys.Models.request.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String ISBN;
    private String genero;
    private Integer añoPublicacion;
    private String foto;
    private List<Integer> autores;
    private List<Integer> materias;
}

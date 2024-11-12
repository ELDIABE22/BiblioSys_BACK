package com.example.bibliosys.Models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "Libro")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descripcion;
    private String ISBN;
    private String genero;
    private Integer añoPublicacion;
    private String foto;
    private String estado;

    @OneToMany
    @JoinColumn(name = "idLibro", referencedColumnName = "id")
    private List<BookAuthor> autores;

    @OneToMany
    @JoinColumn(name = "idLibro", referencedColumnName = "id")
    private List<BookSubject> materias;
}

package com.example.bibliosys.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LibroAutor")
@IdClass(BookAuthor.BookAuthorId.class)
public class BookAuthor {
    @Id
    private Integer idLibro;

    @Id
    private Integer idAutor;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BookAuthorId implements Serializable {
        private Integer idLibro;
        private Integer idAutor;
    }
}

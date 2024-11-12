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
@Table(name = "LibroMateria")
@IdClass(BookSubject.BookSubjectId.class)
public class BookSubject {
    @Id
    private Integer idLibro;

    @Id
    private Integer idMateria;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BookSubjectId implements Serializable {
        private Integer idLibro;
        private Integer idMateria;
    }
}

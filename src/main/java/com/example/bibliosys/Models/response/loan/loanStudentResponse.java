package com.example.bibliosys.Models.response.loan;

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
public class loanStudentResponse {
    private Integer id;
    private String nombres;
    private String apellidos;
}

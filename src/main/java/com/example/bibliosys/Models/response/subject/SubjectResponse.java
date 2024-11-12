package com.example.bibliosys.Models.response.subject;

import com.example.bibliosys.Models.Subject;

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
public class SubjectResponse {
    private Subject subject;
    private String message;
}

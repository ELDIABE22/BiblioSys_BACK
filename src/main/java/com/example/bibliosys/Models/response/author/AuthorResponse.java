package com.example.bibliosys.Models.response.author;

import com.example.bibliosys.Models.Author;

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
public class AuthorResponse {
    private Author author;
    private String message;
}

package com.devsuperior.sendbookemailsb.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    private Integer id;
    private String name;
    private String description;
    private String author;
    private String category;

}

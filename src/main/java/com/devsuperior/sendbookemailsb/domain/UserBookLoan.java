package com.devsuperior.sendbookemailsb.domain;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserBookLoan {

    private User user;
    private Book book;
    private Date loanDate;
}

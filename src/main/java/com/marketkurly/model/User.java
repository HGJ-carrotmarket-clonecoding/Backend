package com.marketkurly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketkurly.dto.requestDto.SignupRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String username;

    public User(SignupRequestDto signupRequestDto) {
        this.email = signupRequestDto.getEmail();
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
    }
}

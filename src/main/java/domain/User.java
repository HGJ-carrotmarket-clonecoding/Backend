package domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dto.requestDto.SignupRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User {

    //번호 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Review> reviewList =new ArrayList<>();


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Liked> likedList = new ArrayList<>();

    public User(SignupRequestDto signupRequestDto){
        this.email = signupRequestDto.getEmail();
        this.username = signupRequestDto.getEmail();
        this.password = signupRequestDto.getPassword();

    }
}

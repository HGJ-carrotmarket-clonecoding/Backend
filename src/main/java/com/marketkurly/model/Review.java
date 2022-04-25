package com.marketkurly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketkurly.dto.requestDto.ReviewRequestDto;
import com.marketkurly.util.Timestamped;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Long likedCount;

    @OneToMany(mappedBy = "review")
    @JsonIgnore
    private List<Liked> likedList = new ArrayList<>();

    public Review(ReviewRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.likedCount = 0L;
        this.user = user;
    }

    public void addReview(Product product) {
        this.product = product;
        product.getReviews().add(this);
    }

    public void deleteReview() {
        this.user.getReviewList().remove(this);
        this.product.getReviews().remove(this);

    }

    public void plusCountLiked() {
        this.likedCount += 1;
    }

    public void minusCountLiked() {

        this.likedCount -= 1;
    }

}

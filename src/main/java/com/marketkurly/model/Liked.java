package com.marketkurly.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Liked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public Liked(Review review, User user) {
        this.user = user;
        this.review = review;
        this.review.getLikedList().add(this);
        this.review.plusCountLiked();
    }

    public void delete() {
        this.review.getLikedList().remove(this);
        this.review.minusCountLiked();
    }
}
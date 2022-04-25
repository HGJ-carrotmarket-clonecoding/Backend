package com.marketkurly.service;

import com.marketkurly.dto.requsetDto.LikeRequestDto;
import com.marketkurly.exception.NoItemException;
import com.marketkurly.model.Liked;
import com.marketkurly.model.Review;
import com.marketkurly.model.User;
import com.marketkurly.repository.LikedRepository;
import com.marketkurly.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikedService {

    private final ReviewRepository reviewRepository;
    private final LikedRepository likedRepository;

    @Autowired
    public LikedService(ReviewRepository reviewRepository, LikedRepository likedRepository) {
        this.reviewRepository = reviewRepository;
        this.likedRepository = likedRepository;
    }

    @Transactional
    public boolean likedReview(LikeRequestDto requestDto, User user) {
        Review review = reviewRepository.findById(requestDto.getReviewId()).orElseThrow(
                () -> new NoItemException("해당 리뷰를 찾을 수 없습니다.")
        );
        Liked liked = likedRepository.findByUserIdAndReviewId(user.getId(), review.getId()).orElse(null);
        if (liked == null) {
            liked = new Liked(review, user);
            likedRepository.save(liked);
            return true;
        }
        liked.delete();
        likedRepository.delete(liked);
        return false;
    }
}

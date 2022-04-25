package com.marketkurly.repository;

import com.marketkurly.model.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    Optional<Liked> findByUserIdAndReviewId(Long userId, Long reviewId);
}

package com.marketkurly.repository;

import com.marketkurly.model.Product;
import com.marketkurly.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByProduct(Product product, Pageable pageable);
    Long countByProductId(Long productId);
    }

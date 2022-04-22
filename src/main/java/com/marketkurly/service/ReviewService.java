package com.marketkurly.service;

import com.marketkurly.dto.reponseDto.ReviewResponseDto;
import com.marketkurly.dto.requestDto.ReviewRequestDto;
import com.marketkurly.exception.NoItemException;
import com.marketkurly.model.Product;
import com.marketkurly.model.Review;
import com.marketkurly.model.User;
import com.marketkurly.repository.ProductRepository;
import com.marketkurly.repository.ReviewRepository;
import com.marketkurly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(UserRepository userRepository, ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ReviewResponseDto createReview(User user, ReviewRequestDto requestDto) {
        Product product = loadProduct(requestDto.getProductId());          //product 존재하는지 확인
        Review savedReview = reviewRepository.save(new Review(requestDto, user));
        savedReview.addReview(product);

        return new ReviewResponseDto(savedReview, user);
    }

    private Product loadProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NoItemException("해당 상품이 존재하지 않습니다.")
        );
    }
}

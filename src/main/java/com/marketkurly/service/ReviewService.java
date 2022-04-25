package com.marketkurly.service;

import com.marketkurly.dto.responseDto.ReviewListResponseDto;
import com.marketkurly.dto.responseDto.ReviewResponseDto;
import com.marketkurly.dto.requestDto.ReviewRequestDto;
import com.marketkurly.exception.NoItemException;
import com.marketkurly.exception.UnauthenticatedException;
import com.marketkurly.model.Product;
import com.marketkurly.model.Review;
import com.marketkurly.model.User;
import com.marketkurly.repository.ProductRepository;
import com.marketkurly.repository.ReviewRepository;
import com.marketkurly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public ReviewListResponseDto getReviewList(Long productId,int page,int display,User user){
        Product product = loadProduct(productId);

        PageRequest pageRequest = PageRequest.of(page -1,display, Sort.by("createdAt").descending());
        List<ReviewResponseDto> reviews = reviewRepository.findAllByProduct(product,pageRequest)
                .stream().map(o -> new ReviewResponseDto(o,user))
                .collect(Collectors.toCollection(ArrayList::new));
        Long reviewCount = reviewRepository.countByProductId(productId);

        return new ReviewListResponseDto(reviews,reviewCount,page,display);

    }
    @Transactional
    public void deleteReview(User user,Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new NoItemException("해당 댓글이 존재하지 않습니다."));
        if (review.getUser().equals(user)) {
            review.deleteReview();
            reviewRepository.delete(review);
        } else {
            throw new UnauthenticatedException("삭제권한이 없습니다.");
        }
    }
    private Product loadProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NoItemException("해당 상품이 존재하지 않습니다.")
        );
    }

}

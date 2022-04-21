package com.marketkurly.controller;


import com.marketkurly.dto.responseDto.ResponseDto;
import com.marketkurly.model.User;
import com.marketkurly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "Liked Controller Api V1")
public class LikedController {

    private final LikedService likedService;
    private final UserService userService;

    @Autowired
    public LikedController(LikedService likedService, UserService userService) {
        this.likedService = likedService;
        this.userService = userService;
    }

    @Operation(summary = "좋아요/좋아요 취소")
    @PostMapping("liked")
    public ResponseDto liked(@RequestBody LikeRequestDto requestDto, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("POST, '/liked', reviewId={}", requestDto.getReviewId());
        if (userDetails == null) {
            throw new NoneLoginException("로그인 사용자만 이용할 수 있습니다.");
        }
        User user = userService.loadUserEamil(userDetails.getUsername());
        if (likedService.likedReview(requestDto, user)) {
            return new ResponseDto("success", "좋아요를 하였습니다.", "");
        }
        return new ResponseDto("success", "좋아요를 취소하였습니다.", "");
    }
}

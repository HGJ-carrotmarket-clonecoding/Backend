package dto.responseDto;


import domain.Liked;
import domain.Review;
import domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long likedCount;
    private boolean isLiked;

    public ReviewResponseDto(Review review, User user) {
        this.reviewId = review.getId();
        this.username = review.getUser().getUsername();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.likedCount = review.getLikeCount();
        for (Liked liked : review.getLikedList()) {
            if (user == null) {
                this.isLiked = false;
            }
            else if (liked.getUser().equals(user)) {
                this.isLiked = true;
            } else {
                this.isLiked = false;
            }
        }
    }
}

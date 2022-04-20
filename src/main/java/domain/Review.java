package domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import dto.requestDto.ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import util.Timestamped;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Review extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Long likeCount;

    //좋아요 조인 리뷰는 하는 인데 좋아요는 여러게 를 사용할수있어서  OneToMany 씀
    @OneToMany(mappedBy = "review")
    @JsonIgnore
    private List<Liked> likedList = new ArrayList<>();


    public Review(ReviewRequestDto reviewRequestDto , User user){
        this.title = reviewRequestDto.getTitle();
        this.content = reviewRequestDto.getContent();
        this.likeCount = 0L;
        this.user = user;
    }

    //상품 리뷰 쓰기
    public void addReview(Product product){
        this.product = product;
        product.getReviews().add(this);
    }
    //유저가 선택한 상품 에 리뷰 를 삭제
    public void deleteReview(){
        this.user.getReviewList().remove(this);
        this.product.getReviews().remove(this);
    }

    public void piusCountLiked(){
        this.likeCount +=1;
    }
    public void minusCountLiked(){
        this.likeCount -=1;
    }

}

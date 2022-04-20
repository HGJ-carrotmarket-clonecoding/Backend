package dto.responseDto;



import domain.Cart;
import domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

    private Long productId;
    private String name;
    private String category1;
    private String category2;
    private String image;
    private String description;
    private Long price;
    private int viewCount;
    private Long amount;

    public ProductResponseDto(Cart cart) {
        Product product = cart.getProduct();
        this.productId = product.getId();
        this.name = product.getName();
        this.category1 = product.getCategory1();
        this.category2 = product.getCategory2();
        this.image = product.getImage();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.viewCount = product.getViewCount();
        this.amount = cart.getAmount();

    }
}

package dto.requestDto;


import org.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductRequestDto {

    private String title;
    private Long price;
    private String description;
    private String category1;
    private String category2;
    private String image;

    public ProductRequestDto(JSONObject itemJson){
        this.title = itemJson.getString("title").replace("<b>","").replace("</b>","");
        this.price = itemJson.getLong("lprice");
        this.description = itemJson.getString("category1") + ">" + itemJson.getString("category2") + ">" +itemJson.getString("category3") + ">"
                + itemJson.getString("mallName") + itemJson.getString("maker");
        this.category1 = itemJson.getString("category1");
        this.category2 = itemJson.getString("category2");
        this.image = itemJson.getString("image");
    }

}

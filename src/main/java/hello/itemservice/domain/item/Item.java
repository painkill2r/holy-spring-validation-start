package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank(message = "공백은 입력할 수 없습니다.") //빈값 + 공백만 있는 경우를 허용하지 않음.
    private String itemName;

    @NotNull //NULL을 허용하지 않음.
    @Range(min = 1000, max = 1000000) //범위 안의 값이어야함.
    private Integer price;

    @NotNull
    @Max(9999) //최대 9999까지만 허용.
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}

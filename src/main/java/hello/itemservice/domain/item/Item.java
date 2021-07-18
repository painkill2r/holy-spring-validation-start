package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
/**
 * 오브젝트 오류 설정
 * 실무에서는 검증 기능이 해당 객체의 범위를 넘어서는 경우들도 있기 때문에
 * 오브젝트 오류(글로벌 오류)의 경우 @ScriptAssert를 억지로 사용하는 것보다는
 * 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장함.
 */
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10,000원 넘게 입력해 주세요.")
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

package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageCodesResolverTest {

    /**
     * 스프링이 제공하는 MessageCodesResolver
     * <p>
     * 검증 오류 코드로 메시지 코드들을 생성함.
     * 'MessageCodesResolver'는 인터페이스이고 기본 구현체로 'DefaultMessageCodesResolver'가 있다.
     * 주로 'ObjectError', 'FieldError와' 함꼐 사용됨.
     * <p>
     * <p>
     * [DefaultMessageCodesResolver의 기본 메시지 생성 규칙]
     * 1. 객체 오류(2가지)
     * - Code + "." + Object Name
     * - Code
     * - 예) 오류 코드가 required이고 Obejct Name가 item인 경우
     * 1. "required.item"
     * 2. "required"
     * <p>
     * 2. 필드 오류(4가지)
     * - Code + "." + Object Name + "." + Field Name
     * - Code + "." + Field Name
     * - Code + "." + Field Type
     * - Code
     * - 예) 오류 코드가 typeMismatch이고 Obejct Name가 user이고 Field Name이 'age'이고, Field Type이 'int'인 경우
     * 1. "typeMismatch.user.age"
     * 2. "typeMismatch.age"
     * 3. "typeMismatch.int"
     * 4. "typeMismatch"
     * <p>
     * <p>
     * [동작 방식]
     * 1. BindingResult.rejectValue(), BindingResult.reject()는 내부에서 'MessageCodesResolver'를 사용하며 여기에서 메시지 코드들을 생성한다.
     * 2. FieldError: MessageCodesResolver를 통해서 생성된 4가지 오류 코드를 순서대로 보관
     * 3. ObjectError: MessageCodesResolver를 통해서 생성된 2가지 오류 코드를 순서대로 보관
     */
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        // 객체에 대한 오류 메시지 생성
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        // 객체의 필드에 대한 오류 메시지 생성
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);

        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
    }
}

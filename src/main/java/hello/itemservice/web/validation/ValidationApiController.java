package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationApiController {

    /**
     * API의 경우 3가지로 나누어 생각해야 함.
     * 1. 성공 요청: 성공
     * 2. 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함.
     * 3. 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함.
     * <p>
     * HTTP 요청 파라미터를 처리하는 @ModelAttribute는 각각의 필드 단위로 세밀하게 적용된다. 그래서 특정 필드에 타입이 맞지 않는
     * 오류가 발생해도 나머지 필드는 정상 처리할 수 있다.
     * HttpMessageConverter는 @ModelAttribute와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용된다.
     * 따라서 메시지 컨버터의 작동이 성공해서 Item 객체를 만들어야 @Validated(또는 @Valid)가 적요된다.
     *
     * @param form
     * @param bindingResult
     * @return
     * @ModelAttribute 필드 단위로 정교하게 바인딩이 적용된다. 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 적용할 수 있다.
     * @RequestBody HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다.(컨트롤러 호출 x, Validator 적용 x)
     */
    @PostMapping("/add")
    public Object addItem(@Validated @RequestBody ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);

            return bindingResult.getAllErrors(); //BindingResult가 가지고 있는 모든 오류(ObjectError, FieldError)를 반환
        }

        log.info("성공 로직 실행");

        return form;
    }
}

package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    /**
     * 해당 컨트롤러가 호출 될 때마다 실행
     *
     * @param dataBinder WebDataBinder는 스프링 파라미터 바인딩의 역할을 해주고, 검증 기능도 내부에 포함함.
     */
    /*
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator); //WebDataBinder에 검증기를 추가하면 해당 컨트롤러 호출시 검증기를 자동으로 적용함.
    }
    */

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * @param item
     * @param bindingResult      Item 객체에 요청 파라미터들이 바인딩 된 결과를 담는 객체(자동으로 View에 같이 넘어감.)
     * @param redirectAttributes
     * @param model
     * @return
     */
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //필드 오류: new FieldError("@ModelAttribute 이름", "오류가 발생한 필드명", "오류 기본 메시지")
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000원 까지 허용합니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                //글로벌 오류: new ObjectError(""@ModelAttribute 이름", "오류 기본 메시지")
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //필드 오류: new FieldError("@ModelAttribute 이름", "오류가 발생한 필드명", "실패한 값", "타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값", "메시지 코드", "메시지에서 사용하는 인자", "오류 기본 메시지")
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000원 까지 허용합니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                //글로벌 오류: new ObjectError(""@ModelAttribute 이름", "메시지 코드", "메시지에서 사용하는 인자", "오류 기본 메시지")
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName()); //에러 처리할 객체명(@ModelAttribute name)
        log.info("target={}", bindingResult.getTarget()); // 에러 처리할 필드 목록

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //필드 오류: new FieldError("@ModelAttribute 이름", "오류가 발생한 필드명", "실패한 값", "타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값", "메시지 코드", "메시지에서 사용하는 인자", "오류 기본 메시지")
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName", "required.default"}, null, null));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                //글로벌 오류: new ObjectError(""@ModelAttribute 이름", "메시지 코드", "메시지에서 사용하는 인자", "오류 기본 메시지")
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName()); //에러 처리할 객체명(@ModelAttribute name)
        log.info("target={}", bindingResult.getTarget()); // 에러 처리할 필드 목록

        //검증 로직
        //ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        if (!StringUtils.hasText(item.getItemName())) {
            //bindingResult.rejectValue("오류가 발생한 필드명", "에러 코드 첫번째 단어[이름 규칙=>에러코드.객체명.필드명]")
            //bindingResult.rejectValue("오류가 발생한 필드명", "에러 코드 첫번째 단어[이름 규칙=>에러코드.객체명.필드명]", "메시지에서 사용하는 인자", "기본 오류 메시지")
            bindingResult.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{10000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{10000}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                //bindingResult.reject("에러 코드")
                //bindingResult.reject("에러 코드", "메시지에서 사용하는 인자", "기본 오류 메시지")
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        itemValidator.validate(item, bindingResult);

        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * 자동으로 검증 로직을 수행하도록 하기 위해 @Validated(검증기를 실행하기 위한 애노테이션) 설정 추가
     * WebDataBinder에 등록한 검증기를 찾아서 실행함.
     * 만약, 여러 검증기를 등록한 경우 어떤 검증기가 실행되어야 할지 구분이 필요한데, 이때 supports()가 사용됨.
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}


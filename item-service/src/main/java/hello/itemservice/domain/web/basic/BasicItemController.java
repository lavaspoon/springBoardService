package hello.itemservice.domain.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    //BasicItemController가 스프링빈에 등록되면서, itemRepository가 생성자등록으로 인해 스프링빈에서 주입됨
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    //아이템 목록 출력
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }
    //상품 상세
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }
    //상품 등록
    @GetMapping("/add")
    public String addForm(){
        return "/basic/addForm";
    }
    //상품 등록 v1
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                        Model model){
            Item item = new Item();
            item.setItemName(itemName);
            item.setPrice(price);
            item.setQuantity(quantity);

            itemRepository.save(item);
            //아이템 상세 페이지로
            model.addAttribute("item",item);
        return "/basic/item";
    }


    //상품 등록 v2
 //   @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        itemRepository.save(item);
    // "item"쓰면 생략 가능(Model model도 생략가능)   model.addAttribute("item",item);
        return "/basic/item";
    }
    //상품 등록 v3
    //   @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);
        return "/basic/item";
    }
    //상품 등록 v4
    @PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }
    //상품 수정 페이지
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    //상품 수정
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
    /**
     * 테스트용
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,50));
        itemRepository.save(new Item("itemB",20000,100));
    }
}

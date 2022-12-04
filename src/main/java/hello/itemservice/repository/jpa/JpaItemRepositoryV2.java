package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Override
    public Item save(Item item) {
        return springDataJpaItemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = springDataJpaItemRepository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        //반환타입이 이미 Optional
        return springDataJpaItemRepository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        //실무에서는 동적쿼리를 따로 쓰지 이렇게 따로따로 만들지 않는다.
        if(StringUtils.hasText(itemName) && maxPrice != null) {
//            너무 길이가 기니 아래 것으로 사용
//            return springDataJpaItemRepository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice);
            return springDataJpaItemRepository.findItems("%" + itemName + "%", maxPrice);
        } else if (StringUtils.hasText(itemName)) {
            return  springDataJpaItemRepository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) {
            return springDataJpaItemRepository.findByPriceLessThanEqual(maxPrice);
        } else {
            return springDataJpaItemRepository.findAll();
        }
    }
}

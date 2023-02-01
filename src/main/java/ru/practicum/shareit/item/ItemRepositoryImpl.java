package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item update(Item item) {
        Item newItem = itemRepository.findById(item.getId()).get();
        if (item.getName() != null)
            newItem.setName(item.getName());
        if (item.getDescription() != null)
            newItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            newItem.setAvailable(item.getAvailable());
        return itemRepository.save(newItem);
    }
}

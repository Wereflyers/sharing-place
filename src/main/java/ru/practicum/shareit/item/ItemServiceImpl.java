package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemDto> getAllForUser(long userId) {
        if (userRepository.get(userId) == null)
            throw new NullPointerException("User " + userId + " is not found");
        return itemRepository.getAllForUser(userId);
    }

    @Override
    public ItemDto get(long id) {
        if (itemRepository.get(id) == null)
            throw new NullPointerException("Item " + id + " is not found.");
        return itemRepository.get(id).toItemDto();
    }

    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        validateItem(itemDto);
        if (userRepository.get(userId) == null)
            throw new NullPointerException("User " + userId + " is not found");
        itemDto.setOwnerId(userId);
        return itemRepository.add(itemDto).toItemDto();
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        if (itemRepository.get(id) == null)
            throw new NullPointerException("Item " + itemDto.getId() + " is not found.");
        if (itemRepository.get(id).getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemDto.setId(id);
        return itemRepository.update(itemDto).toItemDto();
    }

    @Override
    public boolean delete(long userId, long id) {
        if (itemRepository.get(id) == null)
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.get(id).getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        return itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> search(String req) {
        if (req.isBlank())
            return new ArrayList<>();
        return itemRepository.search(req);
    }

    private void validateItem(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank())
            throw new ValidationException("Name is null");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank())
            throw new ValidationException("Description is null");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("Available is null");
    }
}

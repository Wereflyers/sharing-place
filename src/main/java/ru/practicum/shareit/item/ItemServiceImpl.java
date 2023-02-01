package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto get(long id) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        return ItemMapper.toItemDto(itemRepository.findById(id).get());
    }

    @Override
    @Transactional
    public ItemDto add(long userId, ItemDto itemDto) {
        validateItem(itemDto);
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, userId)));
    }

    @Override
    @Transactional
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.findById(id).get().getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemDto.setId(id);
        return ItemMapper.toItemDto(itemRepository.update(ItemMapper.toItem(itemDto, userId)));
    }

    @Override
    @Transactional
    public void delete(long userId, long id) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.findById(id).get().getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String req) {
        if (req.isBlank())
            return new ArrayList<>();
        return itemRepository.search(req).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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

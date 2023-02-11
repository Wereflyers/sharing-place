package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemRequestForResponse add(long userId, ItemRequestDto itemRequestDto) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User not found");
        }
        return createResponse(itemRequestRepository.save(ItemRequestMapper.addItemRequest(itemRequestDto, userId)));
    }

    @Override
    public ItemRequestForResponse get(long userId, long id) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User not found");
        }
        if (itemRequestRepository.findById(id).isEmpty()) {
            throw new NullPointerException("Request not found");
        }
        return createResponse(itemRequestRepository.findById(id).get());
    }

    @Override
    public List<ItemRequestForResponse> getRequests(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User not found");
        }
        if (itemRequestRepository.findAllByUserIdOrderByCreated(userId).isEmpty()) {
            return null;
        }
        return itemRequestRepository.findAllByUserIdOrderByCreated(userId).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestForResponse> getAll(long userId, int from, int size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User not found");
        }
        if (from < 0 || size <= 0) {
            throw new ValidationException("Wrong parameters");
        }
        return itemRequestRepository.findAll(PageRequest.of(from/size, size)).stream()
                .filter(r -> r.getUserId() != userId)
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private ItemRequestForResponse createResponse(ItemRequest itemRequest) {
        List<ItemDto> responses = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        if (responses.isEmpty()) {
            return ItemRequestMapper.toResponse(itemRequest, false, null);
        } else {
            return ItemRequestMapper.toResponse(itemRequest, true, responses);
        }
    }
}

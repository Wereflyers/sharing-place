package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;

import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemForResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam int from, @RequestParam int size) {
        return itemService.getAllForUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemForResponse get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.get(id,userId);
    }

    @PostMapping
    public ItemForResponse add(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemForResponse update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                  @RequestBody ItemDto itemDto) {
        return itemService.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public ItemForResponse delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.delete(userId, id);
    }

    @GetMapping("/search")
    public List<ItemForResponse> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam("text") String req,
                                        @RequestParam int from, @RequestParam int size) {
        return itemService.search(req, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentForResponse addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                         @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, id, commentDto);
    }
}

package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;

import javax.validation.Valid;
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
                                        @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "100") int size) {
        return itemService.getAllForUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemForResponse get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.get(id,userId);
    }

    @PostMapping
    public ItemForResponse add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemForResponse update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                  @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        itemService.delete(userId, id);
    }

    @GetMapping("/search")
    public List<ItemForResponse> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam("text") String req,
                                        @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "100") int size) {
        return itemService.search(req, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentForResponse addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                         @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, id, commentDto);
    }
}

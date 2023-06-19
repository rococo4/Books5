package io.bootify.books.rest;

import io.bootify.books.model.CartDTO;
import io.bootify.books.service.CartService;
import io.bootify.books.util.NotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.http.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartResource {

    private final CartService cartService;

    public CartResource(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cartService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCart(@RequestBody @Valid final CartDTO cartDTO) {
        Long bookId = cartDTO.getBooks();
        var book = BookResource.bs.get(bookId);
        System.out.println(book.getName());
        System.out.println(cartDTO.getBooks());
        if (book.getQuantity() < cartDTO.getQuantity()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        book.setQuantity(book.getQuantity() - cartDTO.getQuantity());
        BookResource.bs.update(bookId, book);
        final Long createdId = cartService.create(cartDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCart(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CartDTO cartDTO) {
        Long bookId = cartDTO.getBooks();
        var book = BookResource.bs.get(bookId);
        if (book.getQuantity() < cartDTO.getQuantity()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        cartDTO.setBooks(bookId);
        book.setQuantity(book.getQuantity() - cartDTO.getQuantity());
        BookResource.bs.update(bookId, book);
        cartService.update(id, cartDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "id") final Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

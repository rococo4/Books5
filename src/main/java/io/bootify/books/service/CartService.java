package io.bootify.books.service;

import io.bootify.books.domain.Book;
import io.bootify.books.domain.Cart;
import io.bootify.books.model.CartDTO;
import io.bootify.books.repos.BookRepository;
import io.bootify.books.repos.CartRepository;
import io.bootify.books.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    public CartService(final CartRepository cartRepository, final BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    public List<CartDTO> findAll() {
        final List<Cart> carts = cartRepository.findAll(Sort.by("id"));
        return carts.stream()
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .toList();
    }

    public CartDTO get(final Long id) {
        return cartRepository.findById(id)
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CartDTO cartDTO) {
        final Cart cart = new Cart();
        mapToEntity(cartDTO, cart);
        return cartRepository.save(cart).getId();
    }

    public void update(final Long id, final CartDTO cartDTO) {
        final Cart cart = cartRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cartDTO, cart);
        cartRepository.save(cart);
    }

    public void delete(final Long id) {
        cartRepository.deleteById(id);
    }

    private CartDTO mapToDTO(final Cart cart, final CartDTO cartDTO) {
        cartDTO.setId(cart.getId());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setBooks(cart.getBooks() == null ? null : cart.getBooks().getId());
        return cartDTO;
    }

    private Cart mapToEntity(final CartDTO cartDTO, final Cart cart) {
        cart.setQuantity(cartDTO.getQuantity());
        final Book books = cartDTO.getBooks() == null ? null : bookRepository.findById(cartDTO.getBooks())
                .orElseThrow(() -> new NotFoundException("books not found"));
        cart.setBooks(books);
        return cart;
    }

    public boolean booksExists(final Long id) {
        return cartRepository.existsByBooksId(id);
    }

}

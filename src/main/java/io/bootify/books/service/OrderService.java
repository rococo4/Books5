package io.bootify.books.service;

import io.bootify.books.domain.Cart;
import io.bootify.books.domain.Order;
import io.bootify.books.model.OrderDTO;
import io.bootify.books.repos.CartRepository;
import io.bootify.books.repos.OrderRepository;
import io.bootify.books.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(final OrderRepository orderRepository,
            final CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setCart(order.getCart() == null ? null : order.getCart().getId());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setOrderId(orderDTO.getOrderId());
        final Cart cart = orderDTO.getCart() == null ? null : cartRepository.findById(orderDTO.getCart())
                .orElseThrow(() -> new NotFoundException("cart not found"));
        order.setCart(cart);
        return order;
    }

    public boolean cartExists(final Long id) {
        return orderRepository.existsByCartId(id);
    }

}

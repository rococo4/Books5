package io.bootify.books.repos;

import io.bootify.books.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByCartId(Long id);

}

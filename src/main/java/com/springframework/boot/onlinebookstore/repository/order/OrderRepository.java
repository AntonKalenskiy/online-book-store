package com.springframework.boot.onlinebookstore.repository.order;

import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Order;
import com.springframework.boot.onlinebookstore.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.orderItems WHERE o.user = :user")
    List<Order> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.orderItems WHERE o.user = :user")
    List<Order> findAllByUser(@Param("user") User user);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(Long orderId);
}

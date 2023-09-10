package com.springframework.boot.onlinebookstore.repository.shoppingcart;

import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT DISTINCT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems ci "
            + "WHERE sc.user = :user")
    ShoppingCart findByUser(@Param("user") User user);
}

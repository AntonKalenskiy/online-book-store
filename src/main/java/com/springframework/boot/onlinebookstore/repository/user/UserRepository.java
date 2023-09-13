package com.springframework.boot.onlinebookstore.repository.user;

import com.springframework.boot.onlinebookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u From User u JOIN FETCH u.roles where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}

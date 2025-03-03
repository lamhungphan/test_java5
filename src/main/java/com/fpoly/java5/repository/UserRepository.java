package com.fpoly.java5.repository;

import com.fpoly.java5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByFullname(String fullname);

    Optional<User> findByEmail(String email);

    @Query("FROM User o WHERE o.email LIKE ?1 OR o.id LIKE ?1")
    List<User> findByEmailOrId(String keyword);
}

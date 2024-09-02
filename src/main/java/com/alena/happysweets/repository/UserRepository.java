package com.alena.happysweets.repository;

import com.alena.happysweets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  //Method gets an optional user with given email
  Optional<User> findUserByEmail(String email);
}

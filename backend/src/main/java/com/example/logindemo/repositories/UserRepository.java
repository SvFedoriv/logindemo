package com.example.logindemo.repositories;

import com.example.logindemo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
   User findByUsernameAndPassword(String username, String Password);
}
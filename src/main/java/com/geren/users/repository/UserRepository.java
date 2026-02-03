package com.geren.users.repository;

import com.geren.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}

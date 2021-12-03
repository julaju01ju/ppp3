package com.javamentor.qa.platform.repository;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}

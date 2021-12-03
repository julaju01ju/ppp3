package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Integer> {

}

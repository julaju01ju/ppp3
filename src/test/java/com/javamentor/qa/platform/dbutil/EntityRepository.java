package com.javamentor.qa.platform.dbutil;

import com.javamentor.qa.platform.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<Entity,Integer> {
}

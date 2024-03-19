package com.aldoj.menuapi.repositories;

import com.aldoj.menuapi.domain.food.Foods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodsRepository extends JpaRepository<Foods, UUID> {
    List<Foods> findAllByUserId(UUID userId);
}

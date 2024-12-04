package com.bcet.roadmap_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcet.roadmap_service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}

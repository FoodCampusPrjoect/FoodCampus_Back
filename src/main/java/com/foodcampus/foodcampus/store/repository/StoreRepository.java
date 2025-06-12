package com.foodcampus.foodcampus.store.repository;

import com.foodcampus.foodcampus.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findBySelectMainCategory(String selectMainCategory);
    List<Store> findBySelectMainCategoryAndSelectSubCategories(String selectMainCategory, String selectSubCategory);
    @Query("SELECT DISTINCT s FROM Store s JOIN s.menu m WHERE s.storeName LIKE %:query% OR m.menu LIKE %:query%")
    List<Store> findByStoreNameContainingOrMenuContaining(@Param("query") String query);
}


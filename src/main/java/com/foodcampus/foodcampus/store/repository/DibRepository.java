package com.foodcampus.foodcampus.store.repository;

import com.foodcampus.foodcampus.store.entity.Dib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DibRepository extends JpaRepository<Dib, Long> {
    List<Dib> findByUserId(Long userId);
    List<Dib> findByStore_StoreId(Long storeId);
    Optional<Dib> findByUserIdAndStore_StoreId(Long userId, Long storeId);
}


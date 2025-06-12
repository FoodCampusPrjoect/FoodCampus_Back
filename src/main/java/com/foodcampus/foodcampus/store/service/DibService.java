package com.foodcampus.foodcampus.store.service;

import com.foodcampus.foodcampus.store.entity.Dib;
import com.foodcampus.foodcampus.store.repository.DibRepository;
import com.foodcampus.foodcampus.user.entity.User;
import com.foodcampus.foodcampus.store.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DibService {

    @Autowired
    private DibRepository dibRepository;

    public Dib addDib(User user, Store store) {
        Dib dib = new Dib(user, store);
        return dibRepository.save(dib);
    }

    public List<Dib> getDibsByUserId(Long userId) {
        return dibRepository.findByUserId(userId);
    }

    public List<Dib> getDibsByStore_StoreId(Long storeId) {
        return dibRepository.findByStore_StoreId(storeId);
    }

    public void removeDib(Long id) {
        dibRepository.deleteById(id);
    }

    // Add this method to check dib existence by userId and storeId
    public Optional<Dib> checkDib(Long userId, Long storeId) {
        return dibRepository.findByUserIdAndStore_StoreId(userId, storeId);
    }

    public boolean removeDibByUserAndStore(Long userId, Long storeId) {
        Optional<Dib> dib = dibRepository.findByUserIdAndStore_StoreId(userId, storeId);
        if (dib.isPresent()) {
            dibRepository.delete(dib.get());
            return true;
        } else {
            return false;
        }
    }


}

package com.foodcampus.foodcampus.store.controller;

import com.foodcampus.foodcampus.store.entity.Dib;
import com.foodcampus.foodcampus.store.entity.Store;
import com.foodcampus.foodcampus.store.service.DibService;
import com.foodcampus.foodcampus.store.service.StoreService;
import com.foodcampus.foodcampus.user.entity.User;
import com.foodcampus.foodcampus.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dibs")
public class DibController {

    @Autowired
    private DibService dibService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    // Add a new endpoint for checking dib
    @GetMapping
    public ResponseEntity<Dib> checkDib(@RequestParam Long userId, @RequestParam Long storeId) {
        Optional<Dib> dib = dibService.checkDib(userId, storeId);
        return dib.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dib> addDib(@RequestParam Long userId, @RequestParam Long storeId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Store> store = storeService.getStoreById(storeId);

        if (user.isPresent() && store.isPresent()) {
            Dib dib = dibService.addDib(user.get(), store.get());
            return ResponseEntity.ok(dib);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Dib>> getDibsByUserId(@PathVariable Long userId) {
        List<Dib> dibs = dibService.getDibsByUserId(userId);
        return ResponseEntity.ok(dibs);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Dib>> getDibsByStoreId(@PathVariable Long storeId) {
        List<Dib> dibs = dibService.getDibsByStore_StoreId(storeId);
        return ResponseEntity.ok(dibs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDib(@PathVariable Long id) {
        dibService.removeDib(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeDibByUserAndStore(@RequestParam Long userId, @RequestParam Long storeId) {
        boolean removed = dibService.removeDibByUserAndStore(userId, storeId);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

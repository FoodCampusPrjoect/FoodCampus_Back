    package com.foodcampus.foodcampus.store.controller;


    import com.foodcampus.foodcampus.store.dto.StoreDto;
    import com.foodcampus.foodcampus.store.entity.Store;
    import com.foodcampus.foodcampus.store.repository.StoreRepository;
    import com.foodcampus.foodcampus.store.service.StoreService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api")
    public class StoreController {
        private final StoreService storeService;
        private final StoreRepository storeRepository;

        @Autowired
        public StoreController(StoreService storeService, StoreRepository storeRepository) {
            this.storeService = storeService;
            this.storeRepository = storeRepository;
        }

        @GetMapping("/stores")
        public ResponseEntity<List<Store>> getAllStores() {
            List<Store> stores = storeService.getAllStores();
            return ResponseEntity.ok(stores);
        }

        @GetMapping("/stores/{storeId}")
        public ResponseEntity<Store> getStoreById(@PathVariable Long storeId) {
            Optional<Store> store = storeService.getStoreById(storeId);
            return store.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        //0601

        @GetMapping("/stores/category/{category}")
        public ResponseEntity<List<Store>> getStoresByCategory(@PathVariable String category) {
            List<Store> stores = storeService.getStoresByCategory(category);
            return ResponseEntity.ok(stores);
        }

        @GetMapping("/stores/category/{mainCategory}/{subCategory}")
        public ResponseEntity<List<Store>> getStoresByMainAndSubCategory(
                @PathVariable String mainCategory,
                @PathVariable String subCategory) {
            List<Store> stores = storeService.getStoresByMainAndSubCategory(mainCategory, subCategory);
            return ResponseEntity.ok(stores);
        }

        // 가게 등록
        @PostMapping("/register")
        public ResponseEntity<?> registerStore(@RequestBody StoreDto storeDto) {
            try {
                return storeService.registerStore(storeDto);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Store registration failed");
            }
        }

        @GetMapping("/stores/coordinates")
        public ResponseEntity<?> getCoordinates(@RequestParam String address) {
            double[] coordinates = storeService.getCoordinates(address);
            if (coordinates != null) {
                return ResponseEntity.ok(coordinates);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get coordinates from address.");
            }
        }

        @GetMapping("/stores/search")
        public ResponseEntity<List<Store>> searchStores(@RequestParam String query) {
            List<Store> stores = storeRepository.findByStoreNameContainingOrMenuContaining(query);
            return ResponseEntity.ok(stores);
        }

        // 가게 삭제
        @DeleteMapping("/stores/{storeId}")
        public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
            try {
                storeService.deleteStore(storeId);
                return ResponseEntity.ok("Store deleted successfully.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting store: " + e.getMessage());
            }
        }
    }

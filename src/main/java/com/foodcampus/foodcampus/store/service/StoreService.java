package com.foodcampus.foodcampus.store.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcampus.foodcampus.review.entity.Review;
import com.foodcampus.foodcampus.review.service.ReviewService;
import com.foodcampus.foodcampus.store.dto.StoreDto;
import com.foodcampus.foodcampus.store.entity.Dib;
import com.foodcampus.foodcampus.store.entity.Menu;
import com.foodcampus.foodcampus.store.entity.Store;
import com.foodcampus.foodcampus.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final DibService dibService; // DibService 의존성 추가
    private final ReviewService reviewService;
    @Value("${kakao.api}")
    private String KAKAO_API_KEY;

    @Autowired
    public StoreService(StoreRepository storeRepository, DibService dibService, ReviewService reviewService) {
        this.storeRepository = storeRepository;
        this.dibService = dibService; // DibService 의존성 주입
        this.reviewService = reviewService;
    }

    @Transactional
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Transactional
    public Optional<Store> getStoreById(Long store_id) {
        return storeRepository.findById(store_id);
    }

    @Transactional
    public List<Store> getStoresByCategory(String category) {
        return storeRepository.findBySelectMainCategory(category);
    }

    @Transactional
    public List<Store> getStoresByMainAndSubCategory(String mainCategory, String subCategory) {
        return storeRepository.findBySelectMainCategoryAndSelectSubCategories(mainCategory, subCategory);
    }

    // 가게 등록
    @Transactional
    public ResponseEntity<?> registerStore(StoreDto storeDto) {
        try {
            Store store = new Store();

            store.setStoreImage(storeDto.getStoreImage());
            store.setStoreName(storeDto.getStoreName());
            store.setAddress(storeDto.getAddress());
            store.setStoreNumber(storeDto.getStoreNumber());
            store.setOpenTime(storeDto.getOpenTime());
            store.setCloseTime(storeDto.getCloseTime());
            store.setDetailAddress(storeDto.getDetailAddress());
            store.setSelectMainCategory(storeDto.getSelectMainCategory());
            store.setStoreInfo(storeDto.getStoreInfo());
            store.setShortInfo(storeDto.getShortInfo());

            store.setTaste(storeDto.getTaste());
            store.setPrice(storeDto.getPrice());
            store.setKindness(storeDto.getKindness());
            store.setHygiene(storeDto.getHygiene());
            store.setScore(storeDto.getScore());

            store.setUrl(storeDto.getUrl());
            store.setSelectSubCategories(storeDto.getSelectSubCategories());
            store.setClosedDays(storeDto.getClosedDays());

            // 메뉴 리스트를 설정할 때 menu 필드가 올바르게 설정되었는지 확인합니다.
            List<Menu> menuList = storeDto.getMenu();
            for (Menu menu : menuList) {
                if (menu.getMenu() == null || menu.getMenu().isEmpty()) {
                    throw new IllegalArgumentException("Menu name cannot be null or empty");
                }
                menu.setStore(store);
            }
            store.setMenu(menuList);

            double[] coordinates = getCoordinatesFromAddress(storeDto.getAddress());
            store.setLatitude(coordinates[0]);
            store.setLongitude(coordinates[1]);

            store = storeRepository.save(store);

            return ResponseEntity.status(HttpStatus.OK).body("Store registered successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred while registering store: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while registering store.");
        }
    }

    @Transactional
    public double[] getCoordinates(String address) {
        try {
            return getCoordinatesFromAddress(address);
        } catch (IOException e) {
            System.out.println("Error occurred while getting coordinates from address: " + e.getMessage());
            return null;
        }
    }

    private double[] getCoordinatesFromAddress(String address) throws IOException {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode documents = root.path("documents");

        if (documents.isArray() && documents.size() > 0) {
            JsonNode location = documents.get(0).path("address");
            double latitude = location.path("y").asDouble();
            double longitude = location.path("x").asDouble();
            return new double[]{latitude, longitude};
        } else {
            throw new IOException("Failed to convert address to coordinates");
        }
    }

    @Transactional
    public List<Store> searchStores(String query) {
        return storeRepository.findByStoreNameContainingOrMenuContaining(query);
    }

    @Transactional
    public void deleteStore(Long storeId) {
        List<Dib> dibs = dibService.getDibsByStore_StoreId(storeId);
        for (Dib dib : dibs) {
            dibService.removeDib(dib.getId());
        }

        List<Review> reviews = reviewService.getReviewsByStoreId(storeId);
        for (Review review : reviews) {
            reviewService.deleteReviewById(review.getId());  // review.getId() 사용
        }
        storeRepository.deleteById(storeId);
    }
}



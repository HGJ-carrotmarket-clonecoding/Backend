package com.marketkurly.service;

import com.marketkurly.dto.requsetDto.ProductRequestDto;
import com.marketkurly.exception.EmptyException;
import com.marketkurly.model.Product;
import com.marketkurly.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final String CLIENT_ID = "fm4NYvCHEDw8lwU9K78x";
    private final String CLIENT_SECRET = "5oMRZB9nq8";
    private final String OpenApiNaverShopUrl = "https://openapi.naver.com/v1/search/shop.json?display=50&query=";
    int display = 15; //1차 프레임에서는 20으로 고정
    int start = 0; // 1차 프레임에서는 0으로 고정

    public Page<Product> getProducts(String category1, String category2, String keyword, int page) {
        page -= 1;
        Page<Product> products = productRepository.findByNameLike("%" + keyword + "%", PageRequest.of(page, display));
        if (products.isEmpty()) {
            String apiBody = getProductsFromApi(keyword, page);
            List<ProductRequestDto> productApi = fromJSONtoItems(apiBody);
//            products = new PageImpl<>(productApi, PageRequest.of(page, display), display);

            List<Product> productList = new ArrayList<>();
            for (ProductRequestDto productDto : productApi) {
                productList.add(new Product(productDto));
            }
            products = new PageImpl<>(productList.subList(0, 15), PageRequest.of(page, display), display);
            productRepository.saveAll(productList);
        }
        return products;


    }
    public String getProductsFromApi(String keyword, int page) {
        // 네이버 쇼핑 API 호출에 필요한 Header, Body 정리
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", CLIENT_ID);
        headers.add("X-Naver-Client-Secret", CLIENT_SECRET);
        String body = "";

        start = page == 0 ? 1 : page * display + 1;
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(OpenApiNaverShopUrl + keyword + "&start=" + start, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        System.out.println("Response Status : " + httpStatus.value());
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }


    public List<ProductRequestDto> fromJSONtoItems(String result) {
        JSONObject json = new JSONObject(result);
        JSONArray items = json.getJSONArray("items");
        List<ProductRequestDto> productDtos = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            ProductRequestDto productDto = new ProductRequestDto(itemJson);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EmptyException("해당 상품이 존재하지 않습니다."));
    }
}

package com.marketkurly.service;

import com.marketkurly.dto.requsetDto.CartRequestDto;
import com.marketkurly.dto.responseDto.CartResponseDto;
import com.marketkurly.dto.responseDto.ProductResponseDto;
import com.marketkurly.exception.NoItemException;
import com.marketkurly.model.Cart;
import com.marketkurly.model.Product;
import com.marketkurly.model.User;
import com.marketkurly.repository.CartRepository;
import com.marketkurly.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Long addProduct(CartRequestDto requestDto, User user) {
        Product product = loadProduct(requestDto.getProductId());
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), requestDto.getProductId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.addNewProduct(product, requestDto.getAmount(), user);
        } else {
            cart.addProductAmount(requestDto.getAmount());
        }
        cartRepository.save(cart);
        return cartRepository.countByUser(user);
    }

    private Product loadProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NoItemException("해당 상품이 존재하지 않습니다.")
        );
    }

    public CartResponseDto getCart(User user) {
        List<ProductResponseDto> responseDtoList = cartRepository.findAllByUserOrderByAddedAtDesc(user).stream().map(ProductResponseDto::new).collect(Collectors.toCollection(ArrayList::new));
        Long itemCount = cartRepository.countByUser(user);
        return new CartResponseDto(responseDtoList, itemCount);
    }

    @Transactional
    public void deleteCart(User user, Long productId) {
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId).orElseThrow(
                () -> new NoItemException("장바구니에서 해당 상품을 찾을 수 없습니다.")
        );
        cart.deleteCart();
        cartRepository.delete(cart);
    }
}

package ru.gb.service;

import ru.gb.domain.Perfume;

import java.util.List;

public interface CartService {

    List<Perfume> getPerfumesInCart();

    void addPerfumeToCart(Long perfumeId);

    void removePerfumeFromCart(Long perfumeId);
}

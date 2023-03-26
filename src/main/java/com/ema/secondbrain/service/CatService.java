package com.ema.secondbrain.service;

import com.ema.secondbrain.entity.Cat;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CatService {

    ResponseEntity<List<Cat>> getAllCats();

    ResponseEntity<Cat> getCatById(Long id);

    ResponseEntity<Cat> createCat(Cat cat);
}

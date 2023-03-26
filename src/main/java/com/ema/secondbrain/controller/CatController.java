package com.ema.secondbrain.controller;

import com.ema.secondbrain.entity.Cat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface CatController {

    @GetMapping("")
    ResponseEntity<List<Cat>> getAllCats();

    @GetMapping("/{id}")
    ResponseEntity<Cat> getCatById(@PathVariable("id") Long id);

    @PostMapping("")
    ResponseEntity<Cat> createCat(Cat cat);
}

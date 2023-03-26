package com.ema.secondbrain.service;

import com.ema.secondbrain.entity.Cat;
import com.ema.secondbrain.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;

    @Autowired
    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public ResponseEntity<List<Cat>> getAllCats() {
        List<Cat> cats = catRepository.findAll();

        if (cats.isEmpty())
            return  ResponseEntity.noContent().build();

        return ResponseEntity.ok(cats);
    }

    @Override
    public ResponseEntity<Cat> getCatById(Long id) {
        Cat cat = catRepository.findById(id).orElse(null);

        if (cat == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(cat);
    }

    @Override
    public ResponseEntity<Cat> createCat(Cat cat) {
        Cat newCat = catRepository.save(cat);

        return ResponseEntity.status(201).body(newCat);
    }


}

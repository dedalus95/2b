package com.ema.secondbrain.controller;

import com.ema.secondbrain.constants.Endpoints;
import com.ema.secondbrain.entity.Cat;
import com.ema.secondbrain.service.CatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static com.ema.secondbrain.constants.Endpoints.API;
import static com.ema.secondbrain.constants.Endpoints.CATS;

@RestController
@RequestMapping(API + CATS)
public class CatControllerImpl implements CatController {

    private final CatServiceImpl catService;

    @Autowired
    public CatControllerImpl(CatServiceImpl catService) {
        this.catService = catService;
    }

        @Override
        public ResponseEntity<List<Cat>> getAllCats() {
            return catService.getAllCats();
        }

        @Override
        public ResponseEntity<Cat> getCatById(Long id) {
            return catService.getCatById(id);
        }

        @Override
        public ResponseEntity<Cat> createCat(Cat cat) {
            return catService.createCat(cat);
        }
}

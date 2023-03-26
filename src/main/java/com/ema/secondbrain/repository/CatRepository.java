package com.ema.secondbrain.repository;

import com.ema.secondbrain.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {}

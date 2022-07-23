package io.gatling.demostore.models;

import java.util.List;

import io.gatling.demostore.models.data.Product;
import io.gatling.demostore.models.data.VideoGame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoGameRepository extends JpaRepository<VideoGame, Integer> {

    VideoGame findById(int id);

    Page<VideoGame> findAll(Pageable pageable);

}

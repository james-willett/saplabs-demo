package io.videogamedb.models;

import io.videogamedb.models.data.VideoGame;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public interface VideoGameRepository extends JpaRepository<VideoGame, Integer> {

    VideoGame findById(int id);

    List<VideoGame> findAll();

}

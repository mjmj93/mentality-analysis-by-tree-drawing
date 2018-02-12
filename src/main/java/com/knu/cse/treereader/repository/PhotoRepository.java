package com.knu.cse.treereader.repository;

import com.knu.cse.treereader.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//https://docs.spring.io/spring-data/jpa/docs/1.9.6.RELEASE/reference/html/#jpa.query-methods.query-creation
//http://arahansa.github.io/docs_spring/jpa.html
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer>{
}
//<model name , primary key type>
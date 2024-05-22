package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.blogapi.model.Category;
import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findByCategory(String category);
    @Query("""
        SELECT c FROM Category c Where category in ?1 
            """)
    List<Category> findAllByCategory(List<String> categories);

    

    
}

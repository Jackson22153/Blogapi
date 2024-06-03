package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.model.Category;
import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findByCategory(String category);
    @Query("""
        SELECT c FROM Category c Where category in ?1 
            """)
    List<Category> findAllByCategory(List<String> categories);

    @Modifying
    @Transactional
    @Procedure("addCategory")
    void addCategory(String category);

    
}

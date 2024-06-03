package com.phucx.blogapi.service.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.model.Category;
import com.phucx.blogapi.repository.CategoryRepository;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByID(Integer categoryID) {
        return categoryRepository.findById(categoryID)
            .orElseThrow(()-> new RuntimeException("Category " + categoryID + " does not found"));
    }

    @Override
    public List<Category> getCategoriesByIDs(List<Integer> categoryIDs) {
        return categoryRepository.findAllById(categoryIDs);
    }

    @Override
    public Category getCategory(String category) {
        var categories = categoryRepository.findByCategory(category);
        if(categories.size()>0) return categories.get(0);
        throw new RuntimeException("Category " + category + " does not found");
    }

    @Override
    public List<Category> getCategories(List<String> categories) {
        log.info("categories: {}", categories);
        return categoryRepository.findAllByCategory(categories);
        
    }

    @Override
    @Modifying
    @Transactional
    public Boolean addCategory(Category category) {
        log.info("addCategory({})", category);
        try {
            List<Category> categories = categoryRepository.findByCategory(category.getCategory());
            if(categories!=null && categories.size()>0) 
                throw new EntityExistsException("Category " + category.getCategory() + " is existed");
            categoryRepository.addCategory(category.getCategory());
            return true;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Modifying
    @Transactional
    public Boolean deleteCategory(Integer categoryID) {
        log.info("deleteCategory(categoryID={})", categoryID);
        Category category = categoryRepository.findById(categoryID)
            .orElseThrow(()-> new RuntimeException("Category " + categoryID + " does not found"));
        categoryRepository.deleteById(category.getId());
        return true;
    }
    
}

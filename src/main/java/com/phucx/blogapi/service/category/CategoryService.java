package com.phucx.blogapi.service.category;

import java.util.List;

import com.phucx.blogapi.model.Category;

public interface CategoryService {
    public List<Category> getAllCategories();
    public Category getCategoryByID(Integer categoryID);
    public List<Category> getCategoriesByIDs(List<Integer> categoryIDs);
    public Category getCategory(String category);
    public List<Category> getCategories(List<String> categories);
    public Boolean addCategory(Category category);
    public Boolean deleteCategory(Integer categoryID);

} 

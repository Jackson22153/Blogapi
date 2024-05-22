package com.phucx.blogapi.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.blogapi.constant.PostStatus;
import com.phucx.blogapi.constant.WebConstant;
import com.phucx.blogapi.model.Category;
import com.phucx.blogapi.model.PostInfo;
import com.phucx.blogapi.service.category.CategoryService;
import com.phucx.blogapi.service.post.PostService;
import com.phucx.blogapi.service.uploadFile.UploadFileService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UploadFileService uploadFileService;
    
    @Operation(summary = "Get posts for all users")
    @GetMapping
    public ResponseEntity<List<PostInfo>> getPosts(
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        List<PostInfo> posts = postService.getPostsByStatus(
            PostStatus.Successful, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Search posts for all users")
    @GetMapping("/search")
    public ResponseEntity<List<PostInfo>> searchPosts(
        @RequestParam(name = "l") String letter,
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        List<PostInfo> posts = postService.searchPostsByStatus(
            letter, PostStatus.Successful, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get posts based on category for all users")
    @GetMapping("/search/{categoryName}")
    public ResponseEntity<List<PostInfo>> searchPostsByCategory(
        @RequestParam(name = "l") String letter,
        @PathVariable String categoryName,
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        List<PostInfo> posts = postService.searchPostsByCategoryAndStatus(
            letter, categoryName, PostStatus.Successful,
            pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get posts based on category for all users")
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<PostInfo>> getPostsByCategory(
        @PathVariable(name = "categoryName") String category,
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        List<PostInfo> posts = postService.getPostsByCategoryAndStatus(
            category, PostStatus.Successful, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get post detail for all users")
    @GetMapping("/detail/{postID}")
    public ResponseEntity<PostInfo> getPostDetail(@PathVariable Integer postID) throws NameNotFoundException{
        PostInfo post = postService.getPostByStatus(postID, PostStatus.Successful);
        return ResponseEntity.ok().body(post);
    }

    @Operation(summary = "Get categories for all users")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categories);
    }

    @Operation(summary = "Get specific image for all users")
    @GetMapping("/image/{imageFilename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageFilename) throws IOException{
        byte[] imageBytes = uploadFileService.getImage(imageFilename);
        String mimeType = uploadFileService.getMimeType(imageFilename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(imageBytes);
    }

    @Operation(summary = "Upload an image for all users")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file){
        return ResponseEntity.ok().body(uploadFileService.uploadImage(file));
    }
}

package com.phucx.blogapi.controller;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.blogapi.constant.PostStatus;
import com.phucx.blogapi.constant.WebConstant;
import com.phucx.blogapi.model.Category;
import com.phucx.blogapi.model.PostInfo;
import com.phucx.blogapi.model.ResponseFormat;
import com.phucx.blogapi.model.Role;
import com.phucx.blogapi.model.UserDetail;
import com.phucx.blogapi.service.category.CategoryService;
import com.phucx.blogapi.service.post.PostService;
import com.phucx.blogapi.service.role.RoleService;
import com.phucx.blogapi.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RoleService roleService;
    
    @Operation(summary = "Get all users's posts", tags = {"post", "admin"})
    @GetMapping("/posts")
    public ResponseEntity<Page<PostInfo>> getPosts(@RequestParam(name = "page", required = false) Integer pageNumber){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<PostInfo> posts = postService.getAllPosts(pageNumber, WebConstant.PAGE_SIZE);    
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get all users's post detail", tags = {"get", "admin"})
    @GetMapping("/posts/{postID}")
    public ResponseEntity<PostInfo> getPostDetail(@PathVariable Integer postID) throws NameNotFoundException{
        PostInfo post = postService.getPostInfo(postID);
        return ResponseEntity.ok().body(post);
    }

    @Operation(summary = "Search all users's posts", tags = {"get", "admin"})
    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostInfo>> searchPosts(
        @RequestParam(name = "l") String letter,
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        Page<PostInfo> posts = postService.searchAllPosts(letter, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }
    
    @Operation(summary = "Update all users's post", tags = {"post", "admin"})
    @PostMapping("/posts")
    public ResponseEntity<ResponseFormat> updatePost(@RequestBody PostInfo postDetail){
        Boolean status = postService.updatePost(postDetail);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Add a new post for admin", tags = {"put", "admin"})
    @PutMapping("/posts")
    public ResponseEntity<ResponseFormat> createPost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPost(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Delete a post for admin", tags = {"delete", "admin"})
    @DeleteMapping("/posts")
    public ResponseEntity<ResponseFormat> deletePost(@RequestParam(name = "postID") Integer postID, Authentication authentication) 
        throws NameNotFoundException{
        Boolean status = postService.deletePost(postID);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Update a specific category", tags = {"post", "admin"})
    @PostMapping("/categories")
    public ResponseEntity<ResponseFormat> updateCategory(@RequestBody Category category){
        Boolean status = categoryService.addCategory(category);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Add new category", tags = {"put", "admin"})
    @PutMapping("/categories")
    public ResponseEntity<ResponseFormat> addCategory(@RequestBody Category category){
        Boolean status = categoryService.addCategory(category);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Delete a specific category", tags = {"delete", "admin"})
    @DeleteMapping("/categories")
    public ResponseEntity<ResponseFormat> deleteCategory(@RequestParam(name = "categoryID") Integer categoryID){
        Boolean status = categoryService.deleteCategory(categoryID);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }


    @Operation(summary = "Get all roles", tags = {"get", "admin"})
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(){
        return ResponseEntity.ok().body(roleService.getAllRoles());
    }

    @Operation(summary = "Create a new user", tags = {"put", "admin"})
    @PutMapping("/user")
    public ResponseEntity<ResponseFormat> addUser(@RequestBody UserDetail userDetail){
        Boolean status = this.userService.addNewUser(userDetail);
        return ResponseEntity.ok().body(new ResponseFormat(status)); 
    }

    // get posts by status
    @Operation(summary = "Get all pending posts", tags = {"get", "admin"})
    @GetMapping("/posts/pending")
    public ResponseEntity<Page<PostInfo>> getPendingPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<PostInfo> posts = postService.getPostsByStatus(
            PostStatus.Pending, pageNumber, WebConstant.PAGE_SIZE);    
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Get all successful posts", tags = {"get", "admin"})
    @GetMapping("/posts/successful")
    public ResponseEntity<Page<PostInfo>> getSuccessfulPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<PostInfo> posts = postService.getPostsByStatus(
            PostStatus.Successful, pageNumber, WebConstant.PAGE_SIZE);    
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Get all canceled posts", tags = {"get", "admin"})
    @GetMapping("/posts/canceled")
    public ResponseEntity<Page<PostInfo>> getCanceledPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<PostInfo> posts = postService.getPostsByStatus(
            PostStatus.Canceled, pageNumber, WebConstant.PAGE_SIZE);    
        return ResponseEntity.ok().body(posts);
    }
    // validate post
    @Operation(summary = "Confirm user's pending post", tags = {"post", "admin"})
    @PostMapping("/posts/confirm")
    public ResponseEntity<ResponseFormat> confirmPost(@RequestBody PostInfo postInfo) throws NameNotFoundException{
        Boolean status = postService.confirmPost(postInfo);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Cancel user's pending post", tags = {"post", "admin"})
    @PostMapping("/posts/cancel")
    public ResponseEntity<ResponseFormat> cancelPost(@RequestBody PostInfo postInfo) throws NameNotFoundException{
        Boolean status = postService.cancelPost(postInfo);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

}

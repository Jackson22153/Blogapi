package com.phucx.blogapi.controller;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.phucx.blogapi.constant.WebConstant;
import com.phucx.blogapi.model.BookmarkPostInfo;
import com.phucx.blogapi.model.FileFormat;
import com.phucx.blogapi.model.PostInfo;
import com.phucx.blogapi.model.ResponseFormat;
import com.phucx.blogapi.model.UserRoleInfoDTO;
import com.phucx.blogapi.service.post.PostService;
import com.phucx.blogapi.service.uploadFile.UploadFileService;
import com.phucx.blogapi.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/blogOwner")
public class BlogOwnerController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private UploadFileService uploadFileService;
    
    // user
    @Operation(summary = "Get user's information")
    @GetMapping("/user")
    public ResponseEntity<UserRoleInfoDTO> getUser(Authentication authentication){
        String username = authentication.getName();
        UserRoleInfoDTO userRoleInfo = userService.getUserRoles(username);
        return ResponseEntity.ok().body(userRoleInfo);
    }


    // bookmarks
    @Operation(summary = "Check whether user's post is in bookmarks or not")
    @GetMapping("/bookmarks/isInBookmark")
    public ResponseEntity<ResponseFormat> checkPostInBookmarks(
        @RequestParam("postID") Integer postID, Authentication authentication){
        String username = authentication.getName();
        Boolean isInBookmark = postService.isInBookmarks(postID, username);
        return ResponseEntity.ok().body(new ResponseFormat(isInBookmark));
    }


    @Operation(summary = "Get posts in bookmarks of a user")
    @GetMapping("/bookmarks/posts")
    public ResponseEntity<List<BookmarkPostInfo>> getBookmarksPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        List<BookmarkPostInfo> posts = postService.getPostsInBookmarks(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Add a post to bookmarks")
    @PostMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> addPostToBookmarks(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPostToBookmarks(postInfo.getId(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Remove a post in bookmarks")
    @DeleteMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> removePostFromBookmarks(@RequestParam(name = "postID") Integer postID, Authentication authentication){
        Boolean status = postService.removePostFromBookmarks(postID, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }


    // post
    @Operation(summary = "Get all posts of a user")
    @GetMapping("/posts")
    public ResponseEntity<List<PostInfo>> getPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.getPostsByUser(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Get a specific user's post detail")
    @GetMapping("/posts/{postID}")
    public ResponseEntity<PostInfo> getPostDetail(
        @PathVariable Integer postID,
        Authentication authentication
    ){
        String username = authentication.getName();
        PostInfo post = postService.getPostByUser(username, postID);
        return ResponseEntity.ok().body(post);
    }

    @Operation(summary = "Search a specific user's posts")
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostInfo>> searchPosts(
        @RequestParam(name = "l") String letter,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.searchPostsOfUser(
            letter, username, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get posts based on category of a user")
    @GetMapping("/posts/category/{category}")
    public ResponseEntity<List<PostInfo>> getPostsByCategory(
        @PathVariable String category,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.getPostsOfUserByCategory(
            username, category, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(
        summary = "Adding a new post of a user",
        description = "Adding a post for that user")
    @PutMapping("/posts")
    public ResponseEntity<ResponseFormat> createPost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPost(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Update a specific user's posts")
    @PostMapping("/posts")
    public ResponseEntity<ResponseFormat> updatePost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.updatePostOfUser(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Delete a specific user's posts")
    @DeleteMapping("/posts")
    public ResponseEntity<ResponseFormat> deletePost(@RequestParam(name = "postID") Integer postID, Authentication authentication) 
        throws NameNotFoundException{
        Boolean status = postService.deletePost(postID, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Upload an image")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileFormat> uploadImage(@RequestBody MultipartFile file){
        return ResponseEntity.ok().body(uploadFileService.uploadImage(file));
    }

}

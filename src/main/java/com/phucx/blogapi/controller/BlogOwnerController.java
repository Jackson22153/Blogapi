package com.phucx.blogapi.controller;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.phucx.blogapi.model.UserRoles;
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
    @Operation(summary = "Get user's information", tags = {"get", "user"})
    @GetMapping("/user")
    public ResponseEntity<UserRoles> getUser(Authentication authentication){
        String username = authentication.getName();
        UserRoles useRoles = userService.getUserRoles(username);
        return ResponseEntity.ok().body(useRoles);
    }


    // bookmarks
    @Operation(summary = "Check whether user's post is in bookmarks or not", tags = {"get", "user"})
    @GetMapping("/bookmarks/isInBookmark")
    public ResponseEntity<ResponseFormat> checkPostInBookmarks(
        @RequestParam("postID") Integer postID, Authentication authentication){
        String username = authentication.getName();
        Boolean isInBookmark = postService.isInBookmarks(postID, username);
        return ResponseEntity.ok().body(new ResponseFormat(isInBookmark));
    }


    @Operation(summary = "Get posts in bookmarks of a user", tags = {"get", "user"})
    @GetMapping("/bookmarks/posts")
    public ResponseEntity<Page<BookmarkPostInfo>> getBookmarksPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        Page<BookmarkPostInfo> posts = postService.getPostsInBookmarks(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Add a post to bookmarks", tags = {"post", "user"})
    @PostMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> addPostToBookmarks(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPostToBookmarks(postInfo.getId(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Remove a post in bookmarks", tags = {"delete", "user"})
    @DeleteMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> removePostFromBookmarks(@RequestParam(name = "postID") Integer postID, Authentication authentication){
        Boolean status = postService.removePostFromBookmarks(postID, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }


    // post
    @Operation(summary = "Get all posts of a user", tags = {"get", "user"})
    @GetMapping("/posts")
    public ResponseEntity<Page<PostInfo>> getPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        Page<PostInfo> posts = postService.getPostsByUser(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }
    @Operation(summary = "Get a specific user's post detail", tags = {"get", "user"})
    @GetMapping("/posts/{postID}")
    public ResponseEntity<PostInfo> getPostDetail(
        @PathVariable Integer postID,
        Authentication authentication
    ){
        String username = authentication.getName();
        PostInfo post = postService.getPostByUser(username, postID);
        return ResponseEntity.ok().body(post);
    }

    @Operation(summary = "Search a specific user's posts", tags = {"get", "user"})
    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostInfo>> searchPosts(
        @RequestParam(name = "l") String letter,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        Page<PostInfo> posts = postService.searchPostsOfUser(
            letter, username, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "Get posts based on category of a user", tags = {"get", "user"})
    @GetMapping("/posts/category/{category}")
    public ResponseEntity<Page<PostInfo>> getPostsByCategory(
        @PathVariable String category,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        Page<PostInfo> posts = postService.getPostsOfUserByCategory(
            username, category, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @Operation(
        summary = "Adding a new post of a user",
        description = "Adding a post for that user",
        tags = {"post", "user"})
    @PutMapping("/posts")
    public ResponseEntity<ResponseFormat> createPost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPost(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Update a specific user's posts", tags = {"post", "user"})
    @PostMapping("/posts")
    public ResponseEntity<ResponseFormat> updatePost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.updatePostOfUser(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Delete a specific user's posts", tags = {"delete", "user"})
    @DeleteMapping("/posts")
    public ResponseEntity<ResponseFormat> deletePost(@RequestParam(name = "postID") Integer postID, Authentication authentication) 
        throws NameNotFoundException{
        Boolean status = postService.deletePost(postID, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Upload an image", tags = {"post", "user"})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileFormat> uploadImage(@RequestBody MultipartFile file){
        return ResponseEntity.ok().body(uploadFileService.uploadImage(file));
    }

}

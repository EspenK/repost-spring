package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.CreateComment;
import me.kverna.spring.repost.data.EditPost;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.CommentService;
import me.kverna.spring.repost.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "posts")
public class PostController {

    private PostService service;
    private CommentService commentService;

    public PostController(PostService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @Operation(
            summary = "Get Post", description = "Get a specific post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping(value = "/{postId}", produces = {"application/json"})
    public Post getPost(@PathVariable int postId) {
        return service.getPost(postId);
    }

    @Operation(
            summary = "Edit Post", description = "Edit a post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @PatchMapping(value = "/{postId}", consumes = {"application/patch+json"}, produces = {"application/json"})
    public Post editPost(@PathVariable int postId, @RequestBody EditPost editPost, @CurrentUser User user) {
        return service.editPost(editPost, service.getPost(postId), user);
    }

    @Operation(
            summary = "Delete Post", description = "Delete a specific post. " +
            "Only the author of the post or the owner of the parent resub can delete the post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")

    )
    @DeleteMapping("/")
    public void deletePost(@PathVariable int postId, @CurrentUser User user) {
        service.deletePost(service.getPost(postId), user);
    }

    @Operation(
            summary = "Create Comment In Post", description = "Create a comment in a post.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{postId}/comments")
    public Comment createComment(@PathVariable int postId, @RequestBody CreateComment createComment, @CurrentUser User user) {
        Post post = service.getPost(postId);
        return commentService.createComment(createComment, post, post.getParentResub(), user);
    }

    @Operation(
            summary = "Get Comments In Post", description = "Get all comments in post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping(value = "/{postId}/comments", produces = {"application/json"})
    public List<Comment> getAllCommentsInPost(@PathVariable int postId) {
        return commentService.getAllCommentsByPost(service.getPost(postId));
    }
}

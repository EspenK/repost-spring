package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.CreateComment;
import me.kverna.spring.repost.data.EditPost;
import me.kverna.spring.repost.data.ErrorResponse;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.AuthorizeUser;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.CommentService;
import me.kverna.spring.repost.service.PostService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@Validated
@Tag(name = "posts")
public class PostController {

    private final PostService service;
    private final CommentService commentService;

    public PostController(PostService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @Operation(
            summary = "Get Post", description = "Get a specific post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @PatchMapping(value = "/{postId}", consumes = {"application/patch+json"}, produces = {"application/json"})
    public Post editPost(@PathVariable int postId, @RequestBody EditPost editPost, @CurrentUser User user) {
        return service.editPost(editPost, service.getPost(postId), user);
    }

    @Operation(
            summary = "Delete Post", description = "Delete a specific post. " +
            "Only the author of the post or the owner of the parent resub can delete the post.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")

    )
    @AuthorizeUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{postId}")
    public void deletePost(@PathVariable int postId, @CurrentUser User user) {
        service.deletePost(service.getPost(postId), user);
    }

    @Operation(
            summary = "Vote Post", description = "Vote on a post in a resub.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @PatchMapping(value = "/{postId}/vote/{vote}", produces = {"application/json"})
    public Post votePost(@PathVariable int postId, @PathVariable @Range(min = -1, max = 1) int vote,
            @CurrentUser User user) {
        return service.votePost(service.getPost(postId), user, vote);
    }

    @Operation(
            summary = "Create Comment In Post", description = "Create a comment in a post.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser
    @PostMapping(value = "/{postId}/comments")
    public Comment createComment(@PathVariable int postId, @RequestBody CreateComment createComment, @CurrentUser User user) {
        Post post = service.getPost(postId);
        return commentService.createComment(createComment, post, post.getParentResub(), user);
    }

    @Operation(
            summary = "Get Comments In Post", description = "Get all comments in post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{postId}/comments", produces = {"application/json"})
    public List<Comment> getAllCommentsInPost(@PathVariable int postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return commentService.getAllCommentsByPost(service.getPost(postId), page, size);
    }
}

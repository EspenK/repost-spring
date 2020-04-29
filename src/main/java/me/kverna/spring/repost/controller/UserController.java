package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.CreateUser;
import me.kverna.spring.repost.data.EditUser;
import me.kverna.spring.repost.data.ErrorResponse;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.AuthorizeUser;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.CommentService;
import me.kverna.spring.repost.service.PostService;
import me.kverna.spring.repost.service.ResubService;
import me.kverna.spring.repost.service.UserService;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserController {

    private final UserService service;
    private final ResubService resubService;
    private final PostService postService;
    private final CommentService commentService;

    public UserController(UserService service, ResubService resubService, PostService postService,
            CommentService commentService) {
        this.service = service;
        this.resubService = resubService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @Operation(
            summary = "Create User", description = "Create a new user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Response")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public User createUser(@RequestBody CreateUser createUser) {
        return service.createUser(createUser);
    }

    @Operation(
            summary = "Get Current User", description = "Get the authorized user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @GetMapping(value = "/me", produces = {"application/json"})
    public User getCurrentUser(@CurrentUser User currentUser) {
        return currentUser;
    }

    @Operation(
            summary = "Edit Current User", description = "Edit the currently authorized user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @PatchMapping(value = "/me", consumes = {"application/patch+json"}, produces = {"application/json"})
    public User editCurrentUser(@RequestBody EditUser editUser, @CurrentUser User currentUser) {
        return service.editUser(editUser, currentUser);
    }

    @Operation(
            summary = "Delete Current User", description = "Delete the currently authorized user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/me", produces = {"application/json"})
    public void deleteCurrentUser(@CurrentUser User currentUser) {
        service.deleteUser(currentUser);
    }

    @Operation(
            summary = "Get User", description = "Get a specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{username}", produces = {"application/json"})
    public User getUser(@PathVariable String username) {
        return service.getUser(username);
    }

    @Operation(
            summary = "Get Resubs Owned By User", description = "Get all resubs owned by specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{username}/resubs", produces = {"application/json"})
    public List<Resub> getResubsByUser(@PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "100") int pageSize) {
        User owner = service.getUser(username);
        return resubService.getAllResubsByOwner(owner, page, pageSize);
    }

    @Operation(
            summary = "Get Posts Owned By User", description = "Get all posts owned by specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{username}/posts", produces = {"application/json"})
    public List<Post> getPostsByUser(@PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "100") int pageSize) {
        User author = service.getUser(username);
        return postService.getAllPostsByAuthor(author, page, pageSize);
    }

    @Operation(
            summary = "Get Comments By User", description = "Get all comments owned by specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{username}/comments", produces = {"application/json"})
    public List<Comment> getCommentsByUser(@PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "100") int pageSize) {
        User author = service.getUser(username);
        return commentService.getAllCommentsByAuthor(author, page, pageSize);
    }
}

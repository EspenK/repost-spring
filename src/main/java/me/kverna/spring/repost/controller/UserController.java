package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kverna.spring.repost.data.CreateUser;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.service.PostService;
import me.kverna.spring.repost.service.ResubService;
import me.kverna.spring.repost.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserController {

    private UserService service;
    private ResubService resubService;
    private PostService postService;

    public UserController(UserService service, ResubService resubService, PostService postService) {
        this.service = service;
        this.resubService = resubService;
        this.postService = postService;
    }

    @Operation(summary = "Create User", description = "Create a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response")
    })
    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public User createUser(@RequestBody CreateUser createUser) {
        return service.createUser(createUser);
    }

    @Operation(summary = "Get User", description = "Get a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{username}", produces = {"application/json"})
    @PreAuthorize("hasRole('USER')")
    public User getUser(@PathVariable String username) {
        return service.getUser(username);
    }

    @Operation(summary = "Get Resubs Owned By User", description = "Get all resubs owned by specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{username}/resubs", produces = {"application/json"})
    public List<Resub> getResubsByUser(@PathVariable String username) {
        User owner = service.getUser(username);
        return resubService.getAllResubsByOwner(owner);
    }

    @Operation(summary = "Get Posts Owned By User", description = "Get all posts owned by specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{username}/posts", produces = {"application/json"})
    public List<Post> getPostsByUser(@PathVariable String username) {
        User author = service.getUser(username);
        return postService.getAllPostsByAuthor(author);
    }
}

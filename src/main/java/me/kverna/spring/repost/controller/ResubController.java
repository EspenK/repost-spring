package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kverna.spring.repost.data.CreatePost;
import me.kverna.spring.repost.data.CreateResub;
import me.kverna.spring.repost.data.EditResub;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.PostService;
import me.kverna.spring.repost.service.ResubService;
import me.kverna.spring.repost.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resubs")
@Tag(name = "resubs")
public class ResubController {

    private ResubService service;
    private PostService postService;
    private UserService userService;

    public ResubController(ResubService service, PostService postService, UserService userService) {
        this.service = service;
        this.postService = postService;
        this.userService = userService;
    }

    @Operation(summary = "Get Resubs", description = "Get all resubs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response")
    })
    @GetMapping(value = "/", produces = {"application/json"})
    public List<Resub> getAllResubs() {
        return service.getAllResubs();
    }

    @Operation(summary = "Get Resub", description = "Get a specific resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{resub}", produces = {"application/json"})
    public Resub getResub(@PathVariable String resub) {
        return service.getResub(resub);
    }

    @Operation(summary = "Create Resub", description = "Create a new resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public Resub createResub(@RequestBody CreateResub createResub) {
        User owner = userService.getUser("aa");
        return service.createResub(createResub, owner);
    }

    @Operation(summary = "Delete Resub", description = "Delete a resub." +
            "\nOnly the owner of a resub can delete the resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping(value = "/{resub}")
    public void deleteResub(@PathVariable String resub, @CurrentUser User user) {
        service.deleteResub(service.getResub(resub), user);
    }

    @Operation(summary = "Edit Resub", description = "Edit a resub." +
            "\nOnly the owner of a resub can edit the resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PatchMapping(value = "/{resub}", consumes = {"application/patch+json"}, produces = {"application/json"})
    public Resub editResub(@RequestBody EditResub editResub, @PathVariable String resub, @CurrentUser User user) {
        return service.editResub(editResub, service.getResub(resub), user);
    }

    @Operation(summary = "Create Post In Resub", description = "Create new post in a resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping(value = "/{resub}/posts", produces = {"application/json"}, consumes = {"application/json"})
    public Post createPostInResub(@RequestBody CreatePost createPost, @PathVariable String resub) {
        User author = userService.getUser("aa");
        return postService.createPost(createPost, service.getResub(resub), author);
    }

    @Operation(summary = "Get Posts In Resub", description = "Get all posts in a resub.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{resub}/posts", produces = {"application/json"})
    public List<Post> getPostsInResub(@PathVariable String resub) {
        return postService.getAllPostsByParentResub(service.getResub(resub));
    }
}

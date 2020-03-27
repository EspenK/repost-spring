package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kverna.spring.repost.data.CreatePost;
import me.kverna.spring.repost.data.CreateResub;
import me.kverna.spring.repost.data.EditResub;
import me.kverna.spring.repost.data.ErrorResponse;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.AuthorizeUser;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.PostService;
import me.kverna.spring.repost.service.ResubService;
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
@RequestMapping("/api/resubs")
@Tag(name = "resubs")
public class ResubController {

    private ResubService service;
    private PostService postService;

    public ResubController(ResubService service, PostService postService) {
        this.service = service;
        this.postService = postService;
    }

    @Operation(
            summary = "Get Resubs", description = "Get all resubs.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response")
            }
    )
    @GetMapping(value = "/", produces = {"application/json"})
    public List<Resub> getAllResubs() {
        return service.getAllResubs();
    }

    @Operation(
            summary = "Get Resub", description = "Get a specific resub.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{resub}", produces = {"application/json"})
    public Resub getResub(@PathVariable String resub) {
        return service.getResub(resub);
    }

    @Operation(
            summary = "Create Resub", description = "Create a new resub.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser
    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public Resub createResub(@RequestBody CreateResub createResub, @CurrentUser User user) {
        return service.createResub(createResub, user);
    }

    @Operation(
            summary = "Delete Resub", description = "Delete a resub." +
            "Only the owner of a resub can delete the resub.",
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
    @DeleteMapping(value = "/{resub}")
    public void deleteResub(@PathVariable String resub, @CurrentUser User user) {
        service.deleteResub(service.getResub(resub), user);
    }

    @Operation(
            summary = "Edit Resub", description = "Edit a resub." +
            "Only the owner of a resub can edit the resub.",
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
    @PatchMapping(value = "/{resub}", consumes = {"application/patch+json"}, produces = {"application/json"})
    public Resub editResub(@PathVariable String resub, @RequestBody EditResub editResub, @CurrentUser User user) {
        return service.editResub(editResub, service.getResub(resub), user);
    }

    @Operation(
            summary = "Create Post In Resub", description = "Create new post in a resub.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser
    @PostMapping(value = "/{resub}/posts", produces = {"application/json"}, consumes = {"application/json"})
    public Post createPostInResub(@PathVariable String resub, @RequestBody CreatePost createPost, @CurrentUser User user) {
        return postService.createPost(createPost, service.getResub(resub), user);
    }

    @Operation(
            summary = "Get Posts In Resub", description = "Get all posts in a resub.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{resub}/posts", produces = {"application/json"})
    public List<Post> getPostsInResub(@PathVariable String resub) {
        return postService.getAllPostsByParentResub(service.getResub(resub));
    }
}

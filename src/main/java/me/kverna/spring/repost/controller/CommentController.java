package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.CreateComment;
import me.kverna.spring.repost.data.EditComment;
import me.kverna.spring.repost.data.ErrorResponse;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.security.AuthorizeUser;
import me.kverna.spring.repost.security.CurrentUser;
import me.kverna.spring.repost.service.CommentService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@Validated
@Tag(name = "comments")
public class CommentController {

    private CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create Reply", description = "Create a reply to a comment.",
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
    @PostMapping(value = "/{commentId}", consumes = {"application/json"}, produces = {"application/json"})
    public Comment createReply(@PathVariable int commentId, @RequestBody CreateComment createComment, @CurrentUser User user) {
        Comment comment = service.getComment(commentId);
        Post post = comment.getParentPost();
        return service.createComment(createComment, post, post.getParentResub(), user);
    }

    @Operation(
            summary = "Edit Comment", description = "Edit a comment." +
            "Only the author of a comment can edit the comment.",
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
    @PatchMapping(value = "/{commentId}", consumes = {"application/patch+json"}, produces = {"application/json"})
    public Comment editComment(@PathVariable int commentId, @RequestBody EditComment editComment, @CurrentUser User user) {
        return service.editComment(editComment, service.getComment(commentId), user);
    }

    @Operation(
            summary = "Delete Comment", description = "Delete a specific comment. " +
            "Only the author of the comment or the owner of the parent resub can delete the comment.",
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
    @DeleteMapping(value = "/{commentId}")
    public void deleteComment(@PathVariable int commentId, @CurrentUser User user) {
        service.deleteComment(service.getComment(commentId), user);
    }

    @Operation(
            summary = "Vote Comment", description = "Vote on a comment in a post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = @SecurityRequirement(name = "OAuth2PasswordBearer", scopes = "user")
    )
    @AuthorizeUser
    @PatchMapping(value = "/{commentId}/vote/{vote}", produces = {"application/json"})
    public Comment voteComment(@PathVariable int commentId, @PathVariable @Range(min = -1, max = 1) int vote, @CurrentUser User user) {
        return service.voteComment(service.getComment(commentId), user, vote);
    }
}

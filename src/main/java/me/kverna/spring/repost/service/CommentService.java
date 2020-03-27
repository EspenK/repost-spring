package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.CommentVote;
import me.kverna.spring.repost.data.CreateComment;
import me.kverna.spring.repost.data.EditComment;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.CommentRepository;
import me.kverna.spring.repost.repository.CommentVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository repository;
    private CommentVoteRepository voteRepository;

    @Autowired
    public CommentService(CommentRepository repository, CommentVoteRepository voteRepository) {
        this.repository = repository;
        this.voteRepository = voteRepository;
    }

    /**
     * Get a comment with the given id.
     *
     * @param commentId the id of the comment.
     * @return the comment with the given comment id.
     */
    public Comment getComment(int commentId) {
        Optional<Comment> comment = repository.findById(commentId);
        if (comment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Comment %s was not found", commentId));
        }

        return comment.get();
    }

    /**
     * Create a new comment in the given resub and post with the given user as the author.
     *
     * @param createComment the comment to create.
     * @param post the post the comment belongs to.
     * @param resub the resub the comment belongs to.
     * @param author the user to be set as author.
     * @return the comment that is created.
     */
    public Comment createComment(CreateComment createComment, Post post, Resub resub, User author) {
        Comment comment = new Comment();
        comment.setParentResub(resub);
        comment.setParentPost(post);
        comment.setAuthor(author);
        comment.setContent(createComment.getContent());
        comment.setCreated(LocalDateTime.now());

        return repository.save(comment);
    }

    /**
     * Edit the comment.
     *
     * @param editComment the new comment fields.
     * @param comment the comment to edit.
     * @param user the user that performs the edit.
     * @return the edited comment.
     */
    public Comment editComment(EditComment editComment, Comment comment, User user) {
        if (!comment.isAuthor(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of this comment");
        }

        if (editComment.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Failed to validate field title: can't be null");
        }
        comment.setContent(editComment.getContent());
        comment.setEdited(LocalDateTime.now());

        return repository.save(comment);
    }

    /**
     * Delete the comment.
     *
     * @param comment the comment to delete.
     * @param user the user that performs the delete.
     */
    public void deleteComment(Comment comment, User user) {
        if (!comment.isUserAllowedToDelete(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of this comment");
        }

        repository.delete(comment);
    }

    public Comment voteComment(Comment comment, User author, int vote) {
        CommentVote commentVote = new CommentVote(comment, author, vote);
        voteRepository.save(commentVote);
        return getComment(comment.getId());
    }

    /**
     * Get all comments in a post.
     *
     * @param post the post.
     * @return a list of all comments in the post.
     */
    public List<Comment> getAllCommentsByPost(Post post) {
        return repository.findAllByParentPost(post);
    }

    /**
     * Get all comments by a user.
     *
     * @param author the user.
     * @return a list of all comments by the user.
     */
    public List<Comment> getAllCommentsByAuthor(User author) {
        return repository.findAllByAuthor(author);
    }

    /**
     * Get all comments in a resub.
     *
     * @param resub the resub.
     * @return a list of comments in the resub.
     */
    public List<Comment> getAllCommentsByResub(Resub resub) {
        return repository.findAllByParentResub(resub);
    }
}

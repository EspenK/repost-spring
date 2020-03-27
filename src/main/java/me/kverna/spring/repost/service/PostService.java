package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.CreatePost;
import me.kverna.spring.repost.data.EditPost;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.PostVote;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.PostRepository;
import me.kverna.spring.repost.repository.PostVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository repository;
    private PostVoteRepository voteRepository;

    @Autowired
    public PostService(PostRepository repository, PostVoteRepository voteRepository) {
        this.repository = repository;
        this.voteRepository = voteRepository;
    }

    /**
     * Get all posts in a resub.
     *
     * @param resub the resub.
     * @return a list of all posts in the resub.
     */
    public List<Post> getAllPostsByParentResub(Resub resub) {
        return repository.findAllByParentResub(resub);
    }

    /**
     * Get all posts by a user.
     *
     * @param author the user.
     * @return a list of all posts by the user.
     */
    public List<Post> getAllPostsByAuthor(User author) {
        return repository.findAllByAuthor(author);
    }

    /**
     * Get a post with the given id.
     *
     * @param postId the id of the post.
     * @return the post with the given id.
     */
    public Post getPost(int postId) {
        Optional<Post> post = repository.findById(postId);
        if (post.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Post %s was not found", postId));
        }

        return post.get();
    }

    /**
     * Create a new post with the user with the given username as the author.
     *
     * @param createPost the post to create.
     * @param author     the user to be set as author.
     * @return the post that is created.
     */
    public Post createPost(CreatePost createPost, Resub resub, User author) {
        Post post = new Post();
        post.setParentResub(resub);
        post.setTitle(createPost.getTitle());
        post.setContent(createPost.getContent());
        post.setUrl(createPost.getUrl());
        post.setAuthor(author);
        post.setCreated(LocalDateTime.now());

        return repository.save(post);
    }

    /**
     * Edit the post.
     *
     * @param editPost the new post fields.
     * @param post     the post to edit.
     * @param user the user that performs the edit.
     * @return the edited post.
     */
    public Post editPost(EditPost editPost, Post post, User user) {
        if (!post.isAuthor(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of this post.");
        }

        if (editPost.getTitle() != null) {
            if (editPost.getTitle().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Failed to validate field title: can't be null");
            }
            post.setTitle(editPost.getTitle().get());
        }
        if (editPost.getContent() != null) {
            post.setContent(editPost.getContent().isEmpty() ? null : editPost.getContent().get());
        }
        if (editPost.getUrl() != null) {
            post.setUrl(editPost.getUrl().isEmpty() ? null : editPost.getUrl().get());
        }

        post.setEdited(LocalDateTime.now());
        return repository.save(post);
    }

    /**
     * Delete the post if the user is the author of the post of the owner of the resub.
     *
     * @param post the post to delete.
     * @param user the user that performs the delete.
     */
    public void deletePost(Post post, User user) {
        if (!post.isUserAllowedToDelete(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of the post");
        }

        repository.delete(post);
    }

    public Post votePost(Post post, User author, int vote) {
        PostVote postVote = new PostVote(post, author, vote);
        voteRepository.save(postVote);
        return getPost(post.getId());
    }
}

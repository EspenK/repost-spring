package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.EditPost;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.PostRepository;
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
    private UserService userService;
    private ResubService resubService;

    @Autowired
    public PostService(PostRepository repository, UserService userService, ResubService resubService) {
        this.repository = repository;
        this.userService = userService;
        this.resubService = resubService;
    }

    /**
     * Get all posts in a resub with the given name.
     *
     * @param name the name of the resub.
     * @return a list of all posts in the resub with the given name.
     */
    public List<Post> getAllPostsByParentResubName(String name) {
        return repository.findAllByParentResub(resubService.getResub(name));
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
     * @param post     the post to create.
     * @param username the username of the user to be set as author.
     * @return the post that is created.
     */
    public Post createPost(Post post, String username) {
        User author = userService.getUser(username);
        post.setAuthor(author);
        post.setCreated(LocalDateTime.now());

        return repository.save(post);
    }

    /**
     * Edit the post with the given id.
     *
     * @param editPost the new post fields.
     * @param postId   the id of the post.
     * @return the edited post.
     */
    public Post editPost(EditPost editPost, int postId) {
        Post post = getPost(postId);

        String content = editPost.getContent();
        String url = editPost.getUrl();

        System.out.println(editPost);

        if (content != null) {
            post.setContent(content);
        }

        if (url != null) {
            post.setUrl(url);
        }

        post.setEdited(LocalDateTime.now());
        return repository.save(post);
    }

    public void deletePost(int postId) {
        Post post = getPost(postId);
        repository.delete(post);
    }
}

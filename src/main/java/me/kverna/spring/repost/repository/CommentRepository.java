package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthor(User author);

    List<Comment> findAllByParentResub(Resub resub);

    List<Comment> findAllByParentPost(Post post);
}

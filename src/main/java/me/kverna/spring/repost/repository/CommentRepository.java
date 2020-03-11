package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthorUsername(String username);

    List<Comment> findAllByParentResubName(String name);

    List<Comment> findAllByParentPostId(int postId);
}

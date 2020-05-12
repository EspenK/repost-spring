package me.kverna.spring.repost.repository;

import java.util.List;
import me.kverna.spring.repost.data.Comment;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthorOrderByCreatedDesc(User author, Pageable pageable);

    List<Comment> findAllByParentPostOrderByCreatedDesc(Post post, Pageable pageable);
}

package me.kverna.spring.repost.repository;

import java.util.List;
import me.kverna.spring.repost.data.Post;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAuthor(User author, Pageable pageable);

    List<Post> findAllByParentResub(Resub resub, Pageable pageable);
}

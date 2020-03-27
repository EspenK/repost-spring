package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.CommentVote;
import me.kverna.spring.repost.data.CommentVote.PrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, PrimaryKey> {
}


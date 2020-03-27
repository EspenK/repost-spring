package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.PostVote;
import me.kverna.spring.repost.data.PostVote.PrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, PrimaryKey> {
}


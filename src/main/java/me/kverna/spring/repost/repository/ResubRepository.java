package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.Resub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResubRepository extends JpaRepository<Resub, Integer> {
}

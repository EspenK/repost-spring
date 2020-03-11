package me.kverna.spring.repost.repository;

import me.kverna.spring.repost.data.Resub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResubRepository extends JpaRepository<Resub, Integer> {
    Resub findByName(String name);

    List<Resub> findAllByOwnerUsername(String username);

    void deleteByName(String name);
}

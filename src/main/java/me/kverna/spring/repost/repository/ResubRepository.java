package me.kverna.spring.repost.repository;

import java.util.List;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResubRepository extends JpaRepository<Resub, Integer> {

    Resub findByName(String name);

    List<Resub> findAllByOrderByCreatedDesc(Pageable pageable);

    List<Resub> findAllByOwnerOrderByCreatedDesc(User owner, Pageable pageable);
}

package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "comments")
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private int id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime edited;

    @ManyToOne
    private User author;
    @ManyToOne
    private Resub parent_resub;
    @ManyToOne
    private Post parent_post;
}

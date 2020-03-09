package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String url;
    private String content;
    private LocalDateTime created;
    private LocalDateTime edited;
}

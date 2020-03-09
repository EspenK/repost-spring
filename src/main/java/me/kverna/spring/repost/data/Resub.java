package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "resubs")
@Entity
@NoArgsConstructor
public class Resub {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String url;
    private String content;
    private LocalDateTime created;
    private LocalDateTime edited;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User owner;
}

package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

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
    private User owner;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resub")
    private Set<Post> posts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent_resub")
    private Set<Comment> comments;
}

package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Table(name = "users")
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String bio;
    private String avatar_url;
    private LocalDateTime created;
    private LocalDateTime edited;

    private String hashed_password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Resub> resubs;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Post> posts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Comment> comments;
}

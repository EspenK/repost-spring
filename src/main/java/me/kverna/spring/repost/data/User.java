package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
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

    @OneToMany(mappedBy = "resub")
    private Set<Resub> resubs;
    @OneToMany(mappedBy = "post")
    private Set<Post> posts;
    @OneToMany(mappedBy = "comment")
    private Set<Comment> comments;
}

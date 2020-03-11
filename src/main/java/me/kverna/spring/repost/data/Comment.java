package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonIgnore
    @ManyToOne
    private User author;

    @JsonIgnore
    @ManyToOne
    private Resub parentResub;

    @JsonIgnore
    @ManyToOne
    private Post parentPost;

    @JsonProperty(value = "parent_resub_name", access = JsonProperty.Access.READ_ONLY)
    public String getParentResubName() {
        return parentResub.getName();
    }

    @JsonProperty(value = "parent_post_id", access = JsonProperty.Access.READ_ONLY)
    public int getParentPostId() {
        return parentPost.getId();
    }

    @JsonProperty(value = "author_username", access = JsonProperty.Access.READ_ONLY)
    public String getAuthorUsername() {
        return author.getUsername();
    }
}

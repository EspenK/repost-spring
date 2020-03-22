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

    /**
     * The user is only allowed to delete a comment if they are
     * the author of the comment or the owner of the resub.
     *
     * @param user the user to check.
     * @return true if the user is allowed to delete the comment.
     */
    public boolean isUserAllowedToDelete(User user) {
        return parentPost.getAuthor() == user || author == user;
    }

    /**
     * Check if the user is the author of the comment.
     *
     * @param user the user to check.
     * @return true if the user is the author of the comment.
     */
    public boolean isAuthor(User user) {
        return author == user;
    }
}

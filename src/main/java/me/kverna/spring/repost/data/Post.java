package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "posts")
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String url;
    private String content;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime edited;

    @JsonIgnore
    @ManyToOne
    private User author;

    @JsonIgnore
    @ManyToOne
    private Resub parentResub;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentPost")
    private Set<Comment> comments;

    @JsonProperty(value = "parent_resub_name", access = JsonProperty.Access.READ_ONLY)
    public String getParentResubName() {
        return parentResub.getName();
    }

    @JsonProperty(value = "author_username", access = JsonProperty.Access.READ_ONLY)
    public String getAuthorUsername() {
        return author.getUsername();
    }

    /**
     * The user is only allowed to delete a post if they are
     * the author of the post or the owner of the resub.
     *
     * @param user the user to check.
     * @return true if the user is allowed to delete the post.
     */
    public boolean isUserAllowedToDelete(User user) {
        return parentResub.getOwner() == user || author == user;
    }

    /**
     * Check if the user is the author of the post.
     *
     * @param user the user to check.
     * @return true if the user is the author of the post.
     */
    public boolean isAuthor(User user) {
        return author == user;
    }
}

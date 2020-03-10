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
@Table(name = "resubs")
@Entity
@NoArgsConstructor
public class Resub {
    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;
    private String name;
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime edited;

    @JsonIgnore
    @ManyToOne
    private User owner;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resub")
    private Set<Post> posts;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent_resub")
    private Set<Comment> comments;

    @JsonProperty(value = "owner_username", access = JsonProperty.Access.READ_ONLY)
    public String getOwnerUsername() {
        return owner.getUsername();
    }
}

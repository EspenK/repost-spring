package me.kverna.spring.repost.data;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "posts_votes")
@Entity
@NoArgsConstructor
public class PostVote {

    @Embeddable
    public static class PrimaryKey implements Serializable {

        @ManyToOne
        @JoinColumn(name = "post_id")
        private Post post;

        @ManyToOne
        @JoinColumn(name = "author_id")
        private User author;
    }

    @EmbeddedId
    private PrimaryKey id;

    private int vote;

    public PostVote(Post post, User author, int vote) {
        this.id = new PrimaryKey();
        this.id.post = post;
        this.id.author = author;
        this.vote = vote;
    }
}

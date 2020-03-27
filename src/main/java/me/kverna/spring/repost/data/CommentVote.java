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
@Table(name = "comments_votes")
@Entity
@NoArgsConstructor
public class CommentVote {

    @Embeddable
    public static class PrimaryKey implements Serializable {

        @ManyToOne
        @JoinColumn(name = "comment_id")
        private Comment comment;

        @ManyToOne
        @JoinColumn(name = "author_id")
        private User author;
    }

    @EmbeddedId
    private PrimaryKey id;

    private int vote;

    public CommentVote(Comment comment, User author, int vote) {
        this.id = new PrimaryKey();
        this.id.comment = comment;
        this.id.author = author;
        this.vote = vote;
    }
}

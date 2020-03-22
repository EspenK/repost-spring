package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditResub {
    private String description;

    @JsonProperty(value = "new_owner_username")
    private String newOwnerUsername;
}

package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditResub {
    private String description;
    @JsonIgnore
    private String newOwnerUsername;

    @JsonProperty(value = "new_owner_username")
    public void setOwnerUsername(String ownerUsername) {
        newOwnerUsername = ownerUsername;
    }
}
package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditResub {
    private Optional<String> description;

    @JsonProperty(value = "new_owner_username")
    private String newOwnerUsername;
}

package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditUser {
    private Optional<String> bio;

    @JsonProperty(value = "avatar_url")
    private Optional<String> avatarUrl;
}

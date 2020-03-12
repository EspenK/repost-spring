package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUser {
    private String username;
    private String bio;

    @JsonProperty(value = "avatar_url")
    private String avatarUrl;
    private String password;
}

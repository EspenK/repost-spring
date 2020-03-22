package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditUser {
    private Optional<String> bio;

    @JsonProperty(value = "avatar_url")
    private Optional<String> avatarUrl;

    @JsonIgnore
    public boolean isBioChanged() {
        return bio != null;
    }

    public String getBio() {
        return bio.isEmpty() ? null : bio.get();
    }

    @JsonIgnore
    public boolean isAvatarUrlChanged() {
        return avatarUrl != null;
    }

    public String getAvatarUrl() {
        return avatarUrl.isEmpty() ? null : avatarUrl.get();
    }
}

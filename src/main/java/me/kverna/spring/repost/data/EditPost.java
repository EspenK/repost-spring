package me.kverna.spring.repost.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditPost {
    private String title;
    private Optional<String> content;
    private Optional<String> url;
}

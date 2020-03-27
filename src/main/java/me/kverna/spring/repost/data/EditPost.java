package me.kverna.spring.repost.data;

import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditPost {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> url;
}

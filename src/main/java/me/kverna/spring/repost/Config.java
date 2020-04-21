package me.kverna.spring.repost;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("repost")
public class Config {

    @Getter @Setter
    private String clientId = "repost";
    @Getter @Setter
    private String signingKey = null;
    @Getter @Setter
    private List<String> origins = List.of("http://localhost", "http://localhost:8080");
}

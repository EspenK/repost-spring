package me.kverna.spring.repost;

import me.kverna.spring.repost.security.AuthorizedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcContext implements WebMvcConfigurer {

    private AuthorizedUserResolver authorizedUserResolver;

    @Autowired
    public WebMvcContext(AuthorizedUserResolver authorizedUserResolver) {
        this.authorizedUserResolver = authorizedUserResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizedUserResolver);
    }
}

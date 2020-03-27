package me.kverna.spring.repost.security;

import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizedUserResolver implements HandlerMethodArgumentResolver {

    private UserService userService;

    @Autowired
    public AuthorizedUserResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(User.class) && methodParameter.getParameterAnnotation(CurrentUser.class) != null;
    }

    @Override
    public User resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // Return null when the current user is anonymous (not authenticated)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        try {
            return userService.getUser(authentication.getName());
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getReason());
        }
    }
}

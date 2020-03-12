package me.kverna.spring.repost.security;

import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizedUserResolver implements HandlerMethodArgumentResolver {
    private UserService userService;

    @Autowired
    public AuthorizedUserResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(User.class) && methodParameter.getParameterAnnotation(AuthorizedUser.class) != null;
    }

    @Override
    public User resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUser(username);
    }
}

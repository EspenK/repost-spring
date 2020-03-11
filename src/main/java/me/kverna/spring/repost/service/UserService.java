package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.CreateUser;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Get a user by username.
     *
     * @param username the username of the user.
     * @return the user with the given username.
     */
    public User getUser(String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User %s was not found", username));
        }

        return user;
    }

    public User createUser(CreateUser createUser) {
        User existingUser = repository.findByUsername(createUser.getUsername());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User %s already exists", createUser.getUsername()));
        }

        User user = new User();
        user.setUsername(createUser.getUsername());
        user.setBio(createUser.getBio());
        user.setAvatarUrl(createUser.getAvatarUrl());
        user.setCreated(LocalDateTime.now());

        return repository.save(user);
    }
}

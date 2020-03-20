package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.CreateUser;
import me.kverna.spring.repost.data.EditUser;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get a user by username.
     *
     * @param username the username of the user.
     * @return the user with the given username.
     */
    public User getUser(String username) {
        System.out.println(username);
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User %s was not found", username));
        }

        System.out.println(user);
        return user;
    }

    /**
     * Create a new user if no existing user has the same username.
     *
     * @param createUser the new user fields.
     * @return the user that is created.
     */
    public User createUser(CreateUser createUser) {
        User existingUser = repository.findByUsername(createUser.getUsername());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User %s already exists", createUser.getUsername()));
        }

        User user = new User();
        user.setUsername(createUser.getUsername());
        user.setHashedPassword(passwordEncoder.encode(createUser.getPassword()));
        user.setCreated(LocalDateTime.now());
        System.out.println(user);

        return repository.save(user);
    }

    /**
     * Edit the user with the given username.
     *
     * @param editUser the edited user fields.
     * @param user     the user to edit.
     * @return the edited user.
     */
    public User editUser(EditUser editUser, User user) {
        user.setBio(editUser.getBio());
        user.setAvatarUrl(editUser.getAvatarUrl());

        user.setEdited(LocalDateTime.now());
        return repository.save(user);
    }

    /**
     * Delete a user.
     *
     * @param user the user to delete.
     */
    public void deleteUser(User user) {
        repository.delete(user);
    }
}

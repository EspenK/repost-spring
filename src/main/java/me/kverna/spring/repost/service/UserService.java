package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.CreateUser;
import me.kverna.spring.repost.data.EditUser;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.ResubRepository;
import me.kverna.spring.repost.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private UserRepository repository;
    private ResubRepository resubRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, ResubRepository resubRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.resubRepository = resubRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setBio(createUser.getBio());
        user.setAvatarUrl(createUser.getAvatarUrl());
        user.setHashedPassword(passwordEncoder.encode(createUser.getPassword()));
        user.setCreated(LocalDateTime.now());

        return repository.save(user);
    }

    /**
     * Edit the user with the given username.
     *
     * @param editUser the edited user fields.
     * @param username the username of the user to edit.
     * @return the edited user.
     */
    public User editUser(EditUser editUser, String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User %s was not found", username));
        }

        String bio = editUser.getBio();
        String avatarUrl = editUser.getAvatarUrl();

        if (bio != null) {
            user.setBio(bio);
        }

        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }

        return repository.save(user);
    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete.
     */
    public void deleteUser(String username) {
        repository.deleteByUsername(username);
    }

    /**
     * Get all resubs owned by a user with the given username.
     *
     * @param username the username of the user.
     * @return a list of all resubs owned by the user with the given username.
     */
    public List<Resub> getAllResubsByOwnerUsername(String username) {
        return resubRepository.findAllByOwnerUsername(username);
    }
}

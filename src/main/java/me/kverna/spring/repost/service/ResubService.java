package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.CreateResub;
import me.kverna.spring.repost.data.EditResub;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.ResubRepository;
import me.kverna.spring.repost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResubService {

    private ResubRepository repository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public ResubService(ResubRepository repository, UserRepository userRepository, UserService userService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Get all resubs.
     *
     * @return a list of all resubs.
     */
    public List<Resub> getAllResubs() {
        return repository.findAll();
    }

    /**
     * Get a resub by name.
     *
     * @param name the name of the resub.
     * @return the resub with the given name.
     */
    public Resub getResub(String name) {
        Resub resub = repository.findByName(name);
        if (resub == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Resub %s was not found", name));
        }

        return resub;
    }

    /**
     * Create a resub if no resub with the same name exists.
     *
     * @param owner       the owner of the resub.
     * @param createResub the resub to create.
     * @return the resub that is created.
     */
    public Resub createResub(CreateResub createResub, User owner) {
        Resub existingResub = repository.findByName(createResub.getName());
        if (existingResub != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Resub %s already exists", createResub.getName()));
        }
        Resub resub = new Resub();
        resub.setName(createResub.getName());
        resub.setDescription(createResub.getDescription());
        resub.setOwner(owner);
        resub.setCreated(LocalDateTime.now());

        return repository.save(resub);
    }

    /**
     * Edit a resub by name.
     *
     * @param editResub the new resub fields.
     * @param resubName the name of the resub to edit.
     * @return the edited resub.
     */
    public Resub editResub(EditResub editResub, String resubName) {
        Resub resub = getResub(resubName);

        String newOwnerUsername = editResub.getNewOwnerUsername();
        String description = editResub.getDescription();
        if (description != null) {
            resub.setDescription(description);
        }

        if (newOwnerUsername != null) {
            User newOwner = new User();
            newOwner.setUsername(newOwnerUsername);
            resub.setOwner(userRepository.save(newOwner));
        }
        resub.setEdited(LocalDateTime.now());
        return repository.save(resub);
    }

    /**
     * Delete a resub by name.
     *
     * @param name the name of the resub to delete.
     */
    public void deleteResub(String name) {
        repository.deleteByName(name);
    }

    /**
     * Get all resubs owned by a user with the given username.
     *
     * @param username the username of the user.
     * @return a list of all resubs owned by the user with the given username.
     */
    public List<Resub> getAllResubsByOwnerUsername(String username) {
        return repository.findAllByOwner(userService.getUser(username));
    }
}

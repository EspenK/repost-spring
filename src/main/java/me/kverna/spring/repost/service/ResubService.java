package me.kverna.spring.repost.service;

import java.time.LocalDateTime;
import java.util.List;
import me.kverna.spring.repost.data.CreateResub;
import me.kverna.spring.repost.data.EditResub;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.ResubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResubService {

    private ResubRepository repository;
    private UserService userService;

    @Autowired
    public ResubService(ResubRepository repository, UserService userService) {
        this.repository = repository;
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
     * @param owner the user to be owner of the resub.
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
     * @param resub the resub to edit.
     * @param user the user that performs the edit.
     * @return the edited resub.
     */
    public Resub editResub(EditResub editResub, Resub resub, User user) {
        if (!resub.isOwner(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the owner of the resub");
        }

        String newOwnerUsername = editResub.getNewOwnerUsername();
        if (newOwnerUsername != null) {
            User newOwner = userService.getUser(newOwnerUsername);
            resub.setOwner(newOwner);
        }

        if (editResub.getDescription() != null) {
            resub.setDescription(
                    editResub.getDescription().isEmpty() ? null : editResub.getDescription().get());
        }

        resub.setEdited(LocalDateTime.now());
        return repository.save(resub);
    }

    /**
     * Delete a resub by name.
     *
     * @param resub the resub to delete.
     * @param user the user that performs the delete.
     */
    public void deleteResub(Resub resub, User user) {
        if (!resub.isUserAllowedToDelete(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the owner of the resub");
        }
        repository.delete(resub);
    }

    /**
     * Get all resubs owned by a user.
     *
     * @param owner the user.
     * @return a list of all resubs owned by the user.
     */
    public List<Resub> getAllResubsByOwner(User owner) {
        return repository.findAllByOwner(owner);
    }
}

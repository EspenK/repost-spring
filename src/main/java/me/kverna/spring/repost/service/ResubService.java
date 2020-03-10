package me.kverna.spring.repost.service;

import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.data.User;
import me.kverna.spring.repost.repository.ResubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResubService {

    private ResubRepository repository;

    @Autowired
    public ResubService(ResubRepository repository) {
        this.repository = repository;
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
     * @param owner the owner of the resub.
     * @param resub the resub to create.
     * @return the resub that is created.
     */
    public Resub createResub(User owner, Resub resub) {
        Resub existingResub = repository.findByName(resub.getName());
        if (existingResub != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Resub %s already exists", resub.getName()));
        }

        resub.setOwner(owner);
        resub.setCreated(LocalDateTime.now());
        return repository.save(resub);
    }

    /**
     * Edit a resub by name.
     *
     * @param resub     the new resub fields.
     * @param resubName the name of the resub to edit.
     * @return the edited resub.
     */
    public Resub editResub(Resub resub, String resubName) {
        Resub existingResub = repository.findByName(resubName);
        if (existingResub == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Resub %s was not found", resubName));
        }

        String description = resub.getDescription();
        if (description != null) {
            existingResub.setDescription(description);
        }
        existingResub.setEdited(LocalDateTime.now());
        return repository.save(existingResub);
    }

    /**
     * Delete a resub by name.
     *
     * @param name the name of the resub to delete.
     */
    public void deleteResub(String name) {
        repository.deleteByName(name);
    }
}

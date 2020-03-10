package me.kverna.spring.repost.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.kverna.spring.repost.data.Resub;
import me.kverna.spring.repost.service.ResubService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resub")
public class ResubController {

    private final ResubService service;

    public ResubController(ResubService service) {
        this.service = service;
    }

    @GetMapping(value = "/", produces = {"application/json"})
    public List<Resub> getAllResubs() {
        return service.getAllResubs();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping(value = "/{resub}", produces = {"application/json"})
    public Resub getResub(@PathVariable String resub) {
        return service.getResub(resub);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "404")
    })
    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public Resub createResub(@RequestBody Resub resub) {
        return service.createResub(null, resub);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping(value = "/{resub}")
    public void deleteResub(@PathVariable String resub) {
        service.deleteResub(resub);
    }
}

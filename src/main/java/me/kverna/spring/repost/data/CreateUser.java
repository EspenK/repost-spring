package me.kverna.spring.repost.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUser {
    private String username;
    private String password;
}

package me.kverna.spring.repost.data;

import lombok.Data;

@Data
public class CreatePost {
    private String title;
    private String content;
    private String url;
}

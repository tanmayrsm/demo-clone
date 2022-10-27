package com.example.demo.dto;

import java.util.Set;

import com.example.demo.model.VideoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {
    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String videoURL;
    private VideoStatus videoStatus;
    private String thumbnailURL;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
}

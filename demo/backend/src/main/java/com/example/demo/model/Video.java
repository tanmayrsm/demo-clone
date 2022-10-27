package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value = "Video")    // mongo will save this as table name 'Video'
@Data           // with this annotation, lombok generates getter and setter for this DTO
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id         // denotes primary key
    private String id;
    private String title;
    private String description;
    private String userId;
    private AtomicInteger likes = new AtomicInteger(0);
    private AtomicInteger disLikes = new AtomicInteger(0);
    private Set<String> tags;
    private String videoURL;
    private VideoStatus videoStatus;
    private AtomicInteger viewCount = new AtomicInteger(0);
    private String thumbnailURL;
    private List<Comment> commentList = new CopyOnWriteArrayList<>();

    public void incrementLikes(){
        likes.incrementAndGet();
    }
    public void decrementLikes(){
        likes.decrementAndGet();
    }
    
    public void incrementdisLikes(){
        disLikes.incrementAndGet();
    }
    public void decrementdisLikes(){
        disLikes.decrementAndGet();
    }
    public void incrementviewCount() {
        viewCount.incrementAndGet();
    }
    public void addComment(Comment comment) {
        commentList.add(comment);
    }
    
}

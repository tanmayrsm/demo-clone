package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")    // mongo will save this as table name 'Video'
@Data           // with this annotation, lombok generates getter and setter for this DTO
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();;
    private Set<String> subscribers = ConcurrentHashMap.newKeySet();;
    private Set<String> videoHistory = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos = ConcurrentHashMap.newKeySet();
    private Set<String> dislikedVideos = ConcurrentHashMap.newKeySet();
    private String sub;

    public void addToLikeVideos(String videoById) {
        likedVideos.add(videoById);
    }

    public void removeFromLikedVideo(String videoId) {
        likedVideos.remove(videoId);
    }

    public void removeFromDislikedVideo(String videoId) {
        dislikedVideos.remove(videoId);
    }

    public void addToDislikeVideo(String videoId) {
        dislikedVideos.add(videoId);
    }

    public void addToVideoHistory(String videoId) {
        videoHistory.add(videoId);
    }

    public void addToSubscribedUsers(String userId) {
        subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String userId) {
        subscribers.add(userId);
    }

    public void removeFromSubscribers(String userId) {
        subscribers.remove(userId);
    }

    public void removeFromSubscribedUsers(String userId) {
        subscribedToUsers.remove(userId);
    }
}

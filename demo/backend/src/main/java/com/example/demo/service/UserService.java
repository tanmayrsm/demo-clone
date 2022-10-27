package com.example.demo.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String sub = ((Jwt)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");

        return userRepository.findBySub(sub).orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub ::" + sub));
    }

    public void addToLikedVideos(String videoById) {
        // add this video to liked video collection of particular user

        User user = getCurrentUser();
        user.addToLikeVideos(videoById);
        userRepository.save(user);
    }

    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    public boolean ifDislikedVideo(String videoId) {
        return getCurrentUser().getDislikedVideos().stream().anyMatch(disLikedVideo -> disLikedVideo.equals(videoId));
    }

    public void removeFromLikedVideo(String videoId) {
        User user = getCurrentUser();
        user.removeFromLikedVideo(videoId);
        userRepository.save(user);
    }

    public void removeFromDislikedVideo(String videoId) {
        User user = getCurrentUser();
        user.removeFromDislikedVideo(videoId);
        userRepository.save(user);
    }

    public void addToDislikedVideos(String videoId) {
        // add this video to liked video collection of particular user

        User user = getCurrentUser();
        user.addToDislikeVideo(videoId);
        userRepository.save(user);
    }

    public void addVideoToHistory(String videoId) {
        User user = getCurrentUser();
        user.addToVideoHistory(videoId);
        userRepository.save(user);
    }

    public void subscribeUser(String userId) {  // this userId is id of content creator, whom we are subscribing

        // get curent user
        User currentUser = getCurrentUser();
        
        // add this channel to current users subscribe channel list
        currentUser.addToSubscribedUsers(userId);
        
        // add userId to creators subscriber list
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Cannot find user with id ::" + userId));

        user.addToSubscribers(currentUser.getId());

        // save to db
        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unSubscribeUser(String userId) {  // this userId is id of content creator, whom we are subscribing

        // get curent user
        User currentUser = getCurrentUser();
        
        // remove this channel from current users subscribe channel list
        currentUser.removeFromSubscribedUsers(userId);
        
        // remove userId from creators subscriber list
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Cannot find user with id ::" + userId));

        user.removeFromSubscribers(currentUser.getId());

        // save to db
        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public Set<String> userHistory(String userId) {
        User user = getUserById(userId);
        return user.getVideoHistory();
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("cannot find user with id ::" + userId));
    }
}

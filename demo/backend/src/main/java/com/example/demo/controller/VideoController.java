package com.example.demo.controller;


import com.example.demo.dto.CommentedDTO;
import com.example.demo.dto.UploadVideoResponse;
import com.example.demo.dto.VideoDTO;
import com.example.demo.service.VideoService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    @Autowired
    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file")MultipartFile file){
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file")MultipartFile file, @RequestParam("videoId") String video){
        return videoService.uploadThumbnail(file, video);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO editVideoStatus(@RequestBody VideoDTO videoData){
        return videoService.updateVideo(videoData);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO getVideoDetails(@PathVariable String videoId){
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/disLike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO dislikeVideo(@PathVariable String videoId) {
        return videoService.dislikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public String addComment(@PathVariable String videoId, @RequestBody CommentedDTO commentDTO) {
        videoService.addComment(videoId, commentDTO);
        return "Comment added";
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentedDTO> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDTO> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping(value = "/iid")
    public String testGet() {
        System.out.println("Called getter");
        return "getter works";
    }
}

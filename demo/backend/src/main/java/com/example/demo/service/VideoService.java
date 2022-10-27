package com.example.demo.service;

import com.example.demo.dto.CommentedDTO;
import com.example.demo.dto.ContentDTO;
import com.example.demo.dto.UploadVideoResponse;
import com.example.demo.dto.VideoDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {
    @Autowired
    private final S3Service fbService;
    @Autowired
    private final VideoRepository videoRepository;
    @Autowired
    private final UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile file){
        // upload file to AWS S3
        // save video data to Db
        String vidurl = fbService.uploadFile(file, ContentDTO.VIDEO);
        Video video = new Video();
        video.setVideoURL(vidurl);

        Video savedVideo = videoRepository.save(video);

        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoURL());
    }

    public VideoDTO updateVideo(VideoDTO videoData) {
        // find the video by id
        Video saveVideo = getVideoByVideoId(videoData.getId());
        // map the videoDTO fields to video
        saveVideo.setTitle(videoData.getTitle());
        saveVideo.setDescription(videoData.getDescription());
        saveVideo.setTags(videoData.getTags());
        saveVideo.setThumbnailURL(videoData.getThumbnailURL());
        saveVideo.setVideoStatus(videoData.getVideoStatus());
        // we dont need to updte video id again...as it will tay the same in mngoDB

        // save the video to mongoDB
        videoRepository.save(saveVideo);

        return videoData;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        // upload thumbnail to fb
        Video saveVidThumbnail = getVideoByVideoId(videoId);
        String thumbnailURL = fbService.uploadFile(file, ContentDTO.IMAGE);
        
        //save thumbnail info in mongoDB
        saveVidThumbnail.setThumbnailURL(thumbnailURL);
        videoRepository.save(saveVidThumbnail);

        return thumbnailURL;
    }

    public VideoDTO getVideoDetails(String videoId) {
        Video video = getVideoByVideoId(videoId);

        // increase video view count
        incrementVideoViewCount(video);
        
        // add video to user history
        userService.addVideoToHistory(videoId);
        
        return mapToVideoDTO(video);
    }

    private void incrementVideoViewCount(Video video) {
        video.incrementviewCount();
        videoRepository.save(video);
    }

    public VideoDTO likeVideo(String videoId) {
        // get video by id
        Video videoById = getVideoByVideoId(videoId);

        // increment Like count

        // if user already liked it, decrement like count, and remove it from users-liked video list
        if(userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideo(videoId);
        }
        // if user has already disliked it, increment like count, and add it to users-liked video list
        else if(userService.ifDislikedVideo(videoId)) {
            videoById.decrementdisLikes();
            userService.removeFromDislikedVideo(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        // if user has no action on this video previously, increment like count, and add it users list of liked video
        else {
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDTO(videoById);
        
    }

    private Video getVideoByVideoId(String id) {
        return videoRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Cannot find video by id ::" + id);
        });
    }

    public VideoDTO dislikeVideo(String videoId) {
        // get video by id
        Video videoById = getVideoByVideoId(videoId);

        // increment Like count

        // if user already liked it, decrement like count, and remove it from users-liked video list
        if(userService.ifDislikedVideo(videoId)) {
            videoById.decrementdisLikes();
            userService.removeFromDislikedVideo(videoId);
        }
        // if user has already disliked it, increment like count, and add it to users-liked video list
        else if(userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideo(videoId);
            videoById.incrementdisLikes();
            userService.addToDislikedVideos(videoId);
        }
        // if user has no action on this video previously, increment like count, and add it users list of liked video
        else {
            videoById.incrementdisLikes();
            userService.addToDislikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDTO(videoById);
    }

    
    private VideoDTO mapToVideoDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setTitle(video.getTitle());
        videoDTO.setDescription(video.getDescription());
        videoDTO.setTags(video.getTags());
        videoDTO.setThumbnailURL(video.getThumbnailURL());
        videoDTO.setVideoStatus(video.getVideoStatus());
        videoDTO.setId(video.getId());
        videoDTO.setVideoURL(video.getVideoURL());
        videoDTO.setLikeCount(video.getLikes().get());
        videoDTO.setDislikeCount(video.getDisLikes().get());
        videoDTO.setViewCount(video.getViewCount().get());
        return videoDTO;
    }

    public void addComment(String videoId, CommentedDTO commentDTO) {
        Video video = getVideoByVideoId(videoId);
        Comment comment = new Comment();
        comment.setText(commentDTO.getCommentText());
        comment.setAuthorId(commentDTO.getAuthorId());

        video.addComment(comment);

        videoRepository.save(video);
    }

    public List<CommentedDTO> getAllComments(String videoId) {
        Video video = getVideoByVideoId(videoId);
        List<Comment> commentList = video.getCommentList();
        return commentList.stream().map(comment -> mapToCommentsDTO(comment)).collect(Collectors.toList());

    }

    private CommentedDTO mapToCommentsDTO(Comment comment) {
        CommentedDTO commentedDTO = new CommentedDTO();
        commentedDTO.setCommentText(comment.getText());
        commentedDTO.setAuthorId(comment.getAuthorId());

        return commentedDTO;
    }

    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAll().stream().map(video -> mapToVideoDTO(video)).collect(Collectors.toList());
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user.service';
import { VideoService } from '../services/video.service';

@Component({
  selector: 'app-video-detail',
  templateUrl: './video-detail.component.html',
  styleUrls: ['./video-detail.component.scss']
})
export class VideoDetailComponent implements OnInit {
  showSubscribeButton = true;
  showUnSubscribeButton = false;

  currentVideoId!: string;
  currentVideoURL!: string;
  videoAvailable = false;
  videoMetaData! : {
    title: string,
    description: string,
    tags?: Array<string>,
    likeCount?: number,
    dislikeCount?: number,
    viewCount?: number,
  };

  constructor(private _activatedRoute: ActivatedRoute, private _videoService: VideoService, private _userService: UserService) {
    this.currentVideoId = _activatedRoute.snapshot.params['videoId'];

    // get current video to be displayed
    this._videoService.getVideo(this.currentVideoId).subscribe({
      next: data_ => {
        this.currentVideoURL = data_.videoURL;
        this.videoAvailable = true;
        this.videoMetaData = {title: data_.title, description: data_.description, tags: data_.tags, likeCount: data_.likeCount, dislikeCount: data_.dislikeCount, viewCount: data_.viewCount};
      },
      error: err_ => {
        console.error("Can't fetch video");
      }
    })
   }

  ngOnInit(): void {
  }
  likeVideo(){
    this._videoService.likeVideo(this.currentVideoId).subscribe({
      next: data_ => {
        this.videoMetaData.likeCount = data_.likeCount;
        this.videoMetaData.dislikeCount = data_.dislikeCount;
      }
    });
  }
  disLikeVideo(){
    this._videoService.dislikeVideo(this.currentVideoId).subscribe({
      next: data_ => {
        this.videoMetaData.likeCount = data_.likeCount;
        this.videoMetaData.dislikeCount = data_.dislikeCount;
      }
    });
  }
  subscribeToUser(){
    let userId = this._userService.getUserById();
    this._userService.subscribeToUser(userId).subscribe({
      next: data_ => {
        this.showUnSubscribeButton = true;
        this.showSubscribeButton = false;
      }
    });
  }
  unSubscribeToUser(){
    let userId = this._userService.getUserById();
    this._userService.unSubscribeToUser(userId).subscribe({
      next: data_ => {
        this.showUnSubscribeButton = false;
        this.showSubscribeButton = true;
      }
    });
  }

}

import { Component, OnInit } from '@angular/core';
import { Form, FormControl, FormGroup } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { VideoDTO } from '../interfaces';
import { VideoService } from '../services/video.service';

@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrls: ['./save-video-details.component.scss']
})
export class SaveVideoDetailsComponent implements OnInit {

  public saveVideoFormDetail: FormGroup;
  public title: FormControl = new FormControl('');
  public description: FormControl = new FormControl('');
  public videoStatus: FormControl = new FormControl('');
  public ENTER = 1;
  public COMMA = 2;

  addOnBlur = true;
  selectedFile!: File;
  currentVideoId!: string;
  currentVideoURL!: string;
  currentThumbnailURL!: string;

  readonly separatorKeysCodes = [this.ENTER , this.COMMA];
  tags: string[] = [];
  selectedFileName!: string;

  constructor(private _activatedRoute: ActivatedRoute, private _videoService: VideoService, private _snackBar: MatSnackBar) {
    this.currentVideoId = _activatedRoute.snapshot.params['videoId'];
    this.saveVideoFormDetail = new FormGroup({
      title: this.title,
      description : this.description,
      videoStatus: this.videoStatus
    });
    // get current video to be displayed
    this._videoService.getVideo(this.currentVideoId).subscribe({
      next: data_ => {
        this.currentVideoURL = data_.videoURL;
        this.currentThumbnailURL = data_.thumbnailURL || '';
      },
      error: err_ => {
        this._snackBar.open("Can't fetch video, please try again", "OK");
        console.error("Can't fetch video");
      }
    });
   }

  ngOnInit(): void {
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our tag
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(tagValue: string): void {
    const index = this.tags.indexOf(tagValue);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }


  onFileSelected(event_: Event) {
    // @ts-ignore
    this.selectedFile = event_.target?.files[0];
    this.selectedFileName = this.selectedFile.name;
  }

  uploadThumbnail() {
    this._videoService.uploadThumbnail(this.selectedFile, this.currentVideoId).subscribe({
      next: data_ => {
        // console.log("SaveVideoDetailsComponent :: Thumnail url :: ", data_);
        this._snackBar.open("Thumbnail uploaded successfully", "OK");
        this.currentThumbnailURL = data_;
      },
      error: err_ => {
        console.error("SaveVideoDetailsComponent :: Error while uploading thumbnail :: ", err_);
        this._snackBar.open("Thumbnail not uploaded, please try again", "OK");
      }
    })
  }

  saveVideo() {
    const videoMetaData: VideoDTO = {
      id: this.currentVideoId,
      title: this.saveVideoFormDetail.get('title')?.value,
      description: this.saveVideoFormDetail.get('description')?.value,
      tags: this.tags,
      videoStatus: this.saveVideoFormDetail.get('videoStatus')?.value,
      videoURL: this.currentVideoURL,
      thumbnailURL: this.currentThumbnailURL,
      likeCount: 0,
      dislikeCount: 0,
      viewCount: 0
    };
    this._videoService.saveVideoDetails(videoMetaData).subscribe({
      next: data_ => {
        this._snackBar.open("Video details saved", "OK");
      },
      error: err_ => {
        console.error("SaveVideoDetailsComponent :: Error while saving video :: ", err_);
        this._snackBar.open("Couldn't save video details, please try again", "OK");
      }
    });
  }

}

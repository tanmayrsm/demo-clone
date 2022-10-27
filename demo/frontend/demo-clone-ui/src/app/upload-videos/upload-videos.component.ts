import { NgxFileDropEntry, FileSystemFileEntry, FileSystemDirectoryEntry } from 'ngx-file-drop';
import { Component, OnInit } from '@angular/core';
import { VideoService } from '../services/video.service';
import { UploadVideResponse } from '../interfaces';
import { Router } from '@angular/router';

@Component({
  selector: 'app-upload-videos',
  templateUrl: './upload-videos.component.html',
  styleUrls: ['./upload-videos.component.scss']
})
export class UploadVideosComponent implements OnInit {

  constructor(private _videoService: VideoService, private _router: Router) { }

  ngOnInit() {
  }

  public fileEntry: FileSystemFileEntry | undefined;
  public files: NgxFileDropEntry[] = [];
  public isFileUploaded = false;

  public dropped(files: NgxFileDropEntry[]) {
    this.files = files;
    for (const droppedFile of files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile) {
        this.fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        this.fileEntry.file((file: File) => {

          // Here you can access the real file
          console.log(droppedFile.relativePath, file);

          this.isFileUploaded = true;

        });
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  public fileOver(event: Event){
    console.log(event);
  }

  public fileLeave(event: Event){
    console.log(event);
  }

  public uploadVideo() {
    // upload video to backend
    if(!!this.fileEntry) {
      this.fileEntry.file(file_ => {
        this._videoService.uploadVideo(file_).subscribe({
          next : data_ => {
            console.log("UploadVideoComponent :: File uploaded!", data_);
            this._router.navigateByUrl("/save-video-details/" + data_.videoId);
          },
          error: err_ => console.error("UploadVideoComponent :: Error while uploading file ::", err_)
        });
      })
    }
  }

}

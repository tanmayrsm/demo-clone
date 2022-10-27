import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
// import { FileSystemFileEntry } from 'ngx-file-drop';
import {Observable} from 'rxjs';
import { UploadVideResponse, VideoDTO } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private _httpClient: HttpClient) { }

  uploadVideo(fileEntry: File): Observable<UploadVideResponse> {

    // You could upload it like this:
    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name)

    // Headers
    // const headers = new HttpHeaders({
    //   'security-token': 'mytoken'
    // })

    // this.http.post('https://mybackend.com/api/upload/sanitize-and-save-logo', formData, { headers: headers, responseType: 'blob' })
    // .subscribe(data => {
      // Sanitized logo returned from backend
    // })

    // http POST call
    return this._httpClient.post<UploadVideResponse>("http://localhost:8080/videos/", formData);
  }

  uploadThumbnail(thumbnailFile: File, videoId: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', thumbnailFile, thumbnailFile.name);
    formData.append('videoId', videoId);

    let HTTPOptions:Object = {

      responseType: 'text'
   };
    // http POST call
    return this._httpClient.post<string>("http://localhost:8080/videos/thumbnail", formData, HTTPOptions);
  }

  getVideo(videoId: string): Observable<VideoDTO> {
    return this._httpClient.get<VideoDTO>("http://localhost:8080/videos/" + videoId)
  }

  saveVideoDetails(videoMetaData: VideoDTO): Observable<VideoDTO> {
    return this._httpClient.put<VideoDTO>("http://localhost:8080/videos/", videoMetaData);
  }

  getAllVideos(): Observable<Array<VideoDTO>> {
    return this._httpClient.get<Array<VideoDTO>>("http://localhost:8080/videos/");
  }

  likeVideo(currentVideoId: string): Observable<VideoDTO> {
    return this._httpClient.post<VideoDTO>("http://localhost:8080/videos/" + currentVideoId + "/like", null);
  }

  dislikeVideo(currentVideoId: string): Observable<VideoDTO> {
    return this._httpClient.post<VideoDTO>("http://localhost:8080/videos/" + currentVideoId + "/disLike", null);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentsDTO } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  constructor(private _http: HttpClient) { }

  postComment(commentObj: CommentsDTO, videoId_: string): Observable<any> {
    return this._http.post("http://localhost:8080/videos/" + videoId_ + "/comment", commentObj, {responseType: 'text'});
  }

  getAllComments(videoId: string): Observable<Array<CommentsDTO>> {
    return this._http.get<Array<CommentsDTO>>("http://localhost:8080/videos/" + videoId + "/comment");
  }

}

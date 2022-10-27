import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userId: string = "";

  constructor(private _httpClient: HttpClient) { }

  subscribeToUser(userId: string): Observable<boolean> {
    return this._httpClient.post<boolean>("http://localhost:8080/user/subscribe/" + userId, null);
  }

  unSubscribeToUser(userId: string) {
    return this._httpClient.post<boolean>("http://localhost:8080/user/unsubscribe/" + userId, null);
  }

  registerUser() {
    this._httpClient.get("http://localhost:8080/user/register", {responseType: "text"}).subscribe({
      next: data_ => this.userId = data_
    });
  }

  getUserById() {
    return this.userId;
  }
}

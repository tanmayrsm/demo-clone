import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommentsDTO } from '../interfaces';
import { CommentsService } from '../services/comments.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {
  commentsDto: Array<CommentsDTO> = [];
  commentsForm!: FormGroup;

  @Input() videoId!: string;

  constructor(private _userService: UserService, private _commentsService: CommentsService, private _snack: MatSnackBar) {

  }

  ngOnInit(): void {
    this.commentsForm = new FormGroup({
      comment: new FormControl("comment")
    });

    // get all comments
    this.getAllComments();
  }

  postComment(){
    const comment = this.commentsForm.get('comment')?.value;
    const commentObj: CommentsDTO = {
      commentText: comment,
      authorId: this._userService.getUserById()
    };
    console.log("u s id:: ", commentObj.authorId);
    this._commentsService.postComment(commentObj, this.videoId).subscribe({
      next: () =>{ this._snack.open("Comment posted!", "ok");
        this.commentsForm.get('comment')?.reset();
        this.getAllComments();
      },
      error: () => this._snack.open("Couldn't post comment, please try again")

    });
  }

  private getAllComments() {
    this._commentsService.getAllComments(this.videoId).subscribe({
      next: data_ => {
        this.commentsDto = data_;
      }
    });
  }

}

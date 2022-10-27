import { Component, OnInit } from '@angular/core';
import { VideoDTO } from '../interfaces';
import { VideoService } from '../services/video.service';

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.scss']
})
export class FeaturedComponent implements OnInit {
  featuredVideos: Array<VideoDTO> = [];

  constructor(private _videoService: VideoService) { }

  ngOnInit(): void {
    this._videoService.getAllVideos().subscribe({
      next: response_ => {
        this.featuredVideos = response_;
      }
    })
  }

}

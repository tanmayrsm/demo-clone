export interface UploadVideResponse {
  videoId: string,
  videoURL: string
}

export interface VideoDTO {
  id: string;
  title: string;
  description: string;
  tags?: Array<string>;
  videoURL: string;
  videoStatus?: string;
  thumbnailURL?: string;
  likeCount: number;
  dislikeCount: number;
  viewCount: number;
}

export interface CommentsDTO {
  commentText: string;
  authorId: string;
}

export class SimplePostDto {
  constructor(content) {
    this.id = content.id;
    this.no = content.id;
    this.title = content.title;
    this.commentSize = content.commentSize;
    this.writer = content.username;
    this.hit = content.hit;
    this.time = content.createdDate;
    this.hasImage = content.hasImage;
    this.type = content.type;
  }
}
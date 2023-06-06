export class PostDto {
  constructor(title, content, writer, images, postTime, hit) {
    this.title = title !== undefined ? title : "";
    this.content = content !== undefined ? content : "";
    this.writer = writer !== undefined ? writer : "";
    this.images = images !== undefined ? images : [];
    this.postTime = postTime;
    this.hit = hit;

  }
}
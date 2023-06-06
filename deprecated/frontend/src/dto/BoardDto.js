import { SimplePostDto } from '@/dto/SimplePostDto';

export class BoardDto {
  constructor(data) {
    this.posts = [];
    for(let i = 0; i < data.content.length; i++) {
      this.posts.push(new SimplePostDto(data.content[i]));
    }
    this.size = data.size;
    this.totalElements = data.totalElements;
    this.totalPages = data.totalPages;
    this.currentPage = data.number;

    this.pageInfo = [];
    this.mobilePageInfo = [];
    this.setPageInfo(this.pageInfo, 10);
    this.setPageInfo(this.mobilePageInfo, 5);
  }

  setPageInfo(pageInfo, size) {
    let temp = parseInt(this.currentPage / size) * size;
    for (let i = 0; i < size; i++) {
      if (temp + i >= this.totalPages) break;
      pageInfo.push(temp + i);
    }
  }
}
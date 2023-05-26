<template>
    <section class="article-list" v-if="articles">
        <div class="article-item" v-for="(article, idx) in articles" :key="idx" @click="handleClickArticle(article?.id)"
             style="cursor: pointer">
            <div class="article-image">
                <img v-if="article?.thumbnail" :src="article.thumbnail" alt="thumbnail">
                <img v-else src="../../assets/no-image.svg" alt="thumbnail">
            </div>
            <div class="article-info">
                <div class="title-container">
                            <span class="title">
                                <span class="text">{{ article.title }}</span>
                                <span class="comment-count">11</span>
                            </span>
                </div>
                <div class="etc">
                    <div class="nick-name" :class="[ !article.nickName ? 'deleted-user': '' ]">{{
                        article.nickName || '[탈퇴한사용자]'
                        }}
                    </div>
                    <div class="dot"></div>
                    <div class="create-at">{{ timeAgoFormat(article.savedAt) }}</div>
                    <div class="dot"></div>
                    <div class="view-count">
                        <font-awesome-icon icon="fa-regular fa-eye"/>
                        {{ article.viewCount }}
                    </div>
                    <div class="dot"></div>
                    <div class="like-count" v-if="article?.likeCount">
                        <font-awesome-icon icon="fa-regular fa-thumbs-up"/>
                        {{ article.likeCount }}
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
import {timeAgoFormat} from "@/utils/timeUtils";

export default {
  name: "ArticleList",
  props: {
    articles: Array,
  },
  data() {
    return {}
  },
  methods: {
    timeAgoFormat,
    handleClickArticle(id) {
      this.$router.push(`/articles/${id}`)
    },
  }
}
</script>

<style lang="scss" scoped>
.deleted-user {
  font-style: italic;
}

.article-item {
  display: flex;
  padding: 5px 10px;
  border-bottom: 1px solid rgb(0 0 0 / 5%);
}

.article-image {
  img {
    background-repeat: no-repeat;
    width: 55px;
    height: 45px;
    border-radius: 0.5rem;
    margin-right: 10px;
    object-fit: cover;
  }
}

.article-info {
  display: flex;
  flex-direction: column;
  gap: 3px;

  .title-container {
    display: flex;
    font-size: .875rem;

    .title {
      .comment-count {
        font-size: .9rem;
        font-weight: 700;
        margin-left: 5px;
        color: #1ca8af;
      }
    }
  }

  .etc {
    display: flex;
    font-size: .8rem;
    color: rgb(0 0 0 / 40%);

    .dot {
      display: flex;
      align-items: center;
      justify-content: center;
      width: .875rem;

      &::after {
        width: .125rem;
        height: .125rem;
        content: "";
        background-color: rgb(0 0 0 / 10%);
      }
    }

    .like-count {
      color: #000;
    }
  }
}
</style>

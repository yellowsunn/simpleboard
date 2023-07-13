<template>
    <div class="info" v-if="article">
        <div class="title-container">
            <div class="title">{{ article.title }}</div>
        </div>
        <div class="etc">
            <div class="user-image">
                <img v-if="article.user?.thumbnail" class="rounded-circle" :src="article.user.thumbnail" referrerpolicy="no-referrer-when-downgrade"/>
                <img v-else class="rounded-circle" src="../../assets/default-thumbnail.svg" :width="width"
                     :height="height"
                     alt="thumbnail"/>
            </div>
            <div class="nick-name">
                {{ article.user?.nickName }}
            </div>
            <div class="dot"></div>
            <div class="created-at">
                {{ timeAgoFormat(article.savedAt) }}
            </div>
            <div class="dot"></div>
            <div class="view-count">
                <font-awesome-icon icon="fa-regular fa-eye"/>
                {{ article.viewCount }}
            </div>
            <div class="dot"></div>
            <div class="like-count">
                <font-awesome-icon icon="fa-regular fa-thumbs-up"/>
                {{ article.likeCount }}
            </div>
        </div>
    </div>
</template>

<script>
import {timeAgoFormat} from "@/utils/timeUtils";

export default {
  name: "ArticleHeader",
  methods: {timeAgoFormat},
  props: {
    article: Object,
  },
  data() {
    return {}
  },
  computed: {}
}
</script>

<style lang="scss" scoped>
.info {
  padding: 20px 0;
  border-bottom: 1px solid rgb(0 0 0 / 5%);
}

.title-container {
  margin-bottom: 10px;

  .title {
    font-size: 1.5rem;
    font-weight: 700;
    text-align: left;

    & {
      @media (max-width: 950px) {
        font-size: 1.3rem;
      }
    }

  }
}

.etc {
  display: flex;
  padding: 5px 0;
  font-size: .875rem;
  color: rgb(0 0 0 / 40%);
  align-items: center;

  .user-image {
    margin-right: 5px;

    img {
      width: 32px;
      height: 32px;
    }
  }

  .nick-name {
    display: flex;
  }

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

.dropdown-menu {
  --bs-dropdown-min-with: none;
  --bs-dropdown-font-size: none;
}

.dropdown-toggle::after {
  content: none;
}
</style>

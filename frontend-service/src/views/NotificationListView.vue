<template>
  <div class="mx-auto" style="max-width: 950px">
    <section class="d-flex">
      <div class="notifications-title">알림</div>
    </section>
    <section class="notification-list" v-if="notificationPage?.notifications">
      <div class="notification-item border rounded p-3" :class="[notification?.readAt && 'read']"
           v-for="(notification, idx) in notificationPage.notifications"
           :key="idx" @click="clickNotification(notification)">
        <div class="d-flex justify-content-end">
          <div v-if="notification?.createdAt" style="font-size: 14px">{{ timeAgoFormat(notification.createdAt) }}</div>
        </div>
        <div class="notification-title d-flex">{{ notification.title }}</div>
        <div class="d-block notification-content">{{ notification.content }}</div>
      </div>
    </section>
    <CustomPagination class="m-3" v-if="pageInfo" :page-info="pageInfo" :page-size="5"
                      @click-page="clickPage"></CustomPagination>
  </div>
</template>

<script>

import {timeAgoFormat} from "@/utils/timeUtils";
import CustomPagination from "@/components/CustomPagination.vue";

export default {
  name: "NotificationListView",
  components: {CustomPagination},
  data() {
    return {
      notificationPage: null,
      pageInfo: null,
    }
  },
  async mounted() {
    const currentPage = this.$route.query?.page || 1
    const {isError, data} = await this.$boardApi('GET', `/api/v1/notifications/me?page=${currentPage}`, undefined, true)
    if (isError) {
      return
    }
    this.notificationPage = data
    this.pageInfo = {
      page: data.page,
      totalPages: data.totalPages,
    }
    await this.readNotifications()
  },
  methods: {
    timeAgoFormat,
    async clickNotification(notification) {
      const type = notification?.data?.type
      if (type === 'comment' || type === 'commentLike') {
        const {articleId, commentId} = notification?.data
        const {isError, data} = await this.$boardApi('GET', `/api/v2/comments/${commentId}/page`, undefined, true)
        if (isError) {
          alert(data?.message)
          return
        }
        this.$router.push(`/articles/${articleId}?comment-page=${data}#comment-${commentId}`)
      } else if (type === 'articleLike') {
        const {articleId} = notification?.data
        this.$router.push(`/articles/${articleId}`)
      }
    },
    async readNotifications() {
      await this.$boardApi('PUT', '/api/v1/notifications/me/read', undefined, true)
    },
    clickPage(page) {
      this.$router.push(`/notifications?page=${page}`)
    }
  }
}

</script>

<style scoped>
.notifications-title {
  font-size: 1.5rem;
  font-weight: 700;
  padding: 15px;
  cursor: pointer;
}

.notification-item {
  display: flex;
  flex-direction: column;
  cursor: pointer;
}

.notification-title {
  font-size: 12px;
  color: rgb(0 0 0 / 40%);
}

.notification-content {
  overflow: hidden;
  text-align: left;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 90%;
}

.read {
  opacity: 50%;
}
</style>

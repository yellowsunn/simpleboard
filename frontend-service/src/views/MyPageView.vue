<template>
    <div class="mx-auto" style="width: 950px" v-if="isLoaded">
        <MyPageHeader></MyPageHeader>
        <div class="d-flex m-3 fs-5 fw-bold">회원 정보 변경</div>
        <div class="my-4">
            <img class="rounded-circle user-thumbnail"
                 alt="thumbnail"
                 @click="$refs.fileInput.click()"
                 :src="userInfo?.thumbnail"
                 :width="thumbnailWidth"
                 :height="thumbnailHeight"
                 referrerpolicy="no-referrer"/>
            <input ref="fileInput" type="file" accept="image/*" @change="handleThumbnailChange" v-show="false">
        </div>
        <div class="input-edit-group mx-auto">
            <div class="form-floating mb-2">
                <input type="email" class="form-control" :value="userInfo?.email" placeholder="test@example.com"
                       disabled
                       readonly>
                <label>이메일</label>
            </div>
            <div class="form-floating mb-2">
                <input type="text" v-model="nickName" class="form-control input-nickname"
                       placeholder="password">
                <label>닉네임</label>
            </div>
            <button type="button" class="btn btn-secondary btn-lg mb-1 w-100" @click="handleUserInfoChange">정보 수정
            </button>
            <div class="d-flex justify-content-end mt-5">
                <button type="button" class="btn btn-danger btn-lg">회원탈퇴</button>
            </div>
            <Transition>
                <div class="alert alert-success px-5" role="alert" v-if="isEditFinished">
                    정보 수정이 완료되었습니다.
                </div>
            </Transition>
        </div>
    </div>

</template>

<script>
import MyPageHeader from "@/components/mypage/MyPageHeader.vue";

export default {
  name: "MyPageView",
  components: {MyPageHeader},
  async created() {
    const {email, nickName, thumbnail, providers} = await this.$boardApi('GET', '/api/users/my-info', null, true)
    this.userInfo = {
      email, nickName, thumbnail, providers
    }
    this.nickName = nickName
    this.isLoaded = true
  },
  data() {
    return {
      isLoaded: false,
      isEditFinished: false,
      thumbnailWidth: 80,
      thumbnailHeight: 80,
      userInfo: {
        email: '',
        thumbnail: '',
        providers: []
      },
      nickName: '',
    }
  },
  computed: {
    thumbnailUrl() {
      if (!this.thumbnail) {
        return ''
      }
      return `${this.thumbnail}?width=${this.thumbnailWidth}&height=${this.thumbnailHeight}`
    }
  },
  methods: {
    async handleThumbnailChange(e) {
      if (!e?.target?.files[0]) {
        alert('파일을 찾을 수 없습니다.')
        return
      }
      const formData = new FormData()
      formData.append('thumbnail', e.target.files[0])
      const data = await this.$boardApi('PATCH', '/api/users/my-info/thumbnail', formData, true, {
        'Content-Type': 'multipart/form-data',
      })
      if (!data?.code) {
        this.userInfo = {
          ...this.userInfo,
          thumbnail: data || ''
        }
      }
    },
    async handleUserInfoChange() {
      if (this.userInfo?.nickName === this.nickName) {
        return
      }
      const response = await this.$boardApi('PUT', '/api/users/my-info', {
        nickName: this.nickName,
      }, true)
      if (response?.code) {
        alert(response?.message)
        this.nickName = this.userInfo?.nickName
        return
      }

      if (response === true) {
        this.userInfo = {
          ...this.userInfo,
          nickName: this.nickName,
        }
        setTimeout(() => { this.isEditFinished = false}, 1500)
      }
      this.isEditFinished = true
    },
  }
}
</script>

<style scoped>
.user-thumbnail {
    cursor: pointer;
}

.input-edit-group {
    width: 360px;
    display: flex;
    flex-direction: column;
}

.input-nickname {
    background-color: #f9f9f9;
}

.input-nickname:focus {
    background-color: #fff;
}

.alert-success {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1;
}

.v-enter-active,
.v-leave-active {
    transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
}
</style>

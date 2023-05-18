<template>
    <div class="navbar-wrap mx-auto">
<!--        <div class="vr"></div>-->
        <swiper :slidesPerView="6"
                :modules="modules"
                class="my-swiper"
        >
            <swiper-slide class="btn mx-2">공지사항</swiper-slide>
            <swiper-slide class="btn">자유게시판</swiper-slide>
            <swiper-slide class="btn">익명</swiper-slide>
        </swiper>
    </div>
</template>

<script>
import {FreeMode} from 'swiper'
import {Swiper, SwiperSlide} from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/free-mode';

export default {
  name: "NavBar",
  components: {
    Swiper,
    SwiperSlide,
  },
  setup() {
    return {
      modules: [FreeMode],
    }
  }
}
</script>

<style scoped>
.navbar-wrap {
    display: flex;
    width: 950px;
}

.my-swiper {
    width: 100%;
}
.swiper-slide {

}
</style>

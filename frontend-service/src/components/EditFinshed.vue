<template>
    <Transition>
        <div class="alert alert-success px-5" role="alert" v-if="editFinished">
            수정이 완료되었습니다.
        </div>
    </Transition>
</template>

<script>
export default {
  name: "EditFinished",
  computed: {
    editFinished() {
      return this.$store.state.isEditFinished
    }
  }
}
</script>

<style scoped>
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

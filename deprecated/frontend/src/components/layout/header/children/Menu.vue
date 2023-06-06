<template>
  <ul class="menu">
    <li class="item"><router-link to="/">HOME</router-link></li>
    <li class="item"><router-link to="/posts">FORUM</router-link></li>
    <li class="item"><router-link to="/users/myinfo">USER INFO</router-link></li>
  </ul>
</template>

<script>
export default {
};
</script>

<style lang="scss" scoped>
a {
  color: white;
  text-decoration: none;
}

ul {
  margin: 0;
}

.menu {
  .item {
    a {
      display: block;
      &:hover {
        background: #50555f;
      }
    }
    & > a {
      font-size: 0.875rem;
      padding: 0.643em 1.071em;
      border-radius: 0.286em;
    }
  }
}

@media screen and (max-width: 48rem) {
  .menu {
    .item {
      text-align: center;
      letter-spacing: 0.188em;
      width: 100%;
      &:first-child {
        margin-top: 0.625em;
      }
    }
  }
}
</style>
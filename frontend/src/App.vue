<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
}
</script>

<style>
:root {
  --vue-color: #41B883;
  --primary-color: #f1f8e9;
  --primary-color-dark: #bec5b7;
  --primary-color-light: #ffffff;
  --header-color: #282c35;
  --button-color: #1a72e6;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

ul {
  list-style: none;
}

button,
button:focus {
  border: none;
  cursor: pointer;
  outline: none;
  background-color: transparent;
}
</style>

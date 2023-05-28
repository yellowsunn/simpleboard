import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import mixins from "@/mixins";
import store from "@/store";
import {library} from '@fortawesome/fontawesome-svg-core'
import {faBell, faEye, faHeart, faThumbsUp, faImage} from '@fortawesome/free-regular-svg-icons'
import {faThumbsUp as fasThumbsUp, faCircleXmark as fasCircleXmark} from '@fortawesome/free-solid-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import TimeAgo from "javascript-time-ago";
import ko from "javascript-time-ago/locale/ko";

TimeAgo.addDefaultLocale(ko)
library.add(faBell, faEye, faThumbsUp, faHeart, fasThumbsUp, fasCircleXmark, faImage)

createApp(App)
  .mixin(mixins)
  .use(router)
  .use(store)
  .component('font-awesome-icon', FontAwesomeIcon)
  .mount('#app')

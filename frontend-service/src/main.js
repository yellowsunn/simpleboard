import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import mixins from "@/mixins";
import store from "@/store";
import {library} from '@fortawesome/fontawesome-svg-core'
import {faBell} from '@fortawesome/free-regular-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(faBell)

createApp(App)
  .mixin(mixins)
  .use(router)
  .use(store)
  .component('font-awesome-icon', FontAwesomeIcon)
  .mount('#app')

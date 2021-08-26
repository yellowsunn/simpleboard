import Vue from 'vue';
import App from './App.vue';
import BootstrapVue from 'bootstrap-vue';
import VueDebounce from 'vue-debounce';
import InfiniteLoading from 'vue-infinite-loading';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import { router } from './router/index';
import { store } from './store/index';
import ContentEditable from 'vue-contenteditable';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(VueDebounce);
Vue.use(InfiniteLoading);
Vue.use(ContentEditable);

new Vue({
  render: h => h(App),
  router,
  store,
}).$mount('#app')

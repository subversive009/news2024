// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vant from 'vant';
import 'vant/lib/index.css';
import './assets/css/index.css';
import axios from 'axios'
import Vueaxios from 'vue-axios'

Vue.use(Vant);
Vue.use(Vueaxios,axios)
axios.defaults.baseURL='http://localhost:8080/';
Vue.prototype.$picIp = 'http://localhost:8080/pic/'
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

Vue.filter("dateFilter",function(dateStr){
  var dt=new Date(dateStr);
  var year=dt.getFullYear();
  var month=dt.getMonth()+1;
  var day=dt.getDate();
  var hour=dt.getHours();
  var minute=dt.getMinutes();
  var sec=dt.getSeconds();
  return `${year}年${month}月${day}日${hour}:${minute}:${sec}`
});
import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Mine from '@/components/Mine'
import Regist from '@/components/Regist'
import Kind from '@/components/Kind'
import Search from '@/components/Search'
import SearchResult from '@/components/SearchResult'
import Detail from '@/components/Detail'
import Video from '@/components/Video'
import Comments from '@/components/Comments'
import AuthorInfo from '@/components/AuthorInfo'
import Chat from '@/components/Chat'
import Follow from '@/components/Follow'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/Mine',
      name: 'Mine',
      component: Mine
    },
    {
      path: '/Follow',
      name: 'Follow',
      component: Follow
    },
    {
      path: '/AuthorInfo',
      name: 'AuthorInfo',
      component: AuthorInfo
    },
    {
      path: '/Chat',
      name: 'Chat',
      component: Chat
    },
    {
      path: '/Regist',
      name: 'Regist',
      component: Regist
    },
    {
      path: '/Kind',
      name: 'Kind',
      component: Kind
    },
    {
      path: '/Search',
      name: 'Search',
      component: Search
    },
    {
      path: '/SearchResult/:value',
      name: 'SearchResult',
      component: SearchResult
    },
    {
      path: '/Detail/:value',
      name: 'Detail',
      component: Detail
    },
    {
      path: '/Video',
      name: 'Video',
      component: Video
    },
    {
      path: '/Comments',
      name: 'Comments',
      component: Comments
    },
  ]
})

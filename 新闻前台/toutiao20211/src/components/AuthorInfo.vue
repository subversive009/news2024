<template>
    <div class="hello">
      <div class="nav">
          <span @click="goBack"><van-icon name="arrow-left"/>返回</span>
      </div>
          <div class="top">
              <div class="top-left">
                  <van-image :src="$picIp+author.image" lazy-load height="70" width="70"/>
                  <div class="user">{{author.userName}} </div>
              </div>
              <div class="top-right">
                  <van-button color="#ee0a24" plain size="small" @click="follow">{{followUserLabel}}</van-button>
                   <van-button color="#ee0a24" plain size="small" @click="chat" v-if="followUserLabel=='已关注'">私聊</van-button>
              </div>
          </div>
          <van-tabs v-model="active">
              <van-tab title="全部">
              </van-tab>
               <van-tab title="文章">
              </van-tab>
               <van-tab title="视频">
              </van-tab>
               <van-tab title="问答">
              </van-tab>
          </van-tabs>
         <div class="list">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
    >
        <div class="card" v-for="(news,index) in list" :key="index" @click="getDetail(news.newsId)">
            <div class="card-left">
                <div class="title">
                    {{news.title}}
                </div>
                <div class="time">
                    <span>{{news.time|dateFilter}} </span>
                    <span>{{news.from1}} </span>
                </div>
            </div>
            <div class="card-right">
                <img :src="$picIp+news.pictures" >
            </div>
        </div>
        </van-list>
    </van-pull-refresh>
      </div>
    </div>
</template>

<script>
import Vue from 'vue';
import { ImagePreview, Toast } from 'vant';
import { Lazyload } from 'vant';

Vue.use(Lazyload);
Vue.use(ImagePreview);
Vue.use(Toast);
export default {
  name: 'Detail',
  data () {
    return {
        author:this.$route.query,
        list:[],
        loading: false,
        finished: false,
        refreshing: false,
        pageNo:1,
        active:1,
        followUserLabel:'关注',
        user:JSON.parse(localStorage.getItem("userInfo")),
    }
  },
  created(){
        this.isFollow();
    },
  methods:{
      chat(){
          this.$router.push({
              name:'Chat',
              query:{
                  uid:this.author.uid,
                  userName:this.author.userName,
                  image:this.author.image
              }
          });
      },
       getDetail(newsId){
          this.$router.push("/Detail/"+newsId);
      },
      onLoad(){
          //上拉加载,获取更多数据
          if(this.refreshing){
              this.list=[]//当前是下拉刷新,list置为空
              this.refreshing=false;
          }
          Vue.axios.get("news/getByUid?current="+this.pageNo+"&uid="+this.author.uid).then((response)=>{
             // console.log(response.data);//输出服务器响应数据
              this.list=this.list.concat(response.data.records);//将新获取的列表数据添加到原列表中
              this.loading=false;
              //判断是否是最后一页
              if(this.pageNo>=response.data.Pages){
                    this.finished=true;
              }
              this.pageNo++;
          })
      },
      onRefresh(){
          //下拉刷新,重新获取新的数据
          this.finished=false;
          //重新加载数据
          //将loading设置为true,表示处于加载状态
          this.loading=true;
          this.pageNo=1;
          this.onLoad();
      },
      follow(){
          if(this.user==null){
            Toast("您未登录,请先登录后再关注该作者!");
            return;
          }
          let userId=this.user.uid;
          let followUserId=this.author.uid;
          if(this.followUserLabel=='已关注'){
              this.axios.get("news/followUser/disFollowUser?userId="+userId+"&followUserId="+followUserId).then(response=>{
                  this.followUserLabel='关注'
              });
          }else{
              this.axios.get("news/followUser/followUser?userId="+userId+"&followUserId="+followUserId).then(response=>{
                  this.followUserLabel='已关注'
              });
          }
          
      },
      isFollow(){
          if(this.user==null){
              return;
          }
          let userId=this.user.uid;
          let followUserId=this.author.uid;
           this.axios.get("news/followUser/isFollowUser?userId="+userId+"&followUserId="+followUserId).then(response=>{
               let result=response.data;
               if(result==true){
                    this.followUserLabel='已关注'
               }
        });
      },
      goBack(){
          this.$router.go(-1);//返回上一次路由
      },
      }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.news{
    text-align: left;
    padding: 5px;
}
.n-title{
    font-size: 26px;
}
.top-right{
    line-height: 50px;
}
.n-img img,video{
    height: 200px;
    width: 100%;
}
.n-content{
    line-height: 2;
    margin-bottom: 90px;
}
.comment{
    display: flex;
}
.comment-left{
    margin-right: 10px;
}
.comment-user{
    font-weight:900;
    font-size: 14px;
    display: flex;
    justify-content: space-between;
}
.comment-right{
    width: 100%;
}
.comment-time{
    color: #777;
    font-size: 14px;
}
.user-right{
    font-weight: 500 !important;
}
.user-right span{
    margin-right: 5px;
}
.comment-input{
    border-top: 0.5px solid #ddd;
    position: fixed;
    left: 0;
    bottom: 45px;
    width: 100%;
}
</style>

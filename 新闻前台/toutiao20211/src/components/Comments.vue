<template>
  <div class="hello">
    <div class="nav">
      <span @click="goBack">
        <van-icon name="arrow-left" />返回
      </span>
    </div>
    <div class="top">
      <div class="top-left">
        <van-image :src="$picIp+user.image" height="70" width="70" />
        <div class="user">{{user.username}}</div>
      </div>
      <div class="top-right">
        <div class="geren">
          个人主页
          <van-icon name="arrow" size="14" />
        </div>
      </div>
    </div>
    <van-tabs v-model="active">
      <van-tab title="收藏"></van-tab>
      <van-tab title="评论">
        <div class="list">
          <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
              <div v-for="(item,index) in list" :key="index">
                <van-swipe-cell>
                  <div class="comment-card">
                    <div class="title1">{{item.content}}</div>
                    <div class="title">{{item.time|dateFilter}}</div>
                    <div class="news-card" @click="getDetail(item.news.newsid)">
                      <div class="top">
                        <div class="top-left">
                          <van-image :src="$picIp+item.news.image" height="50" width="50" />
                          <div class="user">{{item.news.username}}</div>
                        </div>
                        <div class="top-right"></div>
                      </div>
                      <div class="card">
                        <div class="card-left">
                          <div class="title">{{item.news.title}}</div>
                          <div class="title">{{item.news.from1}}</div>
                          <div class="title">{{item.news.time|dateFilter}}</div>
                        </div>
                        <div class="card-right">
                          <img :src="$picIp+item.news.pictures" />
                        </div>
                      </div>
                      <div class="share">
                        <van-row>
                          <van-col span="8">
                            <van-icon name="share-o" />分享
                          </van-col>
                          <van-col span="8">
                            <van-icon name="chat-o" />评论
                          </van-col>
                          <van-col span="8">
                            <van-icon name="good-job-o" />赞
                          </van-col>
                        </van-row>
                      </div>
                    </div>
                  </div>
                  <template #right>
                    <van-button
                      square
                      text="删除"
                      type="danger"
                      class="delete-button"
                      @click="removeById(index,item.commentid)"
                    />
                  </template>
                </van-swipe-cell>
              </div>
            </van-list>
          </van-pull-refresh>
        </div>
      </van-tab>
      <van-tab title="点赞"></van-tab>
      <van-tab title="历史"></van-tab>
    </van-tabs>
  </div>
</template>

<script>
import Vue from "vue";
import { ImagePreview, Toast } from "vant";

Vue.use(ImagePreview);
export default {
  name: "Mine",
  data() {
    return {
      value: this.$route.params.value,
      list:[],
      loading: false,
      finished: false,
      refreshing: false,
      pageNo:1,
      user: JSON.parse(localStorage.getItem("userInfo")),
      active:1
    }
  },
  created() {
   
  },
  methods: {
    removeById(index,commentid){
        console.log(commentid);
        this.axios.post("news/comment/remove",{commentid:commentid}).then(response=>{
            Toast("删除成功");
            this.list.splice(index,1);
        })
    },
    goBack() {
      this.$router.go(-1); //返回上一次路由
    },
    getDetail(newsid){
        this.$router.push("/Detail/"+newsid);
    },
    goSearch(){
        this.$router.push("/Search");
    },
    onLoad(){
          if(this.user==null){
            Toast('您未登录！请登陆后查看评论列表!');
            this.$router.push("/Mine");
            return;
          }
          //上拉加载,获取更多的数据
          if(this.refershing){
            this.list=[];//当前是下拉刷新,list置为空
            this.refershing=false;
          }
          this.axios.post("news/comment/getByUid",{
            pageNum:this.pageNo,
            uid:this.user.uid
          }).then((response)=>{
            this.list=this.list.concat(response.data.list);
            this.loading=false;
            if(this.pageNo>=response.data.pages){
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
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3{
    text-align: left;
    margin: 0 5px;
}
.delete-button{
    height: 100%;
}
.list{
    margin-top: 15px;
}
.comment-card{
    background-color: #fff;
}
.comment-card .title1{
    text-align: left;
    padding: 0 10px;
    margin-bottom: 3px;
}
.comment-card .news-card{
    border: 0.5px solid #eee;
    margin: 10px 0;
}
.comment-card .card .title{
    font-size: 15px;
}
.news-card .news-user .van-image__img{
    height: 100%;
    width: 100%;
}
.news-card .top-left .user{
    font-size: 16px;
    font-weight: 500;
}
.share{
    height: 30px;
    line-height: 30px;
}
</style>

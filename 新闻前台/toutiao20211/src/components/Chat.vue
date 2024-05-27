<template>
    <div class="hello">
      <div class="nav">
          <span @click="goBack"><van-icon name="arrow-left"/>{{author.userName}}</span>
      </div>
      <div class="comment-input">
          <van-field
          v-model="content"
          clearable
          placeholder="请输入评论内容"
          @keyup.enter="send"
          >
          <template #button>
              <van-button size="small" type="warning" @click="send">发送</van-button>
          </template>
          </van-field>
      </div>
         <div class="list">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
    >
        <div class="chatlist" v-for="item in list" :key="item.cid">
            <div class="time">
                {{item.date|dateFilter}}
            </div>
            <div class="chat" :class="{self:item.fromUser==user.username}">
                <van-image :src="item.fromUser==user.username?$picIp+user.image:$picIp+author.image" lazy-load height="60" width="60" round/>
                <div class="content">
                {{item.content}}
                </div>
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
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

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
        user:JSON.parse(localStorage.getItem("userInfo")),
        content:'',
        stompClient:null
    }
  },
  created(){
      this.connect();
    },
  methods:{
      connect(){
          var socket=new SockJS('http://localhost:8080/ws/ep')
          this.stompClient=Stomp.over(socket)
          this.stompClient.connect({},success=>{
              //连接成功
              //订阅消息
              this.stompClient.subscribe("/user/"+this.user.username+"/queue/chat",response=>{
                //当有发送给this.user.username的消息时,response代表服务器发送的消息内容
                var msg=JSON.parse(response.body);
                this.list.push(msg)//消息添加到页面中
              })
          })
      },
      send(){
          var msg={
              fromUser:this.user.username,
              toUser:this.author.userName,
              content:this.content,
              date:new Date()
          }
          this.stompClient.send("/chat",{},JSON.stringify(msg))
          this.list.push(msg)
          this.content=''
      },
      onLoad(){
          //上拉加载,获取更多数据
          if(this.refreshing){
              this.list=[]//当前是下拉刷新,list置为空
              this.refreshing=false;
          }
          if(this.user==null){
              Toast("您未登录,请登录后再聊天!");
              return;
          }
          Vue.axios.get("news/chatmsg/list?current="+this.pageNo+"&fromUser="+this.user.username+"&toUser="+this.author.userName).then((response)=>{
             // console.log(response.data);//输出服务器响应数据
              this.list=this.list.concat(response.data.records);//将新获取的列表数据添加到原列表中
              this.loading=false;
              //判断是否是最后一页
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
      goBack(){
          this.$router.go(-1);//返回上一次路由
      },
      }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.chat{
    display: flex;
    margin: 5px 0;
}
.chatlist .time{
    font-size: 12px;
    margin: 15px 0;
    text-align: center; 
}
.chat .content{
    height: 50px;
    line-height: 50px;
    padding: 5px 10px;
    border: 0.5px solid #ddd;
    border-radius: 5px;
    margin: 0 10px;
}
.self{
    flex-direction: row-reverse;
}
.self .content{
    background-color: rgb(214, 218, 230);
}
.list{
    margin-bottom: 55px;
}
</style>

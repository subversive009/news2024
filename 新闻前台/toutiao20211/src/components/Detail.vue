<template>
    <div class="hello">
      <div class="nav">
          <span @click="goBack"><van-icon name="arrow-left"/>返回</span>
      </div>
      <div class="news">
          <div class="n-title">
              {{news.title}}
          </div>
          <div class="top">
              <div class="top-left" @click="goAuthorInfo">
                  <van-image :src="$picIp+news.image" lazy-load height="60" width="60"/>
                  <div class="user">{{news.userName}} </div>
              </div>
              <div class="top-right">
                  <van-button color="#ee0a24" plain size="small" @click="follow">{{followUserLabel}}</van-button>
              </div>
          </div>
          <div>
              {{news.from1}}&nbsp;{{news.time|dateFilter}}
          </div>
          <div class="n-img" v-if="news.type==1">
              <img :src="$picIp+news.pictures" alt="">
          </div>
          <div class="n-img" v-if="news.type==3">
              <video :src="$picIp+news.video" controls></video>
              <div @click="download">
                  <van-icon name="down" size="24"/>下载视频
              </div>
          </div>
          <div v-if="news.type==2" @click="preview">
              <van-grid :border="false" :column-num="3" :gutter="1">
                  <van-grid-item v-for="(item,index) in news.picList" :key="index">
                      <van-image :src="$picIp+item.pic"/>
                  </van-grid-item>
              </van-grid>
          </div>
          <div class="n-content">
              {{news.content}}
             <!-- 新闻内容 -->
            <div class="summary-button-container">
            <button class="summary-button" @click="generateSummary(news.newsid)">总结文章</button>
            </div>
            <div class="dialog" :style="{display: showDialog ? 'block' : 'none'}">
              <div class="dialog-content">
                <div class="bubble">
                  {{summary.substring(0, summaryLength)}}
                </div>
                <button  class="dialog-button" @click="showDialog = false">x</button>  <!-- 将关闭按钮的文本改为 "x" -->
                <van-field
                  class="dialog-input"
                  v-model="dialogInput"
                  clearable
                  placeholder="请输入文字"
                  @keyup.enter="sendDialogInput"
                >
                  <template #button>
                    <van-button size="small" type="warning" @click="sendDialogInput">发送</van-button>
                  </template>
                </van-field>
              </div>
            </div>
              <h3>评论</h3>
              <div class="comment" v-for="item in commentList" :key="item.commentId">
                  <div class="comment-left">
                    <img :src="$picIp+item.image">
                  </div>
                  <div class="comment-right">
                      <div class="comment-user">
                          <div class="user-left">{{item.userName}}</div>
                          <div class="user-right">
                              <span>12</span><van-icon name="good-job-o" size="18"/>
                          </div>
                      </div>
                      <div class="comment-content">
                          {{item.content}}
                      </div>
                      <div class="comment-time">
                          {{item.time|dateFilter}}
                      </div>
                  </div>
              </div>
              <div class="comment-input">
                <van-field
                    v-model="content"
                    clearable
                    placeholder="请输入评论内容"
                    @keyup.enter="sendComment"
                >
                    <template #button>
                        <van-button size="small" type="warning" @click="sendComment">发送</van-button>
                    </template>
                </van-field>
              </div>
          </div>
      </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { ImagePreview, Toast } from 'vant';

Vue.use(ImagePreview);
export default {
  name: 'Detail',
  data () {
    return {
        newsId:this.$route.params.value,
        news:{},
        commentList:[],
        content:"",
        user:JSON.parse(localStorage.getItem("userInfo")),
        followUserLabel:'关注',
        summary: '',
        showDialog: false,
        summaryLength: 0,  // 新增一个变量来存储当前显示的总结的长度
        dialogInput: '',
    }
  },
  created(){
    this.axios.get("news/getById?newsId="+this.newsId).then(response=>{
        this.news=response.data;
        this.isFollow();
    });
    this.getCommentList();
  },
  methods:{
      generateSummary() {
        this.showDialog = true;
        this.axios.post('news/generateWithGPT3?newsId='+this.newsId, {
        }).then(response=>{
          this.summary = response.data;
          this.summaryLength = 0;  // 重置当前显示的总结的长度
          const intervalId = setInterval(() => {
            this.summaryLength += 1;  // 每隔一定的时间就增加显示的总结的长度
            if (this.summaryLength >= this.summary.length) {
              clearInterval(intervalId);  // 当显示的总结的长度达到总结的长度时，停止定时器
            }
          }, 100);  // 这里的 100 是每隔 100 毫秒增加显示的总结的长度，你可以根据需要调整这个值
        })
      },
      sendDialogInput() {
      console.log(this.dialogInput);  // 打印对话框输入框的内容
      this.dialogInput = '';  // 清空对话框输入框的内容
      },
      goAuthorInfo(){
          this.$router.push({
              name:'AuthorInfo',
              query:{
                  uid:this.news.uid,
                  userName:this.news.userName,
                  image:this.news.image
              }
          });
      },
      follow(){
          if(this.user==null){
            Toast("您未登录,请先登录后再关注该作者!");
            return;
          }
          let userId=this.user.uid;
          let followUserId=this.news.uid;
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
          let followUserId=this.news.uid;
           this.axios.get("news/followUser/isFollowUser?userId="+userId+"&followUserId="+followUserId).then(response=>{
               let result=response.data;
               if(result==true){
                    this.followUserLabel='已关注'
               }
        });
      },
      download(){
          this.axios.get("news/download?filename="+this.news.video,{responseType:"blob"}).then(response=>{
              var url=window.URL.createObjectURL(new Blob([response.data]));//根据文件创建url
              var link=document.createElement("a")//创建a超链接
              link.href=url;
              link.style.display='none'//将超链接不可见
              const fileName=this.news.video;
              link.setAttribute("download",fileName)//保存的文件名
              document.body.appendChild(link)//将a添加页面中
              link.click()//点击超链接,出现另存为对话框
              window.URL.revokeObjectURL(url)
              document.body.removeChild(link)//将url和a资源释放掉
          })
      },
      sendComment(){
        if(this.user==null){
            Toast('您未登录!请登录后发表评论!');
            this.$router.push("/Mine");
            return;
        }
        if(this.content.trim()==""){
            Toast('您输入的内容!请输入评论内容!');
            return;
        }
        this.axios.post("news/comment/add",{
            uid:this.user.uid,
            content:this.content,
            newsId:this.newsId
        }).then(response=>{
            this.content="";
            this.getCommentList();
        })
      },
      getCommentList(){
          this.axios.get("news/comment/getByNewsid?newsId="+this.newsId).then(response=>{
              this.commentList=response.data;
          });
      },
      goBack(){
          this.$router.go(-1);//返回上一次路由
      },
      preview(){
          var arr=[];
          this.news.picList.forEach(item=>{
              arr.push(this.$picIp+item.pic)
          })
          ImagePreview(arr);
      },
      }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 40px;
  background-color: red;
  color: white;
  z-index: 1000;
}
.news{
    margin-top: 40px;
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
.summary-button-container {
  position: sticky;
  top: 0;
  z-index: 1000;
}
.summary-button {
  background-color: #ee0a24; /* 按钮背景颜色 */
  color: #fff; /* 按钮文字颜色 */
  border: none; /* 移除边框 */
  padding: 10px 20px; /* 按钮内边距 */
  text-align: center; /* 文字居中 */
  text-decoration: none; /* 移除下划线 */
  display: inline-block; /* 使其成为行内元素 */
  font-size: 16px; /* 文字大小 */
  margin: 4px 2px; /* 外边距 */
  cursor: pointer; /* 鼠标悬停时变为手指图标 */
}

.dialog {
  display: none; /* 默认隐藏对话框 */
  position: fixed; /* 固定位置 */
  z-index: 1; /* 在顶层 */
  left: 0;
  top: 0;
  width: 100%; /* 宽度设置为全屏 */
  height: 100%; /* 高度设置为全屏 */
  overflow: auto; /* 如果需要的话，启用滚动 */
  background-color: rgb(0,0,0); /* 黑色背景 */
  background-color: rgba(0,0,0,0.4); /* 黑色背景，有一点透明 */
}

.dialog-content {
  position: relative;
  padding-bottom: 50px;
  background-color: #fefefe;
  margin: 15% auto; /* 15% 从顶部和居中 */
  padding: 20px;
  border: 1px solid #888;
  width: 80%; /* 对话框宽度 */
}
.bubble {
  background-color: #f0f0f0;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
}

.dialog-input {
  position: absolute;
  bottom: -50px;  /* 将输入框放在 .dialog-content 的底部之下 */
  height: 50px;
  left: 0;
  width: 100%;
  background-color: #fff;
}

.dialog-button {
  position: absolute;
  top: 5px;  /* 将关闭按钮放在 .dialog-content 的右上角 */
  right: 5px;
  background-color: #f44336;  /* 设置背景颜色为红色 */
  color: white;  /* 设置文字颜色为白色 */
  border: none;  /* 移除边框 */
  font-size: 20px;  /* 增大字体大小 */
  cursor: pointer;  /* 鼠标悬停时变为手指图标 */
  width: 15px;  /* 设置宽度 */
  height: 15px;  /* 设置高度 */
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

<template>
<div class="hello">
    <div class="fixed">
    <van-search
    v-model="value"
    shape="round"
    background="#ee0a24"
    placeholder="请输入搜索关键词"
    @click="goSearch"
    />
    <van-tabs @click="getNewsListByKind">
        <van-tab v-for="kind in kindList" :title="kind.content" :key="kind.kindId">
        </van-tab>
    </van-tabs>
    </div>
    <div class="list kind">
        <div class="video" v-for="(news,index) in list" :key="index" @click="getDetail(news.newsId)">
            <div class="video-top">
                <div class="video-title">{{news.title}}</div>
                <video :src="$picIp+news.video" controls></video>
            </div>
            <div class="video-button">
                <div class="top">
                    <div class="top-left">
                        <van-image :src="$picIp+news.image" lazy-load height="60" width="60"/>
                        <div class="user">
                            <span>{{news.username}}</span>
                            <span class="focus">关注</span>
                        </div>
                    </div>
                    <div class="top-right">
                        <van-icon name="chat-o" size="20"/>
                        <span>53</span>
                        <van-icon name="ellipsis" size="20"></van-icon>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
export default {
  name: 'Kind',
  data () {
    return {
        kindList:[],
        value:'',
        list:[],
        kindId:1
    }
  },
  created(){
    this.axios.get("news/kind/list").then((response)=>{
        this.kindList=response.data;
    })
    this.getNewsListByKind(0,'');
  },
  methods:{
      getDetail(newsId){
          this.$router.push("/Detail/"+newsId);
      },
      goSearch(){
        this.$router.push("/Search");
      },
     getNewsListByKind(name,title){
         console.log(name+","+title)
         this.axios.get("news/getVideosByKindId?KindId="+(name+1)).then((response)=>{
             this.list=response.data;
         })
     }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.kind{
    margin: 95px 0 45px 0;
}
.video video{
    height: 260px;
    width: 100%;
}
.video{
    background-color: #fff;
    margin: 2px 0;
    position: relative;
}
.video-title{
    position: absolute;
    top: 20px;
    left: 5px;
    font-size: 16px;
    color: #777;
}
.top{
    padding: 0;
    line-height: 60px;
}
.top-left span{
    font-weight: 500;
    font-size: 16px;
}
.top-left .focus{
    color: #f00;
}
.top-left .user span{
    margin-right: 5px;
}
.top-right{
    margin-right: 10px;
}
.top-right span{
    margin-right: 10px;
}
</style>

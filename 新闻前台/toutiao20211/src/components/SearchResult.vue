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
    </div>
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
import { Lazyload } from 'vant';

Vue.use(Lazyload);
export default {
  name: 'Mine',
  data () {
    return {
        value:this.$route.params.value,
        list:[],
        loading:false,
        finished:false,
        refreshing:false,
        pageNo:1
    }
  },
  created(){

  },
  methods:{
      getDetail(newsId){
          this.$router.push("/Detail/"+newsId);
      },
      goSearch(){
        this.$router.push("/Search");
      },
      onLoad(){
          //上拉加载,获取更多数据
          if(this.refreshing){
              this.list=[]//当前是下拉刷新,list置为空
              this.refreshing=false;
          }
          Vue.axios.post("news/getPageNewsByTitle",{
              current:this.pageNo,
              title:this.value
          }).then((response)=>{
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
  }
}
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.list{
    margin-top: 50px;
}
</style>

<template>
    <div class="hello">
        <div class="fixed">
            <van-search 
                v-model="value"
                shape="round"
                background="#ee0a24"
                placeholder="请输入搜索关键字"
                @click="goSearch"
            />
            <van-tabs @click="getNewsListByKind">
                <van-tab v-for="kind in kindList" :title="kind.content" :key="kind.kindid">
                </van-tab>
            </van-tabs>
        </div>
        <div class="list kind">
            <div class="card" v-for="(news, index) in list" :key="index" @click="getDetail(news.newsId)">
                <div class="card-left">
                   <div class="title">
                       {{news.title}}
                    </div> 
                    <div class="time">
                        <span>{{news.time|dateFilter}}</span>
                        <span>{{news.from1}}</span>
                    </div>
                </div>
                <div class="card-right">
                    <img :src="$picIp+news.pictures">
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { Search } from 'vant';

export default {
    name: 'Kind',
    components: {
        'van-search': Search
    },
    data(){
        return {
            kindList:[],
            value: '',
            list:[],
            kindid:1
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
            this.axios.get("news/getByKindId?kindId="+(name+1)).then((response)=>{
                this.list=response.data;
            })
        }
    }
}
</script>

<style scoped>
.kind{
    margin-top: 95px;
}
</style>
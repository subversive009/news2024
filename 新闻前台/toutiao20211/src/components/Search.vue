<template>
  <div class="hello">
    <div class="fixed">
      <van-search
        v-model="value"
        shape="round"
        background="#ee0a24"
        placeholder="请输入搜索关键词"
        @search="onSearch"
      />
    </div>
    <div class="search">
      <div class="title">
        <div>搜索历史</div>
        <div class="del-btn">
          <van-icon name="delete-o" @click="clearHistory" />
        </div>
      </div>
      <van-row>
        <van-col
          span="12"
          v-for="(item,index) in list"
          :key="index"
          @click="goSearch(item)"
        >{{item}}</van-col>
      </van-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Mine',
  data() {
    return {
      value:'',
      list: []
    }
  },
  created() {
    var history = localStorage.getItem("history"); //将搜索记录从localStorage取出来
    if (history != null) {
      this.list = history.split(",");
    }
  },
  methods: {
    onSearch(val) {
      //console.log(val);
      //删除重复搜索记录
      for (var i = 0; i < this.list.length; i++) {
        if (this.list[i] == val) {
          this.list.splice(i, 1);
        }
      }
      this.list.splice(0, 0, val); //添加搜索记录
      localStorage.setItem("history", this.list.toString()); //将搜索记录存储在localStorage
      this.goSearch(val);
    },
    goSearch(item) {
      this.$router.push("/SearchResult/" + item);
    },
    clearHistory() {
      localStorage.removeItem("history");
      this.list = [];
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.search {
  padding: 5px;
  margin-top: 50px;
}
.title {
  display: flex;
  height: 30px;
  line-height: 30px;
  padding: 5px;
  justify-content: space-between;
}
.van-col {
  text-align: left;
  margin: 5px 0;
  border-right: 0.5px solid #eee;
  cursor: pointer;
}
</style>

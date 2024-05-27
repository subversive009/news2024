<template>
    <div class="hello">
        <div class="head">
            <div class="top">
                <div class="top-left">
                    <van-icon name="scan"  size="24"/>
                </div>
                <div class="top-right">
                    <van-icon name="envelop-o" size="24"/>
                    <van-icon name="setting-o" size="24"/>
                </div>
            </div>
            <div class="top">
                <div class="top-left">
                    <van-image :src="$picIp+user.image" height="60" width="60"/>
                  <div class="user">{{username}} </div>
                </div>
                <div class="top-right">
                    <div class="geren">
                        个人主页
                        <van-icon name="arrow" size="14"/>
                    </div>
                </div>
            </div>
            <div class="grid">
                <van-row>
                    <van-col span="6">
                        <div class="digit">
                            0
                        </div>
                        <div class="text">
                            头条    
                        </div>
                    </van-col>
                    <van-col span="6">
                        <div class="digit">
                            12
                        </div>
                        <div class="text">
                            获赞    
                        </div>
                    </van-col>
                    <van-col span="6">
                        <div class="digit">
                            3
                        </div>
                        <div class="text">
                            粉丝    
                        </div>
                    </van-col>
                    <van-col span="6" @click="follow">
                        <div class="digit">
                            32
                        </div>
                        <div class="text">
                            关注    
                        </div>
                    </van-col>
                </van-row>
            </div>
        </div>
            <div class="grid">
                <van-row>
                    <van-col span="6">
                        <div class="digit">
                            <van-icon name="bullhorn-o" size="24" color="#ee0a24"/>
                        </div>
                        <div class="text">
                            消息
                        </div>
                    </van-col>
                    <van-col span="6">
                        <div class="digit">
                            <van-icon name="star-o" size="24" color="#ee0a24"/>
                        </div>
                        <div class="text">
                            收藏
                        </div>
                    </van-col>
                    <van-col span="6">
                        <div class="digit">
                            <van-icon name="clock-o" size="24" color="#ee0a24"/>
                        </div>
                        <div class="text">
                            浏览历史
                        </div>
                    </van-col>
                    <van-col span="6">
                        <div class="digit">
                            <van-icon name="down" size="24" color="#ee0a24"/>
                        </div>
                        <div class="text">
                            下载管理
                        </div>
                    </van-col>
                </van-row>
                <div class="title">
                        更多服务
                </div>
                <van-grid>
                    <van-grid-item icon="edit" text="用户反馈"/>
                    <van-grid-item icon="balance-pay" text="钱包"/>
                    <van-grid-item icon="send-gift-o" text="广告"/>
                    <van-grid-item icon="fire-o" text="免流量"/>
                    <van-grid-item icon="coupon-o" text="评论" to="/Comments"/>
                    <van-grid-item icon="good-job-o" text="点赞"/>
                    <van-grid-item icon="closed-eye" text="夜间模式"/>
                    <van-grid-item icon="bulb-o" text="创作中心"/>
                    <van-grid-item icon="notes-o" text="订单"/>
                    <van-grid-item icon="cart-circle-o" text="购物车"/>
                    <van-grid-item icon="bag-o" text="圆梦公益"/>
                    <van-grid-item icon="revoke" text="退出登录" @click="logout"/>
                </van-grid>
            </div>
            <van-overlay :show="show" @click="show=false">
                <div class="wrapper" @click.stop>
                        <div class="block">
                            <h2>登录</h2>
                            <van-form @submit="onSubmit">
                                <van-field
                                    v-model="user.username"
                                    name="username"
                                    label="用户名"
                                    placeholder="用户名"
                                    clearable
                                    :rules="[{ required: true, message: '请填写用户名' }] "
                                />
                                <van-field
                                    v-model="user.password"
                                    type="password"
                                    name="password"
                                    label="密码"
                                    placeholder="密码"
                                    clearable
                                    :rules="[{ required: true, message: '请填写密码' }] "
                                />
                                <div style="margin: 16px;">
                                    <van-button round block type="info" native-type="submit">提交</van-button>
                                </div>
                            </van-form>
                            <div class="reg" @click="goRegist">
                                点击此处进行注册
                            </div>
                        </div>
                </div>
            </van-overlay>
        </div>
</template>

<script>
import Vue from 'vue';
import { Toast } from 'vant';

Vue.use(Toast);
export default {
  name: 'Mine',
  data () {
    return {
        show:true,
        user:{
            username:'',
            password:'',
            image:'default.png',
            uid:0
        },
        username:''
    }
  },
  created(){
     var user=JSON.parse(localStorage.getItem("userInfo"));
     console.log(user)
     if(user!=null){
         this.user=user;
         this.username=user.username;
         this.show=false;//登录框隐藏
     }
  },
  methods:{
        follow(){
            this.$router.push("/Follow");
        },
        onSubmit(values) {
            console.log('submit', values);
            this.axios.post("user/login",this.user).then(response=>{
                console.log(response.data);
                if(response.data!=null&&response.data!=''){
                    Toast('登录成功!');
                    //隐藏登录框
                    this.show=false;
                    this.user=response.data;
                    this.username=this.user.username;
                    localStorage.setItem("userInfo",JSON.stringify(this.user));
                }else{
                    Toast('用户名或密码错误,登录失败!');
                    return;
                }
            })
        },
        goRegist(){
            this.$router.push("/Regist");
        },
        logout(){
            this.show=true;
            this.user={
                username:'',
                password:'',
                image:'default.png',
                uid:0
            };
            this.username=this.user.username;
            localStorage.removeItem("userInfo");
        }
      }
  }
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

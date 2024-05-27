<template>
  <div class="hello">
    <div class="nav">
      <span @click="goBack"><van-icon name="arrow-left" />返回</span>
    </div>
    <h2>注册</h2>
    <van-form @submit="onSubmit" class="form">
      <van-field
        v-model="user.username"
        name="username"
        label="用户名"
        placeholder="用户名"
        clearable
        :rules="[
          {
            pattern,
            required: true,
            message: '用户名必须是6到16位的英文字符,数字或者下划线组成',
          },
        ]"
        @blur="checkUsername"
      />
      <van-field
        v-model="user.password"
        type="password"
        name="password"
        label="密码"
        placeholder="密码"
        clearable
        :rules="[
          {
            pattern,
            required: true,
            message: '密码必须是6到16位的英文字符,数字或者下划线组成',
          },
        ]"
      />
      <van-field
        v-model="user.password1"
        type="password"
        name="password1"
        label="确认密码"
        placeholder="确认密码"
        clearable
        :rules="[
          {
            pattern,
            required: true,
            message: '确认密码必须是6到16位的英文字符,数字或者下划线组成',
          },
        ]"
      />
      <van-field
        v-model="user.email"
        name="email"
        label="邮箱"
        placeholder="邮箱"
        clearable
        :rules="[
          { validator, required: true, message: '请填写正确的邮箱格式' },
        ]"
      />
      <van-field
        v-model="user.checkcode"
        name="checkcode"
        label="验证码"
        placeholder="验证码"
        clearable
        :rules="[{ required: true, message: '请填写正确的验证码' }]"
      >
        <template #button>
          <van-button type="primary" size="small" @click="sendEmail"
            >发送验证码</van-button
          >
        </template>
      </van-field>
      <van-field name="uploader" label="头像">
        <template #input>
          <van-uploader v-model="uploader" :before-read="beforeRead" />
        </template>
      </van-field>
      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit"
          >提交</van-button
        >
      </div>
    </van-form>
  </div>
</template>

<script>
import Vue from "vue";
import { Toast } from "vant";

Vue.use(Toast);
export default {
  name: "Mine",
  data() {
    return {
      user: {
        username: "",
        password: "",
        password1: "",
        email: "",
        checkcode: "",
      },
      uploader: [],
      pattern: /^[a-z0-9_-]{6,16}$/,
    };
  },
  created() {},
  methods: {
    sendEmail() {
      if (this.user.email.trim() == "") {
        Toast.fail("请输入邮箱!");
        return;
      }
      this.axios
        .post("user/sendMail", {
          email: this.user.email,
        })
        .then((response) => {});
    },
    checkUsername() {
      if (this.user.username.trim() == "") {
        Toast.fail("请输入用户名!");
        return;
      }
      this.axios
        .post("user/checkUsername", {
          username: this.user.username,
        })
        .then((response) => {
          if (response.data == "exist") {
            Toast.fail("该用户名已有其他用户使用,请使用其他的用户名!");
            this.user.username = "";
          } else {
            Toast.success("该用户名没有其他用户使用!");
          }
        });
    },
    validator(val) {
      return /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(val);
    },
    onSubmit(values) {
      console.log("submit", values);
      if (this.user.password != this.user.password1) {
        Toast.fail("密码与确认密码不一致!");
        return;
      }
      var form = document.querySelector(".form");
      var formData = new FormData(form);
      var upload = this.uploader[0];
      if (upload == undefined) {
        Toast.fail("请上传头像!");
        return;
      }
      formData.append("imgFile", this.uploader[0].file);
      this.axios
        .post("user/regist", formData, {
          "content-type": "multipart/form-data",
        })
        .then((response) => {
          console.log(response.data);
          if (response.data == "failed") {
            Toast.fail("注册失败!");
          } else {
            Toast.success("注册成功!");
            this.$router.push("/Mine");
          }
        });
    },
    goBack() {
      this.$router.go(-1); //返回上一次路由
    },
    beforeRead(file) {
      if (file.type !== "image/jpeg" && file.type !== "image/png") {
        Toast("请上传 jpg 或 png 格式图片");
        return false;
      }
      return true;
    },
  },
};
</script>
<style scoper>
</style>
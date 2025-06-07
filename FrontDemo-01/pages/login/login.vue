<template>
  <view class="login-container">
    <!-- 自定义导航栏 -->
  
    
    <view class="login-box">
      <view class="welcome-text">
        <text class="title">欢迎回来</text>
        <text class="subtitle">登录您的账号以继续</text>
      </view>
      
      <view class="input-group">
        <view class="input-item">
          <text class="iconfont icon-user"></text>
          <input type="text" v-model="loginForm.username" placeholder="请输入用户名" />
        </view>
        <view class="input-item">
          <text class="iconfont icon-lock"></text>
          <input type="password" v-model="loginForm.password" placeholder="请输入密码" />
        </view>
      </view>
      
      <view class="forgot-password" @tap="goToForgotPassword">忘记密码？</view>
      
      <button class="login-btn" @tap="handleLogin">登录</button>
      
      <view class="register-link">
        还没有账号？<text @tap="goToRegister">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import config from '@/config/index.js'

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    async handleLogin() {
      console.log('handleLogin 被调用');
      if (!this.loginForm.username || !this.loginForm.password) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      uni.request({
        url: `${config.baseUrl}/login`,
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          username: this.loginForm.username,
          password: this.loginForm.password
        },
        success: (res) => {
          console.log('响应数据:', res);
          console.log('code:', res.data.code, typeof res.data.code);
          if (res.data && (res.data.code == 200 || res.data.code == 1 || res.data.code == "200" || res.data.code == "1")) {
            uni.showToast({
              title: '登录成功',
              icon: 'success',
              duration: 1500
            });
            // 保存token
            uni.setStorageSync('token', res.data.token);
            console.log('准备跳转到首页...');
            uni.reLaunch({
              url: '/pages/index/index',
              success: () => {
                console.log('跳转成功');
              },
              fail: (err) => {
                console.error('跳转失败:', err);
                uni.showToast({
                  title: '跳转失败，请重试',
                  icon: 'none',
                  duration: 2000
                });
              }
            });
          } else {
            uni.showToast({
              title: '账号或密码不正确',
              icon: 'none',
              duration: 2000
            });
            console.log('登录失败，code不匹配');
          }
        },
        fail: () => {
          uni.showToast({
            title: '网络错误或服务器异常',
            icon: 'none'
          })
        }
      })
    },
    goToRegister() {
      uni.navigateTo({
        url: '/pages/login/register'
      })
    },
    goToForgotPassword() {
      uni.navigateTo({
        url: '/pages/login/forgot-password'
      })
    }
  }
}
</script>

<style lang="scss">
.login-container {
  min-height: 100vh;
  background-color: #ffffff;
  
  .custom-nav {
    height: 88rpx;
    padding-top: var(--status-bar-height);
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    background-color: #ffffff;
    position: relative;
    
    .nav-back {
      position: absolute;
      left: 30rpx;
      font-size: 36rpx;
      color: #333;
    }
    
    .nav-title {
      flex: 1;
      text-align: center;
      font-size: 34rpx;
      font-weight: 500;
      color: #333;
    }
  }
  
  .login-box {
    padding: 60rpx 40rpx;
    
    .welcome-text {
      margin-bottom: 60rpx;
      
      .title {
        font-size: 48rpx;
        font-weight: bold;
        color: #333;
        display: block;
        margin-bottom: 20rpx;
      }
      
      .subtitle {
        font-size: 28rpx;
        color: #999;
      }
    }
    
    .input-group {
      margin-bottom: 40rpx;
      
      .input-item {
        display: flex;
        align-items: center;
        height: 100rpx;
        border-bottom: 1px solid #eee;
        margin-bottom: 30rpx;
        
        .iconfont {
          font-size: 40rpx;
          color: #999;
          margin-right: 20rpx;
        }
        
        input {
          flex: 1;
          height: 100%;
          font-size: 32rpx;
        }
      }
    }
    
    .forgot-password {
      text-align: right;
      color: #666;
      font-size: 28rpx;
      margin-bottom: 60rpx;
    }
    
    .login-btn {
      width: 100%;
      height: 90rpx;
      line-height: 90rpx;
      background: linear-gradient(to right, #4CAF50, #45a049);
      color: #fff;
      border-radius: 45rpx;
      margin-bottom: 40rpx;
      font-size: 32rpx;
      font-weight: 500;
    }
    
    .register-link {
      text-align: center;
      font-size: 28rpx;
      color: #666;
      
      text {
        color: #4CAF50;
        margin-left: 10rpx;
      }
    }
  }
}
</style> 
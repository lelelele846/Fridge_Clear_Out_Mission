<template>
  <view class="register-container">
    <!-- 自定义导航栏 -->

    
    <view class="register-box">
      <view class="welcome-text">
        <text class="title">创建账号</text>
        <text class="subtitle">加入我们的社区</text>
      </view>
      
      <view class="avatar-upload" @tap="chooseAvatar">
        <image v-if="registerForm.avatar" :src="registerForm.avatar" mode="aspectFill"></image>
        <view v-else class="upload-placeholder">
          <text class="iconfont icon-camera"></text>
          <text>点击上传头像</text>
        </view>
      </view>
      
      <view class="input-group">
        <view class="input-item">
          <text class="iconfont icon-user"></text>
          <input type="text" v-model="registerForm.username" placeholder="请输入用户名" />
        </view>
        <view class="input-item">
          <text class="iconfont icon-email"></text>
          <input type="text" v-model="registerForm.email" placeholder="请输入邮箱" />
        </view>
        <view class="input-item">
          <text class="iconfont icon-lock"></text>
          <input type="password" v-model="registerForm.password" placeholder="请输入密码" />
        </view>
        <view class="input-item">
          <text class="iconfont icon-lock"></text>
          <input type="password" v-model="registerForm.confirmPassword" placeholder="请确认密码" />
        </view>
      </view>
      
      <button class="register-btn" @tap="handleRegister">注册</button>
      
      <view class="login-link">
        已有账号？<text @tap="goToLogin">立即登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import config from '@/config/index.js'

export default {
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        avatar: ''
      }
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.registerForm.avatar = res.tempFilePaths[0]
        }
      })
    },
    handleRegister() {
      if (!this.registerForm.username || !this.registerForm.email || !this.registerForm.password) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      uni.request({
        url: `${config.baseUrl}/register`,
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          image: this.registerForm.avatar,
          username: this.registerForm.username,
          email: this.registerForm.email,
          password: this.registerForm.password
        },
        success: (res) => {
          if (res.data && (res.data.code === "1" || res.data.code === 1 || res.data.code === "200" || res.data.code === 200)) {
            uni.showToast({
              title: '注册成功',
              icon: 'success',
              duration: 1500
            })
            // 延迟1.5秒后返回登录页
            setTimeout(() => {
              // 使用reLaunch替代navigateBack，确保返回到登录页
              uni.reLaunch({
                url: '/pages/login/login',
                success: () => {
                  console.log('返回登录页成功')
                },
                fail: (err) => {
                  console.error('返回登录页失败:', err)
                  uni.showToast({
                    title: '返回登录页失败',
                    icon: 'none',
                    duration: 2000
                  })
                }
              })
            }, 1500)
          } else {
            uni.showToast({
              title: res.data.msg || res.data.message || '注册失败',
              icon: 'none',
              duration: 2000
            })
          }
        },
        fail: () => {
          uni.showToast({
            title: '网络错误或服务器异常',
            icon: 'none',
            duration: 2000
          })
        }
      })
    },
    goToLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.register-container {
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
  
  .register-box {
    padding: 60rpx 40rpx;
    
    .welcome-text {
      margin-bottom: 40rpx;
      
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
    
    .avatar-upload {
      width: 160rpx;
      height: 160rpx;
      margin: 0 auto 60rpx;
      border-radius: 50%;
      overflow: hidden;
      background-color: #f8f8f8;
      border: 2rpx dashed #ddd;
      
      image {
        width: 100%;
        height: 100%;
      }
      
      .upload-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #999;
        font-size: 24rpx;
        
        .iconfont {
          font-size: 48rpx;
          margin-bottom: 10rpx;
        }
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
    
    .register-btn {
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
    
    .login-link {
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
<template>
  <view class="forgot-password-container">
    <!-- 自定义导航栏 -->
   
    <view class="forgot-password-box">
      <view class="welcome-text">
        <text class="title">找回密码</text>
        <text class="subtitle">我们将向您的邮箱发送验证码</text>
      </view>
      
      <view class="input-group">
        <view class="input-item">
          <text class="iconfont icon-email"></text>
          <input type="text" v-model="email" placeholder="请输入注册邮箱" />
        </view>
        <view class="verify-code">
          <view class="input-item">
            <text class="iconfont icon-verify"></text>
            <input type="text" v-model="verifyCode" placeholder="请输入验证码" />
          </view>
          <button class="send-code-btn" @tap="sendVerifyCode" :disabled="isCodeSending">
            {{isCodeSending ? countDown + 's' : '获取验证码'}}
          </button>
        </view>
        <view class="input-item">
          <text class="iconfont icon-lock"></text>
          <input type="password" v-model="newPassword" placeholder="请输入新密码" />
        </view>
        <view class="input-item">
          <text class="iconfont icon-lock"></text>
          <input type="password" v-model="confirmPassword" placeholder="请确认新密码" />
        </view>
      </view>
      
      <button class="submit-btn" @tap="handleResetPassword">重置密码</button>
      
      <view class="back-to-login" @tap="goToLogin">返回登录</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      verifyCode: '',
      newPassword: '',
      confirmPassword: '',
      isCodeSending: false,
      countDown: 60
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    sendVerifyCode() {
      if (!this.email) {
        uni.showToast({
          title: '请输入邮箱',
          icon: 'none'
        })
        return
      }
      
      // 验证邮箱格式
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.email)) {
        uni.showToast({
          title: '请输入正确的邮箱格式',
          icon: 'none'
        })
        return
      }
      
      // TODO: 发送验证码逻辑
      this.isCodeSending = true
      this.startCountDown()
    },
    startCountDown() {
      const timer = setInterval(() => {
        if (this.countDown > 0) {
          this.countDown--
        } else {
          this.isCodeSending = false
          this.countDown = 60
          clearInterval(timer)
        }
      }, 1000)
    },
    handleResetPassword() {
      if (!this.email || !this.verifyCode || !this.newPassword || !this.confirmPassword) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      
      if (this.newPassword !== this.confirmPassword) {
        uni.showToast({
          title: '两次密码输入不一致',
          icon: 'none'
        })
        return
      }
      
      // 验证邮箱格式
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.email)) {
        uni.showToast({
          title: '请输入正确的邮箱格式',
          icon: 'none'
        })
        return
      }
      
      uni.request({
        url: '你的后端接口地址/forget', // 替换为你的后端地址
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          email: this.email,
          vcode: this.verifyCode,
          password: this.newPassword
        },
        success: (res) => {
          if (res.data && res.data.code === "1") {
            uni.showToast({
              title: '重置成功',
              icon: 'success'
            })
            setTimeout(() => {
              uni.navigateBack()
            }, 1000)
          } else {
            uni.showToast({
              title: res.data.msg || '重置失败',
              icon: 'none'
            })
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
    goToLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.forgot-password-container {
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
  
  .forgot-password-box {
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
      
      .verify-code {
        display: flex;
        align-items: center;
        margin-bottom: 30rpx;
        
        .input-item {
          flex: 1;
          margin-bottom: 0;
          margin-right: 20rpx;
        }
        
        .send-code-btn {
          width: 200rpx;
          height: 80rpx;
          line-height: 80rpx;
          background: linear-gradient(to right, #4CAF50, #45a049);
          color: #fff;
          border-radius: 40rpx;
          font-size: 24rpx;
          
          &[disabled] {
            background: #ccc;
          }
        }
      }
    }
    
    .submit-btn {
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
    
    .back-to-login {
      text-align: center;
      font-size: 28rpx;
      color: #4CAF50;
    }
  }
}
</style> 
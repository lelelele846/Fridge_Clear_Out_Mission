<template>
  <view class="photo-add-container">
    <!-- 顶部导航栏 -->
    <view class="nav-bar">
      <view class="back-btn" @click="goBack">
        <uni-icons type="back" size="24" color="#fff" />
      </view>
      <text class="nav-title">扫描添加</text>
      <view class="nav-right"></view>
    </view>

    <!-- 相机预览区域 -->
    <view class="camera-section">
      <view v-if="image" class="image-preview">
        <image :src="image" mode="aspectFill" />
        <view class="scan-line" :class="{ 'scanning': isScanning }"></view>
      </view>
      <view v-else class="camera-placeholder">
        <view class="scan-frame">
          <view class="corner top-left"></view>
          <view class="corner top-right"></view>
          <view class="corner bottom-left"></view>
          <view class="corner bottom-right"></view>
        </view>
        <view class="scan-line" :class="{ 'scanning': isScanning }"></view>
        <text class="scan-tip">将食物放入框内</text>
      </view>
    </view>

    <!-- 底部操作区 -->
    <view class="bottom-section">
      <view v-if="!image" class="camera-controls">
        <button class="control-btn gallery" @click="chooseFromGallery">
          <uni-icons type="image" size="32" color="#fff" />
        </button>
        <button class="control-btn capture" @click="takePhoto">
          <view class="capture-btn"></view>
        </button>
        <button class="control-btn flash" @click="toggleFlash">
          <uni-icons :type="isFlashOn ? 'flash-filled' : 'flash'" size="32" color="#fff" />
        </button>
      </view>
      
      <view v-if="image" class="result-controls">
        <button class="retake-btn" @click="retake">
          <uni-icons type="refresh" size="24" color="#fff" />
          <text>重拍</text>
        </button>
        <button class="submit-btn" @click="submit">
          <text>确认添加</text>
          <uni-icons type="right" size="24" color="#fff" />
        </button>
      </view>
    </view>

    <!-- 识别结果弹窗 -->
    <view v-if="result" class="result-popup" :class="{ 'show': showResult }">
      <view class="result-content">
        <view class="result-header">
          <text class="result-title">识别结果</text>
          <view class="close-btn" @click="closeResult">
            <uni-icons type="close" size="24" color="#666" />
          </view>
        </view>
        <view class="result-info">
          <view class="info-item">
            <text class="label">食物名称</text>
            <text class="value">{{result.name}}</text>
          </view>
          <view class="info-item">
            <text class="label">类别</text>
            <text class="value">{{result.type}}</text>
          </view>
          <view class="info-item">
            <text class="label">保质期</text>
            <text class="value">{{result.expiry || '7天'}}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      image: '',
      result: null,
      isScanning: false,
      isFlashOn: false,
      showResult: false
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    takePhoto() {
      this.isScanning = true;
      setTimeout(() => {
        uni.chooseImage({
          count: 1,
          sourceType: ['camera'],
          success: (res) => {
            this.image = res.tempFilePaths[0];
            this.isScanning = false;
            // 模拟识别过程
            setTimeout(() => {
              this.result = {
                name: '苹果',
                type: '水果',
                expiry: '7天'
              };
              this.showResult = true;
            }, 1000);
          }
        });
      }, 1500);
    },
    chooseFromGallery() {
      uni.chooseImage({
        count: 1,
        sourceType: ['album'],
        success: (res) => {
          this.image = res.tempFilePaths[0];
          // 模拟识别过程
          setTimeout(() => {
            this.result = {
              name: '苹果',
              type: '水果',
              expiry: '7天'
            };
            this.showResult = true;
          }, 1000);
        }
      });
    },
    toggleFlash() {
      this.isFlashOn = !this.isFlashOn;
    },
    retake() {
      this.image = '';
      this.result = null;
      this.showResult = false;
    },
    submit() {
      uni.showToast({ title: '添加成功', icon: 'success' });
      setTimeout(() => uni.navigateBack(), 1000);
    },
    closeResult() {
      this.showResult = false;
    }
  }
}
</script>

<style lang="scss" scoped>
.photo-add-container {
  min-height: 100vh;
  background: #000;
  display: flex;
  flex-direction: column;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-xl $spacing-lg;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.back-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color $transition-fast;
  
  &:active {
    background-color: rgba(255, 255, 255, 0.1);
  }
}

.nav-title {
  font-size: $font-size-lg;
  color: #fff;
  font-weight: 600;
}

.nav-right {
  width: 64rpx;
}

.camera-section {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.camera-placeholder {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.scan-frame {
  width: 600rpx;
  height: 600rpx;
  position: relative;
}

.corner {
  position: absolute;
  width: 60rpx;
  height: 60rpx;
  border-color: $primary-color;
  border-style: solid;
  border-width: 0;
  
  &.top-left {
    top: 0;
    left: 0;
    border-top-width: 4rpx;
    border-left-width: 4rpx;
  }
  
  &.top-right {
    top: 0;
    right: 0;
    border-top-width: 4rpx;
    border-right-width: 4rpx;
  }
  
  &.bottom-left {
    bottom: 0;
    left: 0;
    border-bottom-width: 4rpx;
    border-left-width: 4rpx;
  }
  
  &.bottom-right {
    bottom: 0;
    right: 0;
    border-bottom-width: 4rpx;
    border-right-width: 4rpx;
  }
}

.scan-line {
  position: absolute;
  left: 0;
  right: 0;
  height: 4rpx;
  background: $primary-color;
  box-shadow: 0 0 8rpx $primary-color;
  
  &.scanning {
    animation: scan 2s linear infinite;
  }
}

.scan-tip {
  position: absolute;
  bottom: 200rpx;
  color: #fff;
  font-size: $font-size-md;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.5);
}

.image-preview {
  width: 100%;
  height: 100%;
  position: relative;
  
  image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.bottom-section {
  padding: $spacing-xl;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

.camera-controls {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.control-btn {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  transition: all $transition-fast;
  
  &:active {
    transform: scale(0.95);
    background: rgba(255, 255, 255, 0.3);
  }
  
  &.capture {
    width: 120rpx;
    height: 120rpx;
    background: rgba(255, 255, 255, 0.3);
    
    .capture-btn {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      background: #fff;
      border: 4rpx solid rgba(255, 255, 255, 0.3);
    }
  }
}

.result-controls {
  display: flex;
  justify-content: space-between;
  gap: $spacing-lg;
}

.retake-btn, .submit-btn {
  flex: 1;
  height: 88rpx;
  border-radius: $border-radius-lg;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-xs;
  font-size: $font-size-md;
  font-weight: 500;
  border: none;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.95);
  }
}

.retake-btn {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.submit-btn {
  background: $primary-color;
  color: #fff;
}

.result-popup {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background: $card-background;
  border-radius: $border-radius-xl $border-radius-xl 0 0;
  transform: translateY(100%);
  transition: transform $transition-normal;
  
  &.show {
    transform: translateY(0);
  }
}

.result-content {
  padding: $spacing-lg;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-lg;
}

.result-title {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.close-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color $transition-fast;
  
  &:active {
    background-color: rgba($primary-light, 0.1);
  }
}

.result-info {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-sm 0;
  border-bottom: 2rpx solid rgba($primary-light, 0.1);
  
  .label {
    font-size: $font-size-md;
    color: $text-secondary;
  }
  
  .value {
    font-size: $font-size-md;
    color: $text-primary;
    font-weight: 500;
  }
}

@keyframes scan {
  0% {
    top: 0;
  }
  100% {
    top: 100%;
  }
}
</style> 
<template>
  <view  class="add-modal-bg" @click= "$emit('close')">
    <view class="add-modal">
      <button class="add-btn" @click="$emit('manual')">
        <uni-icons type="compose" size="32" color="#fff" style="margin-right:16rpx;" />手动添加
      </button>
      <button class="add-btn" @click="$emit('photo')">
        <uni-icons type="camera" size="32" color="#fff" style="margin-right:16rpx;" />拍照添加
      </button>
    </view>
  </view>
</template>

<script>
export default {
  name: 'AddModal'
}
</script>

<style lang="scss" scoped>
.add-modal-bg {
  position: fixed;
  left: 0; right: 0; top: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
  animation: fadeIn $transition-normal;
}

.add-modal {
  background: $card-background;
  border-radius: $border-radius-xxl;
  padding: $spacing-xl;
  box-shadow: $shadow-lg;
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
  min-width: 70vw;
  animation: slideUp $transition-normal;
}

.add-btn {
  background: $primary-color;
  color: #fff;
  font-size: $font-size-lg;
  border-radius: $border-radius-lg;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  box-shadow: $shadow-sm;
  margin: 0;
  border: none;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.98);
  }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(20rpx); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style> 
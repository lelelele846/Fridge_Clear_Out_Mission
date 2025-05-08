<template>
  <view class="manual-add-container">
    <view class="header">
      <text class="title">手动添加食物</text>
    </view>
    <view class="form">
      <view class="form-item">
        <text class="label">食物名称</text>
        <input v-model="form.name" placeholder="请输入食物名称" />
      </view>
      <view class="form-item">
        <text class="label">类别</text>
        <input v-model="form.type" placeholder="如：水果、蔬菜、肉类" />
      </view>
      <view class="form-item">
        <text class="label">数量</text>
        <input v-model="form.amount" type="number" placeholder="请输入数量" />
      </view>
      <view class="form-item">
        <text class="label">保质期</text>
        <input v-model="form.expiry" type="date" placeholder="请选择保质期" />
      </view>
      <view class="form-item">
        <text class="label">备注</text>
        <input v-model="form.note" placeholder="可选" />
      </view>
      <button class="submit-btn" @click="submit">添加</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        name: '',
        type: '',
        amount: '',
        expiry: '',
        note: ''
      }
    }
  },
  methods: {
    submit() {
      if (!this.form.name || !this.form.amount || !this.form.expiry) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' });
        return;
      }
      // 这里可以提交到后端或本地存储
      uni.showToast({ title: '添加成功', icon: 'success' });
      setTimeout(() => uni.navigateBack(), 1000);
    }
  }
}
</script>

<style scoped>
.manual-add-container {
  min-height: 100vh;
  background: #eafaf1;
  padding: 48rpx 32rpx;
}
.header {
  text-align: center;
  margin-bottom: 32rpx;
}
.title {
  font-size: 40rpx;
  color: #2e7d4b;
  font-weight: bold;
}
.form {
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 24rpx #d0e7d2;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}
.form-item {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}
.label {
  font-size: 28rpx;
  color: #7bc47f;
}
input {
  background: #f8f8f8;
  border-radius: 12rpx;
  padding: 16rpx;
  font-size: 28rpx;
  border: none;
}
.submit-btn {
  background: #05b214;
  color: #fff;
  font-size: 32rpx;
  border-radius: 16rpx;
  height: 88rpx;
  margin-top: 24rpx;
  font-weight: bold;
}
</style> 
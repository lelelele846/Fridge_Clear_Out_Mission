<template>
  <view class="person-container">
    <!-- 个人信息卡片 -->
    <view class="info-card">
      <view class="avatar-section">
        <image class="avatar" src="https://cdn.uviewui.com/uview/album/1.jpg" />
        <view class="user-info">
          <text class="username">张三</text>
          <text class="user-id">ID: 888888</text>
        </view>
      </view>
      <view class="stats-row">
        <view class="stat-item">
          <text class="stat-num">128</text>
          <text class="stat-label">关注</text>
        </view>
        <view class="stat-item">
          <text class="stat-num">256</text>
          <text class="stat-label">粉丝</text>
        </view>
        <view class="stat-item">
          <text class="stat-num">64</text>
          <text class="stat-label">获赞</text>
        </view>
      </view>
    </view>

    <!-- 家庭组成员 -->
    <view class="section-card">
      <view class="section-header">
        <text class="section-title">家庭组成员</text>
        <uni-icons type="plus" size="24" color="#7bc47f" />
      </view>
      <scroll-view scroll-x class="family-scroll">
        <view class="family-item" v-for="(member, idx) in familyMembers" :key="idx">
          <image class="member-avatar" :src="member.avatar" />
          <text class="member-name">{{member.name}}</text>
          <text class="member-role">{{member.role}}</text>
        </view>
      </scroll-view>
    </view>

    <!-- 个人偏好 -->
    <view class="section-card">
      <view class="section-header">
        <text class="section-title">个人偏好</text>
        <view class="edit-btn" @click="toggleEdit('preference')">
          <uni-icons :type="isEditingPreference ? 'checkmark' : 'compose'" size="24" color="#7bc47f" />
        </view>
      </view>
      <view class="preference-list">
        <view class="preference-item" v-for="(pref, idx) in preferences" :key="idx">
          <view class="pref-info">
            <uni-icons :type="pref.icon" size="24" color="#7bc47f" />
            <text class="pref-name">{{pref.name}}</text>
          </view>
          <view v-if="isEditingPreference" class="pref-options">
            <view 
              v-for="option in pref.options" 
              :key="option"
              class="option-tag"
              :class="{'selected': pref.value === option}"
              @click="updatePreference(pref, option)"
            >
              {{option}}
            </view>
          </view>
          <text v-else class="pref-value">{{pref.value}}</text>
        </view>
      </view>
    </view>

    <!-- 饮食禁忌 -->
    <view class="section-card">
      <view class="section-header">
        <text class="section-title">饮食禁忌</text>
        <view class="edit-btn" @click="toggleEdit('restriction')">
          <uni-icons :type="isEditingRestriction ? 'checkmark' : 'compose'" size="24" color="#7bc47f" />
        </view>
      </view>
      <view class="restriction-list">
        <view v-if="isEditingRestriction" class="restriction-edit">
          <view class="restriction-tags">
            <view 
              v-for="(tag, idx) in restrictionTags" 
              :key="idx"
              class="restriction-tag"
              :class="{'selected': selectedRestrictions.includes(tag)}"
              @click="toggleRestriction(tag)"
            >
              {{tag}}
            </view>
          </view>
          <view class="add-restriction">
            <input 
              type="text" 
              v-model="newRestriction" 
              placeholder="添加其他禁忌" 
              @confirm="addRestriction"
            />
            <button class="add-btn" @click="addRestriction">添加</button>
          </view>
        </view>
        <view v-else class="restriction-display">
          <view 
            v-for="(restriction, idx) in selectedRestrictions" 
            :key="idx"
            class="restriction-tag"
          >
            {{restriction}}
          </view>
          <text v-if="selectedRestrictions.length === 0" class="empty-text">暂无禁忌</text>
        </view>
      </view>
    </view>

    <!-- 底部导航栏 -->
    <BottomTabBar active="person" />
  </view>
</template>

<script>
import BottomTabBar from '@/components/BottomTabBar.vue'

export default {
  components: { BottomTabBar },
  data() {
    return {
      familyMembers: [
        { avatar: 'https://cdn.uviewui.com/uview/album/2.jpg', name: '李四', role: '爸爸' },
        { avatar: 'https://cdn.uviewui.com/uview/album/3.jpg', name: '王五', role: '妈妈' },
        { avatar: 'https://cdn.uviewui.com/uview/album/4.jpg', name: '赵六', role: '儿子' },
        { avatar: 'https://cdn.uviewui.com/uview/album/5.jpg', name: '钱七', role: '女儿' }
      ],
      isEditingPreference: false,
      isEditingRestriction: false,
      preferences: [
        { 
          icon: 'heart', 
          name: '口味偏好', 
          value: '清淡',
          options: ['清淡', '适中', '重口']
        },
        { 
          icon: 'star', 
          name: '饮食禁忌', 
          value: '无',
          options: ['无', '少糖', '少盐', '少油']
        }
      ],
      restrictionTags: ['海鲜过敏', '乳糖不耐', '麸质过敏', '花生过敏', '素食'],
      selectedRestrictions: [],
      newRestriction: ''
    }
  },
  methods: {
    toggleEdit(type) {
      if (type === 'preference') {
        this.isEditingPreference = !this.isEditingPreference;
        if (!this.isEditingPreference) {
          // 保存偏好设置
          uni.showToast({ title: '偏好已保存', icon: 'success' });
        }
      } else {
        this.isEditingRestriction = !this.isEditingRestriction;
        if (!this.isEditingRestriction) {
          // 保存禁忌设置
          uni.showToast({ title: '禁忌已保存', icon: 'success' });
        }
      }
    },
    updatePreference(pref, value) {
      pref.value = value;
    },
    toggleRestriction(tag) {
      const index = this.selectedRestrictions.indexOf(tag);
      if (index === -1) {
        this.selectedRestrictions.push(tag);
      } else {
        this.selectedRestrictions.splice(index, 1);
      }
    },
    addRestriction() {
      if (this.newRestriction && !this.selectedRestrictions.includes(this.newRestriction)) {
        this.selectedRestrictions.push(this.newRestriction);
        this.newRestriction = '';
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.person-container {
  min-height: 100vh;
  background: $background-color;
  padding: $spacing-lg;
  padding-bottom: 140rpx;
}

.info-card {
  background: $card-background;
  border-radius: $border-radius-xl;
  padding: $spacing-xl;
  box-shadow: $shadow-sm;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid $primary-light;
  box-shadow: $shadow-sm;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.username {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.user-id {
  font-size: $font-size-sm;
  color: $text-secondary;
}

.stats-row {
  display: flex;
  justify-content: space-around;
  margin-top: $spacing-lg;
  padding-top: $spacing-lg;
  border-top: 2rpx solid rgba($primary-light, 0.1);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-xs;
}

.stat-num {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.stat-label {
  font-size: $font-size-sm;
  color: $text-secondary;
}

.section-card {
  background: $card-background;
  border-radius: $border-radius-xl;
  padding: $spacing-xl;
  margin-top: $spacing-lg;
  box-shadow: $shadow-sm;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-md;
}

.section-title {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.family-scroll {
  white-space: nowrap;
  margin: 0 -$spacing-lg;
  padding: 0 $spacing-lg;
}

.family-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  margin-right: $spacing-lg;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.95);
  }
}

.member-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  border: 4rpx solid $primary-light;
  box-shadow: $shadow-sm;
}

.member-name {
  font-size: $font-size-sm;
  color: $text-primary;
  margin-top: $spacing-xs;
}

.member-role {
  font-size: $font-size-xs;
  color: $text-secondary;
}

.preference-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.preference-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-sm 0;
  border-bottom: 2rpx solid rgba($primary-light, 0.1);
  transition: background-color $transition-fast;
  
  &:active {
    background-color: rgba($primary-light, 0.05);
  }
}

.pref-info {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
}

.pref-name {
  font-size: $font-size-md;
  color: $text-primary;
}

.pref-value {
  font-size: $font-size-md;
  color: $text-secondary;
}

.edit-btn {
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

.pref-options {
  display: flex;
  gap: $spacing-sm;
}

.option-tag {
  padding: 4rpx $spacing-sm;
  border-radius: $border-radius-sm;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: rgba($primary-light, 0.1);
  transition: all $transition-fast;
  
  &:active {
    transform: scale(0.95);
  }
  
  &.selected {
    background: $primary-color;
    color: #fff;
  }
}

.restriction-list {
  margin-top: $spacing-md;
}

.restriction-edit {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.restriction-tags {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
}

.restriction-tag {
  padding: 8rpx $spacing-md;
  border-radius: $border-radius-lg;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: rgba($primary-light, 0.1);
  transition: all $transition-fast;
  
  &:active {
    transform: scale(0.95);
  }
  
  &.selected {
    background: $primary-color;
    color: #fff;
  }
}

.add-restriction {
  display: flex;
  gap: $spacing-sm;
  margin-top: $spacing-sm;
  
  input {
    flex: 1;
    height: 64rpx;
    background: rgba($primary-light, 0.1);
    border-radius: $border-radius-lg;
    padding: 0 $spacing-md;
    font-size: $font-size-sm;
  }
  
  .add-btn {
    height: 64rpx;
    padding: 0 $spacing-lg;
    background: $primary-color;
    color: #fff;
    border-radius: $border-radius-lg;
    font-size: $font-size-sm;
    display: flex;
    align-items: center;
    justify-content: center;
    border: none;
    
    &:active {
      transform: scale(0.95);
    }
  }
}

.restriction-display {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
  min-height: 64rpx;
  align-items: center;
}

.empty-text {
  font-size: $font-size-sm;
  color: $text-secondary;
}
</style> 
<template>
  <view class="ai-container">
    <!-- 顶部栏 -->
    <view class="ai-top-bar">
      <text class="ai-title">AI 菜谱</text>
      <view class="ai-filters">
        <view class="filter-item">
          <picker @change="onDietChange" :value="dietIndex" :range="dietOptions" range-key="label">
            <view class="picker-content">
              <uni-icons type="medal" size="20" color="#6c47ff" />
              <text>{{dietOptions[dietIndex].label}}</text>
              <uni-icons type="bottom" size="16" color="#6c47ff" />
            </view>
          </picker>
        </view>
        <view class="filter-item">
          <picker @change="onCuisineChange" :value="cuisineIndex" :range="cuisineOptions" range-key="label">
            <view class="picker-content">
              <uni-icons type="cutlery" size="20" color="#6c47ff" />
              <text>{{cuisineOptions[cuisineIndex].label}}</text>
              <uni-icons type="bottom" size="16" color="#6c47ff" />
            </view>
          </picker>
        </view>
        <view class="filter-item">
          <picker @change="onTimeChange" :value="timeIndex" :range="timeOptions" range-key="label">
            <view class="picker-content">
              <uni-icons type="clock" size="20" color="#6c47ff" />
              <text>{{timeOptions[timeIndex].label}}</text>
              <uni-icons type="bottom" size="16" color="#6c47ff" />
            </view>
          </picker>
        </view>
      </view>
    </view>
    <!-- 食材卡片 -->
    <view class="ai-food-list">
      <view class="ai-food-card" v-for="(food, idx) in foodList" :key="idx" 
            @click="isSelectMode ? toggleSelect(food) : showFoodDetail(food)"
            :class="{'selected': isSelectMode && selectedFoods.includes(food.name)}">
        <view class="ai-food-img">
          <text class="food-emoji">{{food.emoji}}</text>
        </view>
        <view class="ai-food-info">
          <view class="ai-food-header">
            <text class="ai-food-name">{{food.name}}</text>
            <text class="ai-food-weight">100g</text>
          </view>
          <view class="ai-food-kcal">
            <uni-icons type="fire" size="20" color="#6c47ff" />
            <text class="kcal">{{food.kcal}} 千卡</text>
          </view>
          <view class="ai-food-nutrition">
            <view class="nutrient"><uni-icons type="leaf" size="18" color="#4cd964" /><text>{{food.carb}}g</text></view>
            <view class="nutrient"><uni-icons type="medal" size="18" color="#007aff" /><text>{{food.protein}}g</text></view>
            <view class="nutrient"><uni-icons type="fire" size="18" color="#f0ad4e" /><text>{{food.fat}}g</text></view>
            <view class="nutrient"><uni-icons type="wallet" size="18" color="#bfa76a" /><text>{{food.price}}</text></view>
          </view>
        </view>
        <view v-if="isSelectMode" class="select-indicator">
          <uni-icons :type="selectedFoods.includes(food.name) ? 'checkbox-filled' : 'circle'" 
                    size="24" 
                    :color="selectedFoods.includes(food.name) ? '#4caf50' : '#bdbdbd'" />
        </view>
      </view>
    </view>
    <!-- 主要功能按钮 -->
    <view class="ai-btns">
      <button class="ai-btn gradient-main" @click="generateFromExpiring">
        <uni-icons type="shuffle" size="22" color="#fff" style="margin-right:8rpx;" />临期食材生成
      </button>
      <button class="ai-btn gradient-sub" @click="toggleSelectMode">
        <uni-icons type="cutlery" size="22" color="#6c47ff" style="margin-right:8rpx;" />
        {{isSelectMode ? '确认选择' : '自选食材生成'}}
      </button>
    </view>
    <!-- 底部导航栏 -->
    <BottomTabBar @add="openAddModal" active="ai" />
  </view>
</template>

<script>
import BottomTabBar from '@/components/BottomTabBar.vue'
import AddModal from '@/components/AddModal.vue'
export default {
  components: { BottomTabBar, AddModal },
  data() {
    return {
      dietIndex: 0,
      cuisineIndex: 0,
      timeIndex: 0,
      dietOptions: [
        { label: '增肌', value: 'muscle' },
        { label: '减脂', value: 'diet' },
        { label: '均衡', value: 'balanced' },
        { label: '素食', value: 'vegetarian' },
        { label: '无麸质', value: 'gluten-free' }
      ],
      cuisineOptions: [
        { label: '中式', value: 'chinese' },
        { label: '西式', value: 'western' },
        { label: '日式', value: 'japanese' },
        { label: '韩式', value: 'korean' },
        { label: '意式', value: 'italian' }
      ],
      timeOptions: [
        { label: '15分钟内', value: '15min' },
        { label: '30分钟内', value: '30min' },
        { label: '1小时内', value: '1hour' },
        { label: '不限时间', value: 'any' }
      ],
      isSelectMode: false,
      selectedFoods: [],
      showAddModal: false
    }
  },
  computed: {
      foodList() {
        return getApp().globalData.foodList;
      },
      loading() {
        return getApp().globalData.loading;
      }
    },
    onLoad() {
      // 如果需要重新加载数据
      if (this.foodList.length === 0) {
        getApp().fetchFoodList();
      }
    },
  methods: {
    onDietChange(e) {
      this.dietIndex = e.detail.value;
      this.updateRecipes();
    },
    onCuisineChange(e) {
      this.cuisineIndex = e.detail.value;
      this.updateRecipes();
    },
    onTimeChange(e) {
      this.timeIndex = e.detail.value;
      this.updateRecipes();
    },
    updateRecipes() {
      // 这里可以根据选择的过滤条件更新食谱
      console.log('更新食谱', {
        diet: this.dietOptions[this.dietIndex].value,
        cuisine: this.cuisineOptions[this.cuisineIndex].value,
        time: this.timeOptions[this.timeIndex].value
      });
    },
    showFoodDetail(food) {
      uni.navigateTo({
        url: `/pages/food/detail?food=${encodeURIComponent(JSON.stringify(food))}`
      });
    },
    toggleSelectMode() {
      if (this.isSelectMode && this.selectedFoods.length > 0) {
        // 跳转到食谱生成页面
        uni.navigateTo({
          url: `/pages/recipe/generate?foods=${JSON.stringify(this.selectedFoods)}`
        });
      }
      this.isSelectMode = !this.isSelectMode;
      if (!this.isSelectMode) {
        this.selectedFoods = [];
      }
    },
    toggleSelect(food) {
      const index = this.selectedFoods.indexOf(food.name);
      if (index === -1) {
        this.selectedFoods.push(food.name);
      } else {
        this.selectedFoods.splice(index, 1);
      }
    },
    generateFromExpiring() {
      const expiringFoods = this.foodList.filter(food => food.expSoon || food.expired);
      if (expiringFoods.length === 0) {
        uni.showToast({
          title: '没有临期食材',
          icon: 'none'
        });
        return;
      }
      uni.navigateTo({
        url: `/pages/recipe/generate?foods=${JSON.stringify(expiringFoods.map(f => f.name))}`
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.ai-container {
  min-height: 100vh;
  background: $background-color;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 140rpx;
}

.ai-top-bar {
  display: flex;
  flex-direction: column;
  padding: $spacing-xl $spacing-lg 0;
  background: $card-background;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.ai-title {
  font-size: $font-size-xl;
  font-weight: 600;
  color: $text-primary;
  margin-bottom: $spacing-md;
}

.ai-filters {
  display: flex;
  justify-content: space-between;
  gap: $spacing-sm;
  margin-bottom: $spacing-md;
}

.filter-item {
  flex: 1;
  background: rgba($primary-light, 0.05);
  border-radius: $border-radius-lg;
  overflow: hidden;
  transition: background-color $transition-fast;
  
  &:active {
    background: rgba($primary-light, 0.1);
  }
}

.picker-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-sm;
  gap: $spacing-xs;
}

.picker-content text {
  font-size: $font-size-sm;
  color: $primary-color;
  font-weight: 500;
}

.ai-food-list {
  margin: $spacing-lg 0 0 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.ai-food-card {
  width: 92vw;
  background: $card-background;
  border-radius: $border-radius-xl;
  box-shadow: $shadow-sm;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: $spacing-lg;
  margin-bottom: $spacing-md;
  position: relative;
  transition: all $transition-fast;
  
  &:active {
    transform: scale(0.98);
  }
  
  &.selected {
    background: rgba($primary-light, 0.05);
    border: 2rpx solid $primary-color;
  }
}

.ai-food-img {
  width: 90rpx;
  height: 90rpx;
  background: rgba($primary-light, 0.1);
  border-radius: $border-radius-lg;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: $spacing-md;
}

.food-emoji {
  font-size: $font-size-xxl;
}

.ai-food-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.ai-food-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ai-food-name {
  font-size: $font-size-md;
  font-weight: 500;
  color: $text-primary;
}

.ai-food-weight {
  font-size: $font-size-sm;
  color: $text-secondary;
}

.ai-food-kcal {
  display: flex;
  align-items: center;
  font-size: $font-size-sm;
  color: $primary-color;
  margin: $spacing-xs 0;
}

.kcal {
  margin-left: $spacing-xs;
}

.ai-food-nutrition {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.nutrient {
  display: flex;
  align-items: center;
  font-size: $font-size-xs;
  color: $primary-color;
  gap: $spacing-xs;
}

.select-indicator {
  position: absolute;
  right: $spacing-md;
  top: 50%;
  transform: translateY(-50%);
}

.ai-btns {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin: $spacing-lg 0 0 0;
  padding: 0 $spacing-lg;
  gap: $spacing-lg;
}

.ai-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: $font-size-md;
  font-weight: 500;
  border: none;
  border-radius: $border-radius-lg;
  height: 88rpx;
  margin: 0;
  box-shadow: $shadow-sm;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.98);
  }
}

.gradient-main {
  background: linear-gradient(90deg, $primary-color 0%, $primary-light 100%);
  color: #fff;
}

.gradient-sub {
  background: linear-gradient(90deg, rgba($primary-light, 0.1) 0%, rgba($primary-light, 0.2) 100%);
  color: $primary-color;
}
</style> 
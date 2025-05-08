<template>
  <view class="recipe-container">
    <!-- 顶部栏 -->
    <view class="recipe-header">
      <view class="back-button" @click="goBack">
        <uni-icons type="back" size="24" color="#2e7d4b" />
      </view>
      <text class="recipe-title">AI 食谱生成</text>
    </view>

    <!-- 已选食材 -->
    <view class="selected-foods">
      <text class="section-title">已选食材</text>
      <view class="food-tags">
        <view class="food-tag" v-for="(food, idx) in selectedFoods" :key="idx">
          <text class="food-emoji">{{getFoodEmoji(food)}}</text>
          <text class="food-name">{{food}}</text>
        </view>
      </view>
    </view>

    <!-- 食谱列表 -->
    <view class="recipe-list">
      <view class="recipe-card" v-for="(recipe, idx) in recipes" :key="idx">
        <view class="recipe-header">
          <text class="recipe-name">{{recipe.name}}</text>
          <view class="recipe-tags">
            <text class="recipe-tag" v-for="(tag, tagIdx) in recipe.tags" :key="tagIdx">{{tag}}</text>
          </view>
        </view>
        <view class="recipe-content">
          <view class="recipe-ingredients">
            <text class="section-title">食材</text>
            <view class="ingredient-list">
              <view class="ingredient-item" v-for="(ing, ingIdx) in recipe.ingredients" :key="ingIdx">
                <text class="ingredient-name">{{ing.name}}</text>
                <text class="ingredient-amount">{{ing.amount}}</text>
              </view>
            </view>
          </view>
          <view class="recipe-steps">
            <text class="section-title">步骤</text>
            <view class="step-list">
              <view class="step-item" v-for="(step, stepIdx) in recipe.steps" :key="stepIdx">
                <text class="step-number">{{stepIdx + 1}}</text>
                <text class="step-content">{{step}}</text>
              </view>
            </view>
          </view>
        </view>
        <view class="recipe-footer">
          <view class="recipe-stats">
            <view class="stat-item">
              <uni-icons type="fire" size="20" color="#f0ad4e" />
              <text>{{recipe.calories}}千卡</text>
            </view>
            <view class="stat-item">
              <uni-icons type="clock" size="20" color="#4caf50" />
              <text>{{recipe.time}}分钟</text>
            </view>
          </view>
          <button class="save-btn" @click="saveRecipe(recipe)">
            <uni-icons type="heart" size="20" color="#fff" />
            <text>收藏</text>
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedFoods: [],
      recipes: [
        {
          name: '番茄炒蛋',
          tags: ['快手', '家常'],
          ingredients: [
            { name: '番茄', amount: '2个' },
            { name: '鸡蛋', amount: '3个' },
            { name: '葱花', amount: '适量' }
          ],
          steps: [
            '番茄切块，鸡蛋打散',
            '热锅下油，倒入蛋液炒至凝固',
            '加入番茄块翻炒',
            '加入盐调味，撒上葱花即可'
          ],
          calories: 280,
          time: 15
        },
        {
          name: '香蕉奶昔',
          tags: ['饮品', '甜点'],
          ingredients: [
            { name: '香蕉', amount: '2根' },
            { name: '牛奶', amount: '250ml' },
            { name: '蜂蜜', amount: '1勺' }
          ],
          steps: [
            '香蕉去皮切块',
            '将香蕉块、牛奶、蜂蜜放入搅拌机',
            '搅拌至顺滑即可'
          ],
          calories: 320,
          time: 5
        }
      ]
    }
  },
  onLoad(options) {
    if (options.foods) {
      this.selectedFoods = JSON.parse(options.foods);
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    getFoodEmoji(foodName) {
      const foodMap = {
        '黄色番茄': '🍅',
        '苹果': '🍎',
        '香蕉': '🍌'
      };
      return foodMap[foodName] || '🍽️';
    },
    saveRecipe(recipe) {
      uni.showToast({
        title: '收藏成功',
        icon: 'success'
      });
    }
  }
}
</script>

<style scoped>
.recipe-container {
  min-height: 100vh;
  background: #eafaf1;
  padding-bottom: 32rpx;
}

.recipe-header {
  display: flex;
  align-items: center;
  padding: 48rpx 32rpx 32rpx 32rpx;
  background: #fff;
  border-bottom: 2rpx solid #e0ffe6;
}

.back-button {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.recipe-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #2e7d4b;
}

.selected-foods {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 28rpx;
  color: #7bc47f;
  margin-bottom: 16rpx;
}

.food-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.food-tag {
  display: flex;
  align-items: center;
  background: #e0ffe6;
  padding: 12rpx 24rpx;
  border-radius: 32rpx;
  gap: 8rpx;
}

.food-emoji {
  font-size: 32rpx;
}

.food-name {
  font-size: 26rpx;
  color: #2e7d4b;
}

.recipe-list {
  padding: 0 32rpx;
}

.recipe-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 24rpx #d0e7d2;
}

.recipe-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #2e7d4b;
  margin-bottom: 16rpx;
}

.recipe-tags {
  display: flex;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.recipe-tag {
  font-size: 24rpx;
  color: #4caf50;
  background: #e0ffe6;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
}

.recipe-content {
  margin-bottom: 24rpx;
}

.ingredient-list {
  margin-bottom: 24rpx;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
  border-bottom: 2rpx solid #e0ffe6;
}

.ingredient-name {
  font-size: 28rpx;
  color: #2e7d4b;
}

.ingredient-amount {
  font-size: 28rpx;
  color: #7bc47f;
}

.step-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.step-item {
  display: flex;
  gap: 16rpx;
}

.step-number {
  width: 40rpx;
  height: 40rpx;
  background: #4caf50;
  color: #fff;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
}

.step-content {
  flex: 1;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.recipe-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 2rpx solid #e0ffe6;
}

.recipe-stats {
  display: flex;
  gap: 24rpx;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  font-size: 26rpx;
  color: #666;
}

.save-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: #4caf50;
  color: #fff;
  font-size: 28rpx;
  padding: 12rpx 32rpx;
  border-radius: 32rpx;
  border: none;
}
</style> 
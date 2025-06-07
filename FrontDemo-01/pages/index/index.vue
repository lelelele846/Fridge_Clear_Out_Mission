<template>
	<view class="warehouse-container">

		<!-- 统计模块 -->
		<view class="stat-cards">
			<view class="stat-card all" :class="{'selected': currentFilter === 'all'}" @click="filterFood('all')">
				<text class="stat-num">{{foodList.length}}</text>
				<text class="stat-label">全部</text>
			</view>
			<view class="stat-card exp-soon" :class="{'selected': currentFilter === 'exp-soon'}" @click="filterFood('exp-soon')">
				<text class="stat-num">{{getExpSoonCount()}}</text>
				<text class="stat-label">即将过期</text>
			</view>
			<view class="stat-card fresh" :class="{'selected': currentFilter === 'fresh'}" @click="filterFood('fresh')">
				<text class="stat-num">{{getFreshCount()}}</text>
				<text class="stat-label">新鲜</text>
			</view>
			<view class="stat-card expired" :class="{'selected': currentFilter === 'expired'}" @click="filterFood('expired')">
				<text class="stat-num">{{getExpiredCount()}}</text>
				<text class="stat-label">已过期</text>
			</view>
		</view>

		<!-- 日历模块 -->
		<view class="calendar-section">
			<view class="calendar-header">
				<text class="calendar-title">{{currentYear}}年{{currentMonth}}月</text>
			</view>
			<view class="calendar-grid">
				<view class="weekday" v-for="day in weekDays" :key="day">{{day}}</view>
				<view class="calendar-day" 
					v-for="(day, index) in calendarDays" 
					:key="index"
					:class="{
						'has-food': day.hasFood,
						'expiring-today': day.expiringToday,
						'expired-today': day.expiredToday
					}"
					@click="showDayFoods(day)">
					<text class="day-number">{{day.date}}</text>
					<view class="day-markers" v-if="day.hasFood">
						<view class="marker expiring" v-if="day.expiringToday"></view>
						<view class="marker expired" v-if="day.expiredToday"></view>
					</view>
					<text class="day-emoji" v-if="day.emoji">{{day.emoji}}</text>
				</view>
			</view>
		</view>

		<!-- 食物列表 -->
		<view class="food-list">
			<view class="food-item" v-for="(item, idx) in filteredFoodList" :key="idx" @click="showFoodDetail(item)">
				<view class="food-img">
					<text class="food-emoji">{{item.emoji}}</text>
				</view>
				<view class="food-info">
					<view class="food-title-row">
						<text class="food-title">{{ item.name }}</text>
						<text v-if="item.expired" class="expired-tag">已过期</text>
						<text v-else-if="item.expSoon" class="exp-soon-tag">即将过期</text>
					</view>
					<view class="food-detail">
						<text class="kcal">{{ item.kcal }} 千卡 · 100g</text>
					</view>
					<view class="food-nutrition">
						<view class="nutrient">
							<uni-icons type="leaf" size="18" color="#4cd964" />
							<text>{{ item.carb }}g</text>
						</view>
						<view class="nutrient">
							<uni-icons type="medal" size="18" color="#007aff" />
							<text>{{ item.protein }}g</text>
						</view>
						<view class="nutrient">
							<uni-icons type="fire" size="18" color="#f0ad4e" />
							<text>{{ item.fat }}g</text>
						</view>
						<view class="nutrient">
							<uni-icons type="wallet" size="18" color="#bfa76a" />
							<text>{{ item.price }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部功能栏 -->
		<BottomTabBar active="fridge" @add="openAddModal" />

	</view>
</template>

<script>
	import BottomTabBar from '@/components/BottomTabBar.vue'
	export default {
		data() {
			return {
				currentTime: '',
				currentFilter: 'all',
				currentYear: new Date().getFullYear(),
				currentMonth: new Date().getMonth() + 1,
				weekDays: ['日', '一', '二', '三', '四', '五', '六'],
			}
		},
		computed: {
			foodList() {
			  return getApp().globalData.foodList;
			},
			filteredFoodList() {
				switch(this.currentFilter) {
					case 'exp-soon':
						return this.foodList.filter(item => item.expSoon);
					case 'fresh':
						return this.foodList.filter(item => !item.expired && !item.expSoon);
					case 'expired':
						return this.foodList.filter(item => item.expired);
					default:
						return this.foodList;
				}
			},
			calendarDays() {
				const days = [];
				const firstDay = new Date(this.currentYear, this.currentMonth - 1, 1);
				const lastDay = new Date(this.currentYear, this.currentMonth, 0);
				const today = new Date();
				
				// 填充月初空白
				for(let i = 0; i < firstDay.getDay(); i++) {
					days.push({ 
						date: '', 
						emoji: '',
						hasFood: false,
						expiringToday: false,
						expiredToday: false,
						foods: []
					});
				}
				
				// 填充日期
				for(let i = 1; i <= lastDay.getDate(); i++) {
					const date = new Date(this.currentYear, this.currentMonth - 1, i);
					const expiringFoods = this.foodList.filter(food => {
						const expDate = new Date(food.expiryDate);
						return expDate.getDate() === i && 
							   expDate.getMonth() === this.currentMonth - 1 && 
							   expDate.getFullYear() === this.currentYear;
					});
					
					const isToday = date.getDate() === today.getDate() && 
								   date.getMonth() === today.getMonth() && 
								   date.getFullYear() === today.getFullYear();
					
					const expiringToday = expiringFoods.some(food => food.expSoon);
					const expiredToday = expiringFoods.some(food => food.expired);
					
					days.push({
						date: i,
						emoji: expiringFoods.length > 0 ? expiringFoods[0].emoji : '',
						hasFood: expiringFoods.length > 0,
						expiringToday,
						expiredToday,
						foods: expiringFoods,
						isToday
					});
				}
				
				return days;
			}
		},
		onLoad() {
			getApp().fetchFoodList();
			this.updateTime();
			setInterval(this.updateTime, 1000);
		},
		methods: {
			updateTime() {
				const now = new Date();
				const h = now.getHours().toString().padStart(2, '0');
				const m = now.getMinutes().toString().padStart(2, '0');
				this.currentTime = `${h}:${m}`;
			},
			goSetting() {
				uni.showToast({ title: '设置功能待实现', icon: 'none' });
			},
			filterFood(type) {
				this.currentFilter = type;
			},
			getExpSoonCount() {
				return this.foodList.filter(item => item.expSoon).length;
			},
			getFreshCount() {
				return this.foodList.filter(item => !item.expired && !item.expSoon).length;
			},
			getExpiredCount() {
				return this.foodList.filter(item => item.expired).length;
			},
			showFoodDetail(food) {
				uni.navigateTo({
					 url: `/pages/food/detail?food=${encodeURIComponent(JSON.stringify(food))}`
				});
			},
			navigateToManual() {
				uni.navigateTo({ url: '/pages/food/manual-add' });
			},
			navigateToPhoto() {
				uni.navigateTo({ url: '/pages/food/photo-add' });
			},
			showDayFoods(day) {
				if (!day.hasFood) return;
				
				uni.showModal({
					title: `${this.currentYear}年${this.currentMonth}月${day.date}日`,
					content: day.foods.map(food => {
						let status = '';
						if (food.expired) status = '(已过期)';
						else if (food.expSoon) status = '(即将过期)';
						return `${food.name} ${status}`;
					}).join('\n'),
					showCancel: false,
					confirmText: '知道了'
				});
			}
		},
		components: { BottomTabBar}
	}
</script>

<style lang="scss" scoped>
.warehouse-container {
  min-height: 100vh;
  background: $background-color;
  display: flex;
  flex-direction: column;
  padding-bottom: 140rpx;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-xl $spacing-lg 0;
  background: $card-background;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.time {
  font-size: $font-size-xl;
  font-weight: 600;
  color: $text-primary;
}

.setting-icon {
  color: $primary-light;
  padding: $spacing-xs;
  border-radius: 50%;
  transition: background-color $transition-fast;
  &:active {
    background-color: rgba($primary-light, 0.1);
  }
}

.stat-cards {
  display: flex;
  justify-content: space-around;
  margin: $spacing-lg 0;
  padding: 0 $spacing-lg;
}

.stat-card {
  width: 140rpx;
  height: 140rpx;
  border-radius: $border-radius-xl;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: $card-background;
  box-shadow: $shadow-sm;
  transition: all $transition-fast;
  border: 2rpx solid transparent;
  
  &:active {
    transform: scale(0.98);
  }
  
  &.selected {
    border-color: $primary-color;
    box-shadow: 0 0 0 2rpx rgba($primary-color, 0.2);
  }
}

.stat-card.all {
  &.selected {
    border-color: $primary-light;
    box-shadow: 0 0 0 2rpx rgba($primary-light, 0.2);
  }
}

.stat-card.exp-soon {
  background: rgba($primary-light, 0.1);
  &.selected {
    border-color: $primary-light;
    box-shadow: 0 0 0 2rpx rgba($primary-light, 0.2);
  }
}

.stat-card.fresh {
  background: rgba($primary-color, 0.1);
  &.selected {
    border-color: $primary-color;
    box-shadow: 0 0 0 2rpx rgba($primary-color, 0.2);
  }
}

.stat-card.expired {
  background: rgba(#ff4d4f, 0.1);
  &.selected {
    border-color: #ff4d4f;
    box-shadow: 0 0 0 2rpx rgba(#ff4d4f, 0.2);
  }
}

.stat-num {
  font-size: $font-size-xl;
  font-weight: 600;
  color: $text-primary;
  margin-bottom: $spacing-xs;
}

.stat-label {
  font-size: $font-size-sm;
  color: $text-secondary;
}

.calendar-section {
  background: $card-background;
  border-radius: $border-radius-xl;
  margin: 0 $spacing-lg $spacing-lg;
  padding: $spacing-lg;
  box-shadow: $shadow-sm;
}

.calendar-header {
  text-align: center;
  margin-bottom: $spacing-md;
}

.calendar-title {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: $spacing-xs;
}

.weekday {
  text-align: center;
  font-size: $font-size-sm;
  color: $text-secondary;
  padding: $spacing-xs 0;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: $border-radius-md;
  background: rgba($primary-light, 0.05);
  transition: all $transition-fast;
  position: relative;
  padding: $spacing-xs;
  
  &:active {
    background: rgba($primary-light, 0.1);
  }
  
  &.has-food {
    background: rgba($primary-color, 0.1);
  }
  
  &.expiring-today {
    background: rgba($primary-light, 0.15);
    border: 2rpx solid $primary-light;
  }
  
  &.expired-today {
    background: rgba(#ff4d4f, 0.15);
    border: 2rpx solid #ff4d4f;
  }
}

.day-markers {
  position: absolute;
  top: 4rpx;
  right: 4rpx;
  display: flex;
  gap: 4rpx;
}

.marker {
  width: 8rpx;
  height: 8rpx;
  border-radius: 50%;
  
  &.expiring {
    background: $primary-light;
  }
  
  &.expired {
    background: #ff4d4f;
  }
}

.day-number {
  font-size: $font-size-sm;
  color: $text-primary;
}

.day-emoji {
  font-size: $font-size-md;
  margin-top: $spacing-xs;
}

.food-list {
  flex: 1;
  padding: 0 $spacing-lg;
}

.food-item {
  display: flex;
  background: $card-background;
  border-radius: $border-radius-xl;
  margin-bottom: $spacing-lg;
  box-shadow: $shadow-sm;
  padding: $spacing-lg;
  transition: transform $transition-fast;
  &:active {
    transform: scale(0.98);
  }
}

.food-img {
  width: 120rpx;
  height: 120rpx;
  background: rgba($primary-color, 0.1);
  border-radius: $border-radius-lg;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: $spacing-lg;
}

.food-emoji {
  font-size: $font-size-xxl;
}

.food-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.food-title-row {
  display: flex;
  align-items: center;
}

.food-title {
  font-size: $font-size-lg;
  font-weight: 600;
  color: $text-primary;
}

.expired-tag {
  background: #ff4d4f;
  color: #fff;
  font-size: $font-size-xs;
  border-radius: $border-radius-sm;
  padding: 2rpx $spacing-sm;
  margin-left: $spacing-md;
}

.exp-soon-tag {
  background: #faad14;
  color: #fff;
  font-size: $font-size-xs;
  border-radius: $border-radius-sm;
  padding: 2rpx $spacing-sm;
  margin-left: $spacing-md;
}

.food-detail {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: $spacing-xs 0;
}

.food-nutrition {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.nutrient {
  display: flex;
  align-items: center;
  font-size: $font-size-xs;
  color: $primary-light;
  gap: $spacing-xs;
}

.add-modal-bg {
  position: fixed;
  left: 0; right: 0; top: 0; bottom: 0;
  background: rgba(0,0,0,0.35);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.add-modal {
  background: #fff;
  border-radius: 32rpx;
  padding: 48rpx 32rpx;
  box-shadow: 0 8rpx 48rpx #b2b2ff44;
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  min-width: 70vw;
}

.add-btn {
  background: #05b214;
  color: #fff;
  font-size: 36rpx;
  border-radius: 20rpx;
  height: 96rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  box-shadow: 0 4rpx 16rpx #b2b2ff44;
  margin: 0;
  border: none;
}

.add-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 24rpx;
}
</style>

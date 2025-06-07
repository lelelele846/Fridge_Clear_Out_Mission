<template>
	<view class="food-detail-container">
		<!-- 顶部图片区域 -->
		<view class="food-image-section">
			<image class="food-image" :src="foodDetail.image" mode="aspectFill" />
			<view class="back-button" @click="goBack">
				<uni-icons type="back" size="24" color="#fff" />
			</view>
		</view>

		<!-- 食物基本信息 -->
		<view class="food-info-card">
			<view class="food-header">
				<text class="food-emoji">{{foodDetail.emoji}}</text>
				<view class="food-title-section">
					<text class="food-name">{{foodDetail.name}}</text>
					<view class="food-status">
						<text v-if="foodDetail.expired" class="status-tag expired">已过期</text>
						<text v-else-if="foodDetail.expSoon" class="status-tag exp-soon">即将过期</text>
						<text v-else class="status-tag fresh">新鲜</text>
					</view>
				</view>
			</view>

			<!-- 日期信息 -->
			<view class="date-info">
				<view class="date-item">
					<text class="date-label">购买日期</text>
					<text class="date-value">{{foodDetail.purchaseDate}}</text>
				</view>
				<view class="date-item">
					<text class="date-label">过期日期</text>
					<text class="date-value">{{foodDetail.expiryDate}}</text>
				</view>
			</view>

			<!-- 营养成分 -->
			<view class="nutrition-section">
				<text class="section-title">营养成分</text>
				<view class="nutrition-grid">
					<view class="nutrition-item">
						<text class="nutrition-value">{{foodDetail.kcal}}</text>
						<text class="nutrition-label">热量(kcal)</text>
					</view>
					<view class="nutrition-item">
						<text class="nutrition-value">{{foodDetail.carb}}</text>
						<text class="nutrition-label">碳水(g)</text>
					</view>
					<view class="nutrition-item">
						<text class="nutrition-value">{{foodDetail.protein}}</text>
						<text class="nutrition-label">蛋白质(g)</text>
					</view>
					<view class="nutrition-item">
						<text class="nutrition-value">{{foodDetail.fat}}</text>
						<text class="nutrition-label">脂肪(g)</text>
					</view>
				</view>
			</view>

			<!-- 存储建议 -->
			<view class="storage-section">
				<text class="section-title">存储建议</text>
				<text class="storage-tips">{{foodDetail.storageTips}}</text>
			</view>
		</view>

		<!-- 底部操作按钮 -->
		<view class="action-buttons">
			<button class="action-btn edit" @click="editFood">
				<uni-icons type="compose" size="20" color="#fff" />
				<text>编辑</text>
			</button>
			<button class="action-btn delete" @click="deleteFood">
				<uni-icons type="trash" size="20" color="#fff" />
				<text>删除</text>
			</button>
		</view>
	</view>
</template>

<script>
	
	export default {
		data() {
			return {
				foodDetail: [
					{
					name: '黄色番茄',
					emoji: '🍅',
					image: 'https://example.com/tomato.jpg',
					purchaseDate: '2024-03-20',
					expiryDate: '2024-03-25',
					kcal: 32.0,
					carb: 6.3,
					protein: 2.1,
					fat: 0.6,
					expired: false,
					expSoon: false,
					storageTips: '建议存放在阴凉干燥处，避免阳光直射。最佳保存温度为10-15℃。',
					}
				]
			}
		},
		onLoad(options) {
			if (options.food) {
			      try {
			        this.foodDetail = JSON.parse(decodeURIComponent(options.food));
			      } catch (error) {
			        console.error('解析食物信息时出错:', error);
			      }
			}
		},
		computed:{
			foodList()
			{
				return getApp().globalData.foodList;
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			editFood() {
				uni.showToast({
					title: '编辑功能开发中',
					icon: 'none'
				});
			},
			 async deleteFood() {
			      uni.showModal({
			        title: '确认删除',
			        content: '确定要删除这个食物吗？',
			        success: async (res) => {
			          if (res.confirm) {
			            try {
			              const foodId = this.foodDetail.id;
						  alert(foodId);
			              await axios.delete(`http://localhost:8080/food/${foodId}`);
			              uni.showToast({
			                title: '删除成功',
			                icon: 'success'
			              });
			              setTimeout(() => {
			                uni.navigateBack();
			              }, 1500);
			            } catch (error) {
			              console.error(error);
			              uni.showToast({
			                title: '删除失败',
			                icon: 'none'
			              });
			            }
			          }
			        }
			      });
			    }
			  }
	}
</script>

<style scoped>
	.food-detail-container {
		min-height: 100vh;
		background: #eafaf1;
	}

	.food-image-section {
		position: relative;
		height: 400rpx;
	}

	.food-image {
		width: 100%;
		height: 100%;
	}

	.back-button {
		position: absolute;
		top: 48rpx;
		left: 32rpx;
		width: 64rpx;
		height: 64rpx;
		background: rgba(0, 0, 0, 0.3);
		border-radius: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.food-info-card {
		margin: -48rpx 32rpx 32rpx 32rpx;
		background: #fff;
		border-radius: 24rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 24rpx #d0e7d2;
	}

	.food-header {
		display: flex;
		align-items: center;
		gap: 24rpx;
		margin-bottom: 32rpx;
	}

	.food-emoji {
		font-size: 64rpx;
	}

	.food-title-section {
		flex: 1;
	}

	.food-name {
		font-size: 36rpx;
		color: #2e7d4b;
		font-weight: bold;
		margin-bottom: 8rpx;
	}

	.food-status {
		display: flex;
		gap: 16rpx;
	}

	.status-tag {
		font-size: 24rpx;
		padding: 4rpx 16rpx;
		border-radius: 8rpx;
	}

	.status-tag.expired {
		background: #ff4d4f;
		color: #fff;
	}

	.status-tag.exp-soon {
		background: #faad14;
		color: #fff;
	}

	.status-tag.fresh {
		background: #52c41a;
		color: #fff;
	}

	.date-info {
		display: flex;
		justify-content: space-between;
		margin-bottom: 32rpx;
		padding-bottom: 32rpx;
		border-bottom: 2rpx solid #eafaf1;
	}

	.date-item {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.date-label {
		font-size: 24rpx;
		color: #7bc47f;
	}

	.date-value {
		font-size: 28rpx;
		color: #2e7d4b;
		font-weight: 500;
	}

	.section-title {
		font-size: 32rpx;
		color: #2e7d4b;
		font-weight: bold;
		margin-bottom: 24rpx;
	}

	.nutrition-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 24rpx;
		margin-bottom: 32rpx;
		padding-bottom: 32rpx;
		border-bottom: 2rpx solid #eafaf1;
	}

	.nutrition-item {
		background: #f8f8f8;
		padding: 24rpx;
		border-radius: 16rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}

	.nutrition-value {
		font-size: 36rpx;
		color: #2e7d4b;
		font-weight: bold;
	}

	.nutrition-label {
		font-size: 24rpx;
		color: #7bc47f;
	}

	.storage-tips {
		font-size: 28rpx;
		color: #666;
		line-height: 1.6;
	}

	.action-buttons {
		display: flex;
		gap: 24rpx;
		padding: 32rpx;
	}

	.action-btn {
		flex: 1;
		height: 88rpx;
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
		font-size: 28rpx;
		color: #fff;
	}

	.action-btn.edit {
		background: #4caf50;
	}

	.action-btn.delete {
		background: #ff4d4f;
	}
</style> 
<template>
  <view class="community-container">
    <!-- 顶部栏 -->
    <view class="community-top-bar">
      <text class="community-title">19:49</text>
      <view class="community-tabs">
        <text :class="['tab', {active: tab==='关注'}]" @click="tab='关注'">关注</text>
        <text :class="['tab', {active: tab==='推荐'}]" @click="tab='推荐'">推荐</text>
      </view>
      <uni-icons type="compose" size="28" color="#7bc47f" />
    </view>
    <!-- 用户故事横向滚动 -->
    <scroll-view scroll-x class="story-scroll">
      <view class="story-item" v-for="(story, idx) in stories" :key="idx">
        <image class="story-avatar" :src="story.avatar" />
        <text class="story-name">{{story.name}}</text>
        <view class="story-live" v-if="story.live">直播中</view>
      </view>
    </scroll-view>
    <!-- 分享内容区 -->
    <view class="community-feed">
      <view class="feed-card" v-for="(feed, idx) in feeds" :key="idx">
        <view class="feed-header">
          <image class="feed-avatar" :src="feed.avatar" />
          <view class="feed-userinfo">
            <text class="feed-username">{{feed.name}}</text>
            <text class="feed-desc">{{feed.desc}}</text>
          </view>
          <view class="feed-tag" v-if="feed.live">直播中</view>
        </view>
        <view class="feed-content">
          <text>{{feed.content}}</text>
        </view>
        <image v-if="feed.img" class="feed-img" :src="feed.img" />
        <view class="feed-actions">
          <uni-icons type="redo" size="22" color="#7bc47f" />
          <uni-icons type="chat" size="22" color="#7bc47f" />
          <uni-icons type="heart" size="22" color="#7bc47f" />
        </view>
      </view>
    </view>
    <!-- 底部导航栏 -->
    <BottomTabBar active="community" />
  </view>
</template>

<script>
import BottomTabBar from '@/components/BottomTabBar.vue'
export default {
  components: { BottomTabBar },
  data() {
    return {
      tab: '关注',
      stories: [
        { avatar: 'https://cdn.uviewui.com/uview/album/1.jpg', name: 'TED英语官方', live: true },
        { avatar: 'https://cdn.uviewui.com/uview/album/2.jpg', name: '真·风舞九天', live: true },
        { avatar: 'https://cdn.uviewui.com/uview/album/3.jpg', name: '哔哩哔哩漫画', live: true },
        { avatar: 'https://cdn.uviewui.com/uview/album/4.jpg', name: '纸条App', live: true },
        { avatar: 'https://cdn.uviewui.com/uview/album/5.jpg', name: '逍遥散人', live: true }
      ],
      feeds: [
        {
          avatar: 'https://cdn.uviewui.com/uview/album/1.jpg',
          name: 'TED英语官方',
          desc: '直播了',
          live: true,
          content: '光速单词 6000',
          img: 'https://img1.baidu.com/it/u=123456789,123456789&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=300'
        },
        {
          avatar: 'https://cdn.uviewui.com/uview/album/6.jpg',
          name: 'CNBeatbox',
          desc: '刚刚 · 投搞了视频',
          live: false,
          content: '张嘴就来 BBOX巡演结束后的随机采访真的给了我们太多惊喜，我们拥有最棒的观众！今年夏天，我们在现场等你。',
          img: 'https://img1.baidu.com/it/u=987654321,987654321&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=300'
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.community-container {
  min-height: 100vh;
  background: $background-color;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 140rpx;
}

.community-top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-xl $spacing-lg 0;
  background: $card-background;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.community-title {
  font-size: $font-size-lg;
  color: $text-primary;
  font-weight: 600;
}

.community-tabs {
  display: flex;
  gap: $spacing-lg;
}

.tab {
  font-size: $font-size-md;
  color: $text-secondary;
  font-weight: 500;
  padding: 0 $spacing-xs;
  transition: color $transition-fast;
  
  &.active {
    color: $primary-color;
    border-bottom: 4rpx solid $primary-color;
  }
}

.story-scroll {
  display: flex;
  flex-direction: row;
  margin: $spacing-md 0 0 0;
  padding-left: $spacing-md;
  height: 120rpx;
  white-space: nowrap;
}

.story-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  margin-right: $spacing-lg;
  position: relative;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.95);
  }
}

.story-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  border: 4rpx solid $primary-light;
  box-shadow: $shadow-sm;
}

.story-name {
  font-size: $font-size-xs;
  color: $text-primary;
  margin-top: $spacing-xs;
}

.story-live {
  position: absolute;
  bottom: 18rpx;
  left: 50%;
  transform: translateX(-50%);
  background: #ff4d4f;
  color: #fff;
  font-size: $font-size-xs;
  border-radius: $border-radius-sm;
  padding: 2rpx $spacing-sm;
}

.community-feed {
  margin: $spacing-md 0 0 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-lg;
}

.feed-card {
  width: 92vw;
  background: $card-background;
  border-radius: $border-radius-xl;
  box-shadow: $shadow-sm;
  padding: $spacing-lg;
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
  transition: transform $transition-fast;
  
  &:active {
    transform: scale(0.98);
  }
}

.feed-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
}

.feed-avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  border: 4rpx solid $primary-light;
  box-shadow: $shadow-sm;
}

.feed-userinfo {
  display: flex;
  flex-direction: column;
  gap: 2rpx;
}

.feed-username {
  font-size: $font-size-sm;
  color: $text-primary;
  font-weight: 600;
}

.feed-desc {
  font-size: $font-size-xs;
  color: $text-secondary;
}

.feed-tag {
  background: #ff4d4f;
  color: #fff;
  font-size: $font-size-xs;
  border-radius: $border-radius-sm;
  padding: 2rpx $spacing-sm;
  margin-left: auto;
}

.feed-content {
  font-size: $font-size-md;
  color: $text-primary;
  margin: $spacing-xs 0 0 0;
  line-height: 1.6;
}

.feed-img {
  width: 100%;
  border-radius: $border-radius-lg;
  margin-top: $spacing-xs;
}

.feed-actions {
  display: flex;
  gap: $spacing-lg;
  margin-top: $spacing-xs;
}
</style> 
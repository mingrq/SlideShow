# 最新版本
 2.0.0

#

### 使用
#
```
在 build.gradle 中添加
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.mingrq:SlideShow:Tag'
	}
```
## 方法

### _初始化_

#
### SlideShow setIndicatorMargin(float indicatorMargin)
#### 设置指示器间距
#
indicatorMargin ：指示器间距 ``单位：px``


### SlideShow setIndicatorSize(float indicatorSize)
#### 设置指示器大小
#

### SlideShow setIndicatorBgColor(int indicatorBgColor)
#### 设置指示器背景色
#

### SlideShow setIndicatorHeight(float indicatorHeight)
#### 设置指示器区域高度
#

### SlideShow setSlideTime(int slideTime)
#### 设置滚动时间
#

### SlideShow setIndicatorSelecter(int indicatorSelecter)
#### 设置指示器图标选择器
#

### SlideShow setData(List<String> infoList)
#### 设置轮播图数据
#

### SlideShow setSlideOnItemClickLisenter(OnItemClickLisenter onItemClickLisenter)
#### 设置点击监听
#

### SlideShow setSlideOnItemTouchLisenter(OnItemTouchLisenter onItemTouchLisenter)
#### 设置滑动监听
#

### int dp2px(float dpValues)
#### dp转px
#

### void commit() 
#### 提交
#

### _操作_

### void startSlide()
#### 开始轮播
#

### void cancelSlide()
#### 停止轮播
#

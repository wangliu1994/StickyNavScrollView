# 通用滚动悬浮菜单栏控件
### 一、悬浮吸顶的StickyNavScrollView
#### 1、说明：
由一个HeadView(View)，一个TabBar(View)和一个底部TabView(View)构成
#### 2、注意：
##### 本次是使用嵌套滑动的机制实现的，所以底部TabView要使用支持嵌套滑动的控件（不必直接，子布局中包含嵌套滑动的控件就行），列表建议使用RecyclerView，普通布局建议使用 StickyNestScrollChildLayout。
#### 3、使用方法：
##### 1）在XML中设置HeadView与，TabBar与TabView，在java文件中获取对应的View
- 这里注意三个View的设置属性分别为为navHeadView，navTabBar，navTabView，摆放顺序可以变，其内部会按照预期的顺序摆放，如下：

```java
<com.winnie.library.stickynav.view.StickyNavScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:sticky="http://schemas.android.com/apk/res-auto"
    android:id="@+id/sticky_nav_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    sticky:navHeadView="@layout/view_sticky_nav_head"
    sticky:navTabBar="@layout/view_sticky_tab_bar"
    sticky:navTabView="@layout/view_pager" />
```

```java
StickyNavScrollView mScrollView = (StickyNavScrollView) findViewById(R.id.sticky_nav_layout);
View headView = mScrollView.getHeadView();
View tabBar = mScrollView.getTabBar();
mViewPager = (ViewPager) mScrollView.getTabView();
```


##### 2）在java中设置HeadView与，TabBar与TabView
```java
FrameLayout layout = (FrameLayout) findViewById(R.id.sticky_nav_layout);
StickyNavScrollView mScrollView = new StickyNavScrollView(this);
View mHeadView = LayoutInflater.from(this).inflate(R.layout.view_sticky_nav_head, null);
View mTabBar = LayoutInflater.from(this).inflate(R.layout.view_sticky_tab_bar, null);
mViewPager = new ViewPager(this);
mViewPager.setId(R.id.sticky_nav_tab_view);
mScrollView.setHeadView(mHeadView);
mScrollView.setTabBar(mTabBar);
mScrollView.setTabView(mViewPager);
layout.addView(mScrollView);
```
### 二、悬浮吸顶的StickyNavScrollLayout
#### 1、使用方法：
##### 1）在XML中设置HeadView与，TabBar与TabView，在java文件中获取对应的View。
- 这里注意三个View的id一定要设置为sticky_nav_head_view，sticky_nav_tab_bar，sticky_nav_tab_view，
- 摆放顺序可以变，其内部会按照预期的顺序摆放，但是如果id不是以上三个，会被默认按顺序放在HeadView的容器里

```java
<com.winnie.library.stickynav.layout.StickyNavScrollLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/sticky_nav_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout
        android:id="@id/sticky_nav_head_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
    </LinearLayout>
    <TextView
        android:id="@id/sticky_nav_tab_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#eeeeeeee"
        android:gravity="center"
        android:padding="10dp"
        android:text="软件介绍"
        android:textSize="20sp"
        android:textStyle="bold" />
    <android.support.v4.view.ViewPager
        android:id="@id/sticky_nav_tab_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</com.winnie.stickynav.layout.StickyNavScrollLayout>
```

##### 2）在java中设置HeadView与，TabBar与TabView
- 这里注意三个View的id一定要设置为sticky_nav_head_view，sticky_nav_tab_bar，sticky_nav_tab_view，
- 摆放顺序可以变，其内部会按照预期的顺序摆放，但是如果id不是以上三个，会被默认按顺序放在HeadView的容器里

```java
FrameLayout layout = (FrameLayout) findViewById(R.id.sticky_nav_layout);
StickyNavScrollLayout mScrollView = new StickyNavScrollLayout(this);
View mHeadView = LayoutInflater.from(this).inflate(R.layout.view_sticky_nav_head, null);
 mHeadView.setId(R.id.sticky_nav_head_view);
View mTabBar = LayoutInflater.from(this).inflate(R.layout.view_sticky_tab_bar, null);
mTabBar.setId(R.id.sticky_nav_tab_bar);
mViewPager = new ViewPager(this);
mViewPager.setId(R.id.sticky_nav_tab_view);
mScrollView.addView(mHeadView);
mScrollView.addView(mTabBar);
mScrollView.addView(mViewPager);
layout.addView(mScrollView);
```

### 三、 下拉刷新
#### 1、使用方法
##### StickyNavScrollLayout替换为对应的StickyPullToRefreshLayout，StickyNavScrollView替换为对应的StickyPullToRefreshView  即可

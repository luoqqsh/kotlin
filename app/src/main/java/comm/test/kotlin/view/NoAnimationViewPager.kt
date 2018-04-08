package comm.test.kotlin.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoAnimationViewPager : ViewPager {
    private val DISABLE = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    //去除页面切换时的滑动翻页效果
    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, false)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return DISABLE && super.onInterceptTouchEvent(arg0)
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return DISABLE && super.onTouchEvent(arg0)
    }

}

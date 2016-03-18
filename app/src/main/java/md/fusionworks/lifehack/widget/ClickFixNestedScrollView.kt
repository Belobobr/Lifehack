package md.fusionworks.lifehack.widget

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

class ClickFixNestedScrollView(context: Context, attrs: AttributeSet) : NestedScrollView(context,
    attrs) {

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    if (ev.action == MotionEvent.ACTION_DOWN) {
      // Explicitly call computeScroll() to make the Scroller compute itself
      computeScroll()
    }
    return super.onInterceptTouchEvent(ev)
  }
}
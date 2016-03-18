package md.fusionworks.lifehack.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * A [FrameLayout] that will always be square -- same width and height,
 * where the height is based off the width.
 */
class SquareFrameLayout : FrameLayout {

  constructor(context: Context) : super(context) {
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr) {
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
      context, attrs, defStyleAttr, defStyleRes) {
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // Set a square layout.
    super.onMeasure(widthMeasureSpec, widthMeasureSpec)
  }
}
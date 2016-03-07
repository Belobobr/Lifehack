package md.fusionworks.lifehack.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.TypefaceCache

class TypefaceTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : TextView(
    context, attrs, defStyle) {

  init {

    val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, 0, 0)
    try {
      val font = ta.getString(R.styleable.TypefaceTextView_typeface)
      val typeface = TypefaceCache.getTypeface(context, font)
      if (null != typeface) {
        setTypeface(typeface)
      }
    } finally {
      ta.recycle()
    }
  }
}
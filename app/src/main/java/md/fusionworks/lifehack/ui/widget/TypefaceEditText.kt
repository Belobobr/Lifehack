package md.fusionworks.lifehack.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.TypefaceCache

class TypefaceEditText : EditText {

  constructor(context: Context) : super(context) {
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

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

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs,
      defStyle) {

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
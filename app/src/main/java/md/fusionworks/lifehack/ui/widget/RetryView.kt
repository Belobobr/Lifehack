package md.fusionworks.lifehack.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.retry_view.view.*
import md.fusionworks.lifehack.R

/**
 * Created by ungvas on 2/28/16.
 */
class RetryView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs,
    R.attr.retryViewStyle) {

  private var onRetryActionListener: OnRetryActionListener? = null

  fun setOnRetryActionListener(onRetryActionListener: OnRetryActionListener) {
    this.onRetryActionListener = onRetryActionListener
  }

  init {

    View.inflate(context, R.layout.retry_view, this)

    val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.RetryView, 0, 0)
    try {
      val message = ta.getString(R.styleable.RetryView_message)
      messageField.text = message
    } finally {
      ta.recycle()
    }

    retryButton.setOnClickListener { v -> if (onRetryActionListener != null) onRetryActionListener!!.onRetryAction() }

    hide()
  }

  fun show() {
    visibility = View.VISIBLE
  }

  fun hide() {
    visibility = View.GONE
  }

  interface OnRetryActionListener {
    fun onRetryAction()
  }
}

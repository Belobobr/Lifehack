package md.fusionworks.lifehack.util

import android.os.Build
import android.view.View

/**
 * Created by ungvas on 3/10/16.
 */

fun View.setAccessibilityIgnore() {
  isClickable = false
  isFocusable = false
  contentDescription = ""
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
  }
}
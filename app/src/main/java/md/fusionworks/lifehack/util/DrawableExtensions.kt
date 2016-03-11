package md.fusionworks.lifehack.util

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.graphics.drawable.DrawableCompat

/**
 * Created by ungvas on 3/11/16.
 */

fun Drawable.tint(@ColorRes color: Int) {
  DrawableCompat.setTint(this, color)
}
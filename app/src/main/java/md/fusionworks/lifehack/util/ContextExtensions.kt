import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(res: Int): Drawable = ContextCompat.getDrawable(this, res)
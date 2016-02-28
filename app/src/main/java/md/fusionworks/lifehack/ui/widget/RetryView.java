package md.fusionworks.lifehack.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;

/**
 * Created by ungvas on 2/28/16.
 */
public class RetryView extends RelativeLayout {

  @Bind(R.id.messageField) TextView messageField;
  @Bind(R.id.retryButton) Button retryButton;

  private OnRetryActionListener onRetryActionListener;

  public void setOnRetryActionListener(OnRetryActionListener onRetryActionListener) {
    this.onRetryActionListener = onRetryActionListener;
  }

  public RetryView(Context context, AttributeSet attrs) {
    super(context, attrs, R.attr.retryViewStyle);

    inflate(context, R.layout.new_retry_view, this);
    ButterKnife.bind(this);

    TypedArray ta =
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.RetryView, 0, 0);
    try {
      String message = ta.getString(R.styleable.RetryView_message);
      messageField.setText(message);
    } finally {
      ta.recycle();
    }

    retryButton.setOnClickListener(v -> {
      if (onRetryActionListener != null) onRetryActionListener.onRetryAction();
    });

    hide();
  }

  public void show() {
    setVisibility(VISIBLE);
  }

  public void hide() {
    setVisibility(GONE);
  }

  public interface OnRetryActionListener {
    void onRetryAction();
  }
}

package nl.fnord.android.listviewoverdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * A list view for use with list items that have their own background drawable. Such list views
 * suffer from GPU Overdraw of the item background on top of the list view (ancestor) background.
 * <p>
 * This subclass detects when its data set contains enough elements to fill all available space
 * and start scrolling. If this is the case, it sets its own background to transparent.
 * </p>
 * <p><strong>Limitation:</strong> Header and Footer views are ignored. If the list view has few
 * enough items that it wouldn't scroll, but a header and/or footer are big enough to cause it to
 * scroll anyway, the background is not hidden and overdraw is present.
 * </p>
 * <p>Source: http://stackoverflow.com/q/15625930/49489 CC-BY-SA</p>
 */
public class BackgroundListView extends ListView {

    /** We need our own instance, because it's used as a sentinel value later on. */
    private static final Drawable TRANSPARENT = new ColorDrawable(0x00000000);

    /** If true, the next call to {@code onDraw(Canvas)} evaluates whether to show or hide the background. */
    private boolean mNeedsBackgroundCheck = true;

    /** The background to restore if the list shrinks. */
    private Drawable mOriginalBackground;

    public BackgroundListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BackgroundListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackgroundListView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mOriginalBackground = this.getBackground();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        Drawable newBackground = getBackground();
        if (newBackground != TRANSPARENT) {
            this.mOriginalBackground = newBackground;
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        Drawable newBackground = getBackground();
        if (newBackground != TRANSPARENT) {
            this.mOriginalBackground = newBackground;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mNeedsBackgroundCheck) {
            maybeHideBackground();
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mNeedsBackgroundCheck = true;
    };

    @Override
    protected void handleDataChanged() {
        super.handleDataChanged();
        mNeedsBackgroundCheck = true;
    }

    private void maybeHideBackground() {
        if (isInEditMode()) {
            return;
        }
        final int maxPosition = getAdapter().getCount() - 1;
        final int firstVisiblePosition = getFirstVisiblePosition();
        final int lastVisiblePosition = getLastVisiblePosition();
        if (firstVisiblePosition > 0 || lastVisiblePosition < maxPosition) {
            setBackground(TRANSPARENT);
        } else {
            setBackground(mOriginalBackground);
        }
        mNeedsBackgroundCheck = false;
    }
}

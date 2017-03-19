package com.lj.timecounter.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.Chronometer;
import android.widget.TextView;

import com.lj.timecounter.R;

/**
 * Modified by langjian@qiyi.com on 2017/2/17.
 * Custom Class from android.widget.Chronometer
 * @attr ref android.R.styleable#Chronometer_countDown
 */
public class CustomChronometer extends TextView {

    private static final String TAG = CustomChronometer.class.getSimpleName();

    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnChronometerTickListener {

        /**
         * Notification that the chronometer has changed.
         */
        void onChronometerTick(CustomChronometer chronometer);

        /**
         * Notification that the chronometer has completed.
         */
        void onChronometerTickComplete(CustomChronometer chronometer);

        /**
         * format time by user.
         */
        String onFormatTime(long elapsedSeconds);
    }
    private long mNextSeconds = -1;//V8.3 add custom attr next second of counter
    private long mLimit;//V8.3 add minimum or maximum limit.
    private long mBase;
    private long mNow; // the currently displayed time
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private String mFormat;
    private StringBuilder mFormatBuilder;
    private OnChronometerTickListener mOnChronometerTickListener;
    private StringBuilder mRecycle = new StringBuilder(8);
    private boolean mCountDown;

    /**
     * Initialize this Chronometer object.
     * Sets the base to the current time.
     */
    public CustomChronometer(Context context) {
        this(context, null, 0);
    }

    /**
     * Initialize with standard view layout information.
     * Sets the base to the current time.
     */
    public CustomChronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Initialize with standard view layout information and style.
     * Sets the base to the current time.
     */
    public CustomChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomChronometer, defStyleAttr, 0);
        setCountDown(a.getBoolean(R.styleable.CustomChronometer_countDown, true));
        a.recycle();

        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void setLimit(long limit) {
        mLimit = limit;
    }

    /**
     * Set this view to count down to the base instead of counting up from it.
     *
     * @param countDown whether this view should count down
     *
     * @see #setBase(long)
     */
    public void setCountDown(boolean countDown) {
        mCountDown = countDown;
        updateText(SystemClock.elapsedRealtime());
    }

    /**
     * @return whether this view counts down
     *
     * @see #setCountDown(boolean)
     */
    public boolean isCountDown() {
        return mCountDown;
    }

    /**
     * Set the time that the count-up timer is in reference to.
     *
     * @param base Use the {@link SystemClock#elapsedRealtime} time base.
     */
    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    /**
     * Return the base time as set through {@link #setBase}.
     */
    public long getBase() {
        return mBase;
    }

    /**
     * Sets the format string used for display.  The Chronometer will display
     * this string, with the first "%s" replaced by the current timer value in
     * "MM:SS" or "H:MM:SS" form.
     *
     * If the format string is null, or if you never call setFormat(), the
     * Chronometer will simply display the timer value in "MM:SS" or "H:MM:SS"
     * form.
     *
     * @param format the format string.
     */
    public void setFormat(String format) {
        mFormat = format;
        if (format != null && mFormatBuilder == null) {
            mFormatBuilder = new StringBuilder(format.length() * 2);
        }
    }

    /**
     * Returns the current format string as set through {@link #setFormat}.
     */
    public String getFormat() {
        return mFormat;
    }

    /**
     * Sets the listener to be called when the chronometer changes.
     *
     * @param listener The listener.
     */
    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    /**
     * @return The listener (may be null) that is listening for chronometer change
     *         events.
     */
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    /**
     * Start counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     *
     * Chronometer works by regularly scheduling messages to the handler, even when the
     * Widget is not visible.  To make sure resource leaks do not occur, the user should
     * make sure that each start() call has a reciprocal call to {@link #stop}.
     */
    public void start() {
        mStarted = true;
        updateRunning();
    }

    /**
     * Stop counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     *
     * This stops the messages to the handler, effectively releasing resources that would
     * be held as the chronometer is running, via {@link #start}.
     */
    public void stop() {
        mStarted = false;
        updateRunning();
    }

    /**
     * The same as calling {@link #start} or {@link #stop}.
     * @hide pending API council approval
     */
    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {
        mNow = now;
        mNextSeconds = mCountDown ? mBase - now : now - mBase;
        mNextSeconds /= 1000;
        boolean negative = false;
        if (mNextSeconds < 0) {
            mNextSeconds = -mNextSeconds;
            negative = true;
        }
        String text = DateUtils.formatElapsedTime(mRecycle, mNextSeconds);
        if (negative) {
            text = getResources().getString(R.string.negative_duration, text);
        }

        if (mFormat != null) {
            if (mOnChronometerTickListener != null) {
                text = mOnChronometerTickListener.onFormatTime(mNextSeconds);
            }
        }
        setText(text);
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                postDelayed(mTickRunnable, 1000);
            } else {
                removeCallbacks(mTickRunnable);
            }
            mRunning = running;
        }
    }

    private final Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                if(mNextSeconds == mLimit){
                    stop();
                    dispatchChronometerTickComplete();
                    return;
                }
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                postDelayed(mTickRunnable, 1000);
            }
        }

    };

    private void dispatchChronometerTickComplete() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTickComplete(this);
        }
    }

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    private static final int MIN_IN_SEC = 60;
    private static final int HOUR_IN_SEC = MIN_IN_SEC*60;
    private static String formatDuration(long ms) {
        final Resources res = Resources.getSystem();
        final StringBuilder text = new StringBuilder();

        int duration = (int) (ms / DateUtils.SECOND_IN_MILLIS);
        if (duration < 0) {
            duration = -duration;
        }

        int h = 0;
        int m = 0;

        if (duration >= HOUR_IN_SEC) {
            h = duration / HOUR_IN_SEC;
            duration -= h * HOUR_IN_SEC;
        }
        if (duration >= MIN_IN_SEC) {
            m = duration / MIN_IN_SEC;
            duration -= m * MIN_IN_SEC;
        }
        int s = duration;

        try {
            if (h > 0) {
                text.append(res.getQuantityString(
                        R.plurals.duration_hours, h, h));
            }
            if (m > 0) {
                if (text.length() > 0) {
                    text.append(' ');
                }
                text.append(res.getQuantityString(
                        R.plurals.duration_minutes, m, m));
            }

            if (text.length() > 0) {
                text.append(' ');
            }
            text.append(res.getQuantityString(
                    R.plurals.duration_seconds, s, s));
        } catch (Resources.NotFoundException e) {
            // Ignore; plurals throws an exception for an untranslated quantity for a given locale.
            return null;
        }
        return text.toString();
    }

    @Override
    public CharSequence getContentDescription() {
        return formatDuration(mNow - mBase);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return Chronometer.class.getName();
    }
}
package com.example.xunwang.bbcomm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.xunwang.bbcomm.R;

/**
 * Created by xunwang on 16/8/22.
 */
public class CommButtonView extends Button {
    private Drawable bg;
    private Paint paint;

	public CommButtonView(Context context) {
		super(context);
        init(context);
	}

	public CommButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init(context);
	}

	public CommButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        init(context);
	}

    private void init(Context context) {
        bg = context.getApplicationContext().getResources().getDrawable(R.drawable.audio_play_bg);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(((BitmapDrawable)bg).getBitmap(), 0, 0 , paint);
    }
}

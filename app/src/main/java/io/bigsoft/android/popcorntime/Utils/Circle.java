package io.bigsoft.android.popcorntime.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import io.bigsoft.android.popcorntime.R;

    /**
     *  @https://stackoverflow.com/questions/29381474/how-to-draw-a-circle-with-animation-in-android-with-circle-size-based-on-a-value
     */

    public class Circle extends View {

        private static final int START_ANGLE_POINT = -90;

        private final Paint paint;
        private RectF rect;
        private final int strokeWidth;

        private float angle;

        public Circle(Context context, AttributeSet attrs) {
            super(context, attrs);

            strokeWidth = 32;

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            //Circle color
            paint.setColor(getResources().getColor(R.color.rating_yellow));

            //size 200x200 example

            //Initial Angle (optional, it can be zero)
            angle = 0;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            rect = new RectF(strokeWidth, strokeWidth, getWidth() - strokeWidth, getHeight() - strokeWidth);
            canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }


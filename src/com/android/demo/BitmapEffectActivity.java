package com.android.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.android.demo.bitmap.BitmapEffect;

public class BitmapEffectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
    
    class MyView extends View {

        private Bitmap mBitmap = null;
        private int mBmpWidth = 0;
        private int mBmpHeight = 0;
        private Rect mSrcRect;
        private Rect mDstRect;
        private Matrix mMatrix;
        public MyView(Context context) {
            super(context);
            init();
        }

        private void init() {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.fengjie);
            mBitmap = BitmapEffect.createReflectedImage(bmp);
            mBmpWidth = mBitmap.getWidth();
            mBmpHeight = mBitmap.getHeight();
            mMatrix = new Matrix();
            mMatrix.preScale(0.5f, 0.5f, mBmpWidth/2, mBmpHeight/2);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            //canvas.drawBitmap(mBitmap, 0, 0, mBmpWidth, mBmpHeight, null);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }
}

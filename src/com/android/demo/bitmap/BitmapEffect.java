package com.android.demo.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;

public class BitmapEffect {

    public static Bitmap createReflectedImage(Bitmap originalImage) {
        // The gap we want between the reflection and the original image
        final boolean skew = true;
        final int reflectionGap = 4;
        final int reflectionSkew = skew ? 150 : 0;
        
        final Paint drawPaint = new Paint();
        drawPaint.setColor(Color.RED);
        drawPaint.setStyle(Style.STROKE);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int reflectedWidth = width;
        int reflectedHeight = height / 2;

        // This will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        // Create a Bitmap with the flip matrix applied to it.
        // We only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width,
                height / 2, matrix, false);

        // Create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width + reflectionSkew, (height + height / 2),
                Config.ARGB_8888);

        // Create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // Draw in the original image
        canvas.drawBitmap(originalImage, reflectionSkew, 0, null);
        // Draw in the gap
        Paint defaultPaint = new Paint();
        canvas.drawRect(reflectionSkew, height, width + reflectionSkew, height + reflectionGap, defaultPaint);
        // Draw in the reflection
        if (skew) {
            Matrix m = new Matrix();
            m.preTranslate(reflectionSkew, height + reflectionGap);
            m.preSkew(-1.1f, 0f);
            m.preScale(1.0f, 0.5f);
            canvas.drawBitmap(reflectionImage, m, null);
        } else {
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        }

        // Create a shader that is a linear gradient that covers the reflection
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                TileMode.CLAMP);
        // Set the paint to use this shader (linear gradient)
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width + reflectionSkew, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }
}

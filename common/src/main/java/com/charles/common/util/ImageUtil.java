package com.charles.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

public class ImageUtil {
    public static Bitmap rsBlur(Context context, Bitmap source, int radius){
        if (radius > 25) {
            return source;
        }

        //(1)
        RenderScript renderScript =  RenderScript.create(context);

        Log.i("ImageUtil.rsBlur","scale size:"+ source.getWidth()+"*"+ source.getHeight());

        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript, source);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(source);
        //(8)
        renderScript.destroy();

        return source;
    }
}

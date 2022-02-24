// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint$Align;
import com.logitech.ue.views.UEDeviceView;
import android.view.View;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.view.View$DragShadowBuilder;

public class DeviceDragShadowBuilder extends View$DragShadowBuilder
{
    int mCloudBottomPadding;
    Rect mCloudBounds;
    int mCloudPadding;
    int mDeviceColor;
    Drawable mDeviceDrawable;
    Point mDeviceRenderSize;
    String mLabel;
    NinePatchDrawable mNinePatchCloud;
    Paint mPaint;
    int mTextSize;
    
    public DeviceDragShadowBuilder(final Context context, final String mLabel, final int mDeviceColor) {
        super((View)null);
        this.mLabel = mLabel;
        this.mDeviceColor = mDeviceColor;
        this.mCloudPadding = context.getResources().getDimensionPixelSize(2131361871);
        this.mCloudBottomPadding = context.getResources().getDimensionPixelSize(2131361870);
        this.mTextSize = context.getResources().getDimensionPixelSize(2131361872);
        final Point deviceMaxDimensions = UEDeviceView.getDeviceMaxDimensions(UEDeviceView.DisplayMode.MODE_SMALL, context);
        this.mDeviceRenderSize = UEDeviceView.calculateDevicesSize(mDeviceColor, (float)deviceMaxDimensions.x, (float)deviceMaxDimensions.y, context);
        (this.mPaint = new Paint()).setTextSize((float)this.mTextSize);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint$Align.CENTER);
        this.mPaint.setFakeBoldText(true);
        this.mDeviceDrawable = UEDeviceView.getDeviceFrontImage(context, mDeviceColor, UEDeviceView.DisplayMode.MODE_SMALL);
        final Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), 2130837595);
        this.mNinePatchCloud = new NinePatchDrawable(context.getResources(), decodeResource, decodeResource.getNinePatchChunk(), (Rect)null, (String)null);
    }
    
    public void onDrawShadow(final Canvas canvas) {
        if (this.mLabel != null) {
            this.mNinePatchCloud.setBounds(this.mCloudBounds);
            this.mNinePatchCloud.draw(canvas);
            this.mPaint.setColor(-1);
            canvas.drawText(this.mLabel, (float)(canvas.getWidth() / 2), (float)(this.mCloudPadding + this.mTextSize), this.mPaint);
        }
        this.mDeviceDrawable.setBounds((canvas.getWidth() - this.mDeviceRenderSize.x) / 2, canvas.getHeight() - this.mDeviceRenderSize.y, (canvas.getWidth() + this.mDeviceRenderSize.x) / 2, canvas.getHeight());
        this.mDeviceDrawable.draw(canvas);
    }
    
    public void onProvideShadowMetrics(final Point point, final Point point2) {
        this.mCloudBounds = new Rect();
        if (this.mLabel != null) {
            this.mPaint.getTextBounds(this.mLabel, 0, this.mLabel.length(), this.mCloudBounds);
            this.mCloudBounds.top = 0;
            this.mCloudBounds.bottom = Math.round(this.mPaint.getTextSize() + this.mCloudBottomPadding + this.mCloudPadding * 2);
            this.mCloudBounds.left = 0;
            final Rect mCloudBounds = this.mCloudBounds;
            mCloudBounds.right += this.mCloudBottomPadding;
            this.mNinePatchCloud.setBounds(this.mCloudBounds);
        }
        point.set(Math.max(this.mCloudBounds.width(), this.mDeviceRenderSize.x), this.mDeviceRenderSize.y + this.mCloudBounds.height());
        point2.set(point.x / 2, point.y - this.mDeviceRenderSize.y / 2);
    }
}

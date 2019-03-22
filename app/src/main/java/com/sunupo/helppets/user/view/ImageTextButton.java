package com.sunupo.helppets.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.Button;

import com.sunupo.helppets.R;


public class ImageTextButton extends Button {
    private float mDrawableWidth;
    private float mDrawableHight;
    private float mDrawableSpace;
    private Bitmap mBitmap;
    private float mTxtSize;

    public ImageTextButton(Context context) {
        super(context, null);
    }


    public ImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(context, attributeSet);
    }


    public void init(Context context, AttributeSet attributeSet) {
        //获取所需的控件参数
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
                R.styleable.ImageTextButton);
        int imgId = typedArray.getResourceId(
                R.styleable.ImageTextButton_myDrawableTop, 0);
        //如果没指定img直接返回
        if (imgId == 0)
            return;
        mTxtSize = this.getTextSize();
        //可以考虑将默认值设置到配置文件中
        mDrawableSpace = typedArray.getDimension(
                R.styleable.ImageTextButton_drawable_space, 0);
        mDrawableHight = typedArray.getDimension(
                R.styleable.ImageTextButton_drawable_hight, mTxtSize);
        mDrawableWidth = typedArray.getDimension(
                R.styleable.ImageTextButton_drawable_width, mTxtSize);

        //获得位图
        mBitmap = BitmapFactory.decodeResource(getResources(), imgId);

        //下面两行可以考虑删除，然后手动设置android:layout_height
        this.setHeight( (int)(mDrawableHight > mTxtSize ? mDrawableHight : mTxtSize ) );
        //虽然知道这个时候getMeasuredWidth()的值是0，还是设置一下
        this.setWidth((int)(mDrawableWidth+(getText().length()+1)*mTxtSize));
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //缩放位图,不要在setImg方法中使用这个方法
        Bitmap b = zoomImage(mBitmap, mDrawableWidth, mDrawableHight);
        int h = (int) ((this.getMeasuredHeight() - b.getHeight()) * 0.5);
        canvas.drawBitmap(b, 0, h, null);
        // 文本显示
        canvas.translate((int)(mTxtSize/2), 0);
        super.onDraw(canvas);
    }
}

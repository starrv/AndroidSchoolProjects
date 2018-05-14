package lehman.android;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Owner on 4/18/2015.
 */
public class FanView extends View
{
    int tempColor;
    float textSize;
    String title,temp,wind,weather;
    Paint tempPaint,tempPaint2;
    Bitmap fanBlades;
    Drawable fanPole;
    Matrix matrix=new Matrix();
    Rect rectFanPole;
    final int SIZE=40;

    public FanView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FanView,
                0, 0);

        try {
            title = a.getString(R.styleable.FanView_header);
            tempColor = a.getColor(0, R.styleable.FanView_textColor);
            textSize=a.getFloat(4, R.styleable.FanView_textSize);
            fanBlades = BitmapFactory.decodeResource(context.getResources(), R.drawable.fan_blades);
            fanPole = a.getDrawable(R.styleable.FanView_fanPole);
            temp="65 F";
            wind="N 10";
            weather="Sunny";
        } finally {
            a.recycle();
        }
        init();
    }

    private void init()
    {
        tempPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        tempPaint.setColor(tempColor);
        tempPaint.setTextSize(textSize);

        tempPaint2=new Paint(Paint.ANTI_ALIAS_FLAG);
        tempPaint2.setColor(tempColor);
        tempPaint2.setTextSize(new Float(0.6*textSize));

        rectFanPole=new Rect(135,370,168,530);
        fanPole.setBounds(rectFanPole);
       // RectF rect2=new RectF(100,340,420,180);

       matrix.postTranslate(90,295);

    }
    @Override
    protected void onDraw(Canvas c)
    {
        c.drawText(title, 100,100,tempPaint);
        c.drawText(temp,100,130,tempPaint2);
        c.drawText(weather,100,160,tempPaint2);
        c.drawText(wind,100,190,tempPaint2);
        c.drawBitmap(fanBlades,matrix, tempPaint2);
        fanPole.draw(c);
        if(Lab2Fragment.flag)        setAngle(Lab2Fragment.change);
    }

    public void setText(String text) {
        this.title = text;
        invalidate();
        requestLayout();
    }

    public String getText() {
        return title;
    }

    public void setColor(int color) {
        this.tempColor=color;
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return tempColor;
    }
    public void setAngle(float angle)
    {
       // this.angle+=(angle%360);
        matrix.setRotate(angle,60,100);
        invalidate();
        requestLayout();
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
/**
 *
 * @author burke
 */
public class GraphicButton {

    private int upperLeftX;
    private int upperLeftY;
    private int lowerRightX;
    private int lowerRightY;
    private int width, height;

    private int hTextOffset, vTextOffset;

    private String label;
    private int textColor, buttonColor;

    private Paint paint;

    public GraphicButton(int x, int y, int w, int h, String strLabel)
    {
        width = w;
        height = h;

        upperLeftX = x;
        upperLeftY = y;
        lowerRightX = x + w;
        lowerRightY = x + h;

        buttonColor = Color.GRAY;
        textColor = Color.BLACK;
        label = strLabel;

        hTextOffset = 15;
        vTextOffset = (height / 3);

        paint = new Paint();
    }

    public GraphicButton(int x, int y, int w, int h, int horizTextOffset, int vertTextOffset, String strLabel)
    {
        width = w;
        height = h;

        upperLeftX = x;
        upperLeftY = y;
        lowerRightX = x + w;
        lowerRightY = x + h;

        buttonColor = Color.GRAY;
        textColor = Color.BLACK;
        label = strLabel;

        hTextOffset = horizTextOffset;

        paint = new Paint();
    }

    public void draw(Canvas c)
    {
        paint.setColor(buttonColor);
        c.drawRect(this.getRect(), paint);

        paint.setColor(textColor);
        c.drawText(label, upperLeftX + hTextOffset, upperLeftY + vTextOffset, paint);
    }

    public void setLabel(String s)
    {
        label = s;
    }

    public String getLabel()
    {
        return label;
    }

    public RectF getRect()
    {
        return new RectF(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
    }


}

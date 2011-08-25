/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

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

    public GraphicButton(int x, int y, int width, int height)
    {
        upperLeftX = x;
        upperLeftY = y;
        lowerRightX = x + width;
        lowerRightY = x + height;
    }

    public RectF getRect()
    {
        return new RectF(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
    }


}

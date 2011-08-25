/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

import java.util.LinkedList;
import android.graphics.RectF;

/**
 *
 * @author burke
 */
public class BlockSet {

    private LinkedList<Block> lstBlocks;

    public BlockSet()
    {
        lstBlocks = new LinkedList<Block>();
    }

    public BlockSet(int xPos, int yPos, int rows, int columns, int spacing)
    {
        int x = xPos;
        int y = yPos;
        int width = 0;
        lstBlocks = new LinkedList<Block>();
        Block b;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                b = new Block(x, y);
                lstBlocks.add(b);
                y += b.getHeight();
                y += spacing;
                width = b.getWidth();
            }
            x += (width + spacing);
            y = yPos;
        }
    }

    public void addBlock(int x, int y)
    {
        Block b = new Block(x, y);
        lstBlocks.add(b);
    }

    public RectF getBounds()
    {
        float minX = 0;
        float maxX = 0;
        float minY = 0;
        float maxY = 0;

        for (Block a : lstBlocks)
        {
            if (a.getRect().left < minX)
                minX = a.getRect().left;
            if (a.getRect().right > maxX)
                maxY = a.getRect().right;
            if (a.getRect().top < minY)
                minY = a.getRect().top;
            if (a.getRect().bottom > maxY)
                maxY = a.getRect().bottom;
        }

        return new RectF(minX, minY, maxX, maxY);
    }

    public LinkedList<Block> getBlocks()
    {
        return lstBlocks;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

/**
 *
 * @author burke
 */
public class Block {

    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private String color;

    public Block(int x, int y)
    {
            xPos = x;
            yPos = y;
            width = 5;
            height = 5;
            color = "RED";
    }

}

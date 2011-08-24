/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.qpad;

/**
 *
 * @author burke
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
//import android.graphics.Typeface;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.location.*;
import android.os.Bundle;
import java.util.LinkedList;
import java.lang.Math;
import android.content.Intent;

public class GameView extends View {
  private boolean debug = false;

   private boolean drawGame = true;
   private int paddleBottomMargin = 20;
   private int score = 0;
   private int xMin = 0;          // This view's bounds
   private int xMax;
   private int yMin = 0;
   private int yMax;
   private float ballRadius = 4; // Ball's radius
   private float ballX = ballRadius + 50;  // Ball's center (x,y)
   private float ballY = ballRadius + 75;
   private float ballSpeedX = 2;  // Ball's speed (x,y)
   private float ballSpeedY = 2;
   private float paddleX = 20;
   private float paddleY = 20;
   //private float paddleYFixed;
   //private float paddleXFixed;
   private float paddleWidth = 5f;
   private float paddleLength = 100f;
   private RectF ballBounds;      // Needed for Canvas.drawOval
   private Paint paint;           // The paint (e.g. style, color) used for drawing
   //LocationManager locationManager;
   //LocationListener locationListener;
   private RectF paddleLeft, paddleRight, paddleBottom, paddleTop;

   private float previousX;
   private float previousY;
   private Double latitude = 0.0;
   private Double drawLat = 0.0;
   private Double longitude = 0.0;
   private Double drawLong = 0.0;
   private Double altitude = 0.0;
   private float speed = 0.0f;
   private String exStr = " ";
   Context parentContext;

   private LinkedList<Block> lstBlocks;



   // Constructor
   public GameView(Context context) {
      super(context);
      parentContext = context;

      drawGame = true;
      lstBlocks = new LinkedList<Block>();

      ballX = 40;
      ballY = 20;

      int startX = 80;
      int startY = 100;
      int width = 0;
      int blkX = startX;
      int blkY = startY;

      for (int i = 0; i < 10; i++)
      {
          Block b;
          for (int j = 0; j < 10; j++)
          {
              b = new Block(blkX, blkY);
              lstBlocks.add(b);
              blkY += b.getHeight() + 1;
              width = b.getWidth();
          }
          blkX += width + 1;
          blkY = startY;
      }
      

      ballBounds = new RectF();
      paddleLeft = new RectF();
      paddleRight = new RectF();
      paddleTop = new RectF();
      paddleBottom = new RectF();

      paint = new Paint();
      this.setFocusable(true);
      this.requestFocus();
      this.setFocusableInTouchMode(true);

   }

   private void detectBallCollisions()
   {
       boolean changeX = false;
       boolean changeY = false;
       
       if(ballBounds.intersect(paddleTop) || ballBounds.intersect(paddleBottom))
       {
           changeX = false;
           ballSpeedY *= -1;
           if (ballSpeedX > 0 && ballX < (paddleTop.left + (paddleLength / 4.5)))
           {
               changeX = true;
           }
           else if (ballSpeedX < 0 && ballX > (paddleTop.right - (paddleLength / 4.5)))
           {
               changeX = true;
           }

           if(changeX)
           {
               ballSpeedX *= -1;
           }
       }
       else if(ballBounds.intersect(paddleLeft) || ballBounds.intersect(paddleRight))
       {
           ballSpeedX *= -1;
           changeY = false;

           if (ballSpeedY > 0 && ballY < (paddleRight.top + (paddleLength / 4.5)))
           {
               changeY = true;
           }

           else if (ballSpeedY < 0 && ballY > (paddleRight.bottom - (paddleLength / 4.5)))
           {
               changeY = true;
           }

           if(changeY)
           {
               ballSpeedY *= -1;
           }

       }

       //detect collisions with walls
       else if(ballX + ballRadius > xMax) {
         

         //drawGame = false;
         //gameOver();
         ballX = xMax-ballRadius;
         ballSpeedX = -ballSpeedX;
      } else if (ballX - ballRadius < xMin) {
         //drawGame = false;
         //gameOver();
         ballSpeedX = -ballSpeedX;
         ballX = xMin+ballRadius;
      }
      if (ballY + ballRadius > yMax) {
         //drawGame = false;
         //gameOver();
         ballSpeedY = -ballSpeedY;
         ballY = yMax - ballRadius;
      } else if (ballY - ballRadius < yMin) {
         //drawGame = false;
         //gameOver();
         ballSpeedY = -ballSpeedY;
         ballY = yMin + ballRadius;
      }


       else{

           LinkedList<Block> blocksToRemove = new LinkedList<Block>();

           for (Block b : lstBlocks)
           {
               RectF blockBounds = b.getRect();
               if (ballBounds.intersect(blockBounds))
               {
                    float horizDiff = 0;
                    float vertDiff = 0;

                    float leftDiff = 0;
                    float rightDiff = 0;
                    float topDiff = 0;
                    float bottomDiff = 0;

                    //check which sides intersect
                    if (ballBounds.right > blockBounds.left)
                        leftDiff = Math.abs(blockBounds.left - ballBounds.right);

                    if (ballBounds.left < blockBounds.right)
                        rightDiff = Math.abs(blockBounds.right - ballBounds.left);

                    if (ballBounds.bottom > blockBounds.top)
                        topDiff = Math.abs(blockBounds.top - ballBounds.bottom);

                    if (ballBounds.top < blockBounds.bottom)
                        bottomDiff = Math.abs(blockBounds.bottom - ballBounds.top);

                    if (ballSpeedX > 0) //check ball direction for X-axis intersect
                        horizDiff = leftDiff;
                    else
                        horizDiff = rightDiff;

                    if (ballSpeedY > 0) //check ball direction for Y-axis intersect
                        vertDiff = topDiff;
                    else
                        vertDiff = bottomDiff;

                    //compare diffs
                    if(horizDiff < vertDiff)
                        ballSpeedX *= -1;
                    else if (horizDiff > vertDiff)
                        ballSpeedY *= -1;

                    else if (horizDiff == vertDiff)
                    {
                        ballSpeedX *= -1;
                        ballSpeedY *= -1;
                    }

                    score++;
                    blocksToRemove.add(b);
                    break;
               }
           }
           for (Block b : blocksToRemove)
           {
               lstBlocks.remove(b);
           }
       }
   }

   private void gameOver(Canvas c)
   {
       //drawGame = false;
       //this.setEnabled(false);
       //this.getParent().requestLayout();
       Intent i = new Intent(parentContext, QPad.class);
       ballX = 20;
       ballY = 20;

       ballSpeedX = 0;
       ballSpeedY = 0;
       //this.getParent().clearChildFocus(this.findFocus());
       invalidate();
       c.drawText("Game Over!", xMax / 2, yMax / 2, paint);
       try{
       Thread.sleep(500);
       }

       catch (Exception e) { }

       //try{this.set}
       //catch (Exception e){}

       

       parentContext.startActivity(i);
       //this.setFocusable(false);
       //this.getRootView().showContextMenu();
       //Thread.yield();
       //this.clearFocus();
   }

   // Called back to draw the view. Also called after invalidate().
   @Override
   protected void onDraw(Canvas canvas) {
      // Draw the ball
      if (drawGame == false)
      {
          gameOver(canvas);
      }
      else
      {
          ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
          paddleLeft.set(1, paddleY, paddleWidth + 1, paddleY + paddleLength);
          paddleRight.set(xMax - paddleWidth, paddleY, xMax, paddleY + paddleLength);
          paddleTop.set(paddleX, 1, paddleX + paddleLength, paddleWidth + 1);
          paddleBottom.set(paddleX, yMax - paddleWidth - paddleBottomMargin, paddleX + paddleLength, yMax - paddleBottomMargin);

          paint.setColor(Color.GREEN);
          canvas.drawText("Score: " + score, 5, yMax - 5, paint);


          paint.setColor(Color.GRAY);
          //paint.setTextSize(20);
          //int test = Float.floatToIntBits(paddleXFixed);
          canvas.drawRect(ballBounds, paint);

          for (Block b : lstBlocks)
          {
            b.draw(canvas);
          }

          //paint.setColor(Color.GRAY);
          canvas.drawRect(paddleTop, paint);
          canvas.drawRect(paddleBottom, paint);
          canvas.drawRect(paddleLeft, paint);
          canvas.drawRect(paddleRight, paint);
          update();


          // Delay
          try {
             Thread.sleep(10);
          } catch (InterruptedException e) { }

            invalidate();  // Force a re-draw
       }
   }

   // Detect collision and update the position of the ball.

   private void update() {
      // Get new (x,y) position
      detectBallCollisions();
      ballX += ballSpeedX;
      ballY += ballSpeedY;
      // Detect collision and react

      //paddleX = ballX - 35;
      //paddleY = ballY - 35;
   }

   // Key-up event handler
   @Override
   public boolean onKeyUp(int keyCode, KeyEvent event) {
      switch (keyCode) {
         case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase rightward speed
            ballSpeedX++;
            break;
         case KeyEvent.KEYCODE_DPAD_LEFT:  // Increase leftward speed
            ballSpeedX--;
            break;
         case KeyEvent.KEYCODE_DPAD_UP:    // Increase upward speed
            ballSpeedY--;
            break;
         case KeyEvent.KEYCODE_DPAD_DOWN:  // Increase downward speed
            ballSpeedY++;
            break;
         case KeyEvent.KEYCODE_DPAD_CENTER: // Stop
            ballSpeedX = 0;
            ballSpeedY = 0;
            break;
         case KeyEvent.KEYCODE_A:    // Zoom in
            // Max radius is about 90% of half of the smaller dimension
            float maxRadius = (xMax > yMax) ? yMax / 2 * 0.9f  : xMax / 2 * 0.9f;
            if (ballRadius < maxRadius) {
               ballRadius *= 1.05;   // Increase radius by 5%
            }
            break;
         case KeyEvent.KEYCODE_Z:    // Zoom out
            if (ballRadius > 20) {   // Minimum radius
               ballRadius *= 0.95;   // Decrease radius by 5%
            }
            break;
      }
      return true;  // Event handled
   }

   // Touch-input handler
   @Override
   public boolean onTouchEvent(MotionEvent event) {
      float currentX = event.getX();
      float currentY = event.getY();
      float deltaX, deltaY;
      float scalingFactor = 5.0f / ((xMax > yMax) ? yMax : xMax);
      switch (event.getAction()) {
         case MotionEvent.ACTION_MOVE:
            // Modify rotational angles according to movement
            deltaX = currentX - previousX;
            deltaY = currentY - previousY;
            //ballSpeedX += deltaX * scalingFactor;
            //ballSpeedY += deltaY * scalingFactor;
            
            


            paddleX = paddleX + (currentX - previousX);
            paddleY = paddleY + (currentY - previousY);


      }
      // Save current x, y
      previousX = currentX;
      previousY = currentY;
      return true;  // Event handled
   }


   // Called back when the view is first created or its size changes.
   @Override
   public void onSizeChanged(int w, int h, int oldW, int oldH) {
      // Set the movement bounds for the ball
      xMax = w-1;
      yMax = h-1;
   }
}

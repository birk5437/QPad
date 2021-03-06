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
import android.graphics.Typeface;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.os.Bundle;
import java.util.LinkedList;
import java.lang.Math;
import android.content.Intent;
import android.app.Activity;
import 	android.app.AlertDialog;
import 	android.view.LayoutInflater;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.app.Activity;


public class GameView extends View {

   private boolean burkeMode = false;
   private boolean resetHigh = false;
   private boolean debug = false;
   private boolean beenReset = false;
   public boolean gameOver = false;
   private boolean touchingScreen = false;
   private boolean isColliding = false;

   private int lives = 3;
   private int paddleBottomMargin = 20;
   private int score = 0;
   private int xMin = 0;          // This view's bounds
   private int xMax;
   private int yMin = 0;
   private int yMax;
   private float ballRadius = 4; // Ball's radius
   private float ballX = ballRadius + 50;  // Ball's center (x,y)
   private float ballY = ballRadius + 75;
   
   private float standardBallSpeedX = 2;
   private float standardBallSpeedY = 2;
   private float maxBallSpeed = 3.5f;
   private float ballSpeedChange = .5f;
   
   private float ballSpeedX;
   private float ballSpeedY;
   
   private float paddleX;// = 20;
   private float paddleY;// = 20;
   //private float paddleYFixed;
   //private float paddleXFixed;
   private float paddleBuffer = 35f;
   private float paddleWidth = 5f; //default 5
   private float paddleLength = 110f;
   private float paddleReboundBuffer = 2.0f;  //(paddleLength/pRB) == size of the area on each end of the paddle that will make the ball bounce back in the opposite direction.  Must be >= 2
   private float fasterBallBuffer = 4.0f;
   private RectF ballBounds;      // Needed for Canvas.drawOval
   private Paint paint;           //The paint (e.g. style, color) used for drawing
   //LocationManager locationManager;
   //LocationListener locationListener;
   private RectF paddleLeft, paddleRight, paddleBottom, paddleTop;

   private float previousX;
   private float previousY;
   private float paddleDeltaX = 0;
   private float paddleDeltaY = 0;

   private Double latitude = 0.0;
   private Double drawLat = 0.0;
   private Double longitude = 0.0;
   private Double drawLong = 0.0;
   private Double altitude = 0.0;
   private float speed = 0.0f;
   private String exStr = " ";
   Activity parentContext;
   Context myContext;

   float horizDiff = 0;
   float vertDiff = 0;
   float leftDiff = 0;
   float rightDiff = 0;
   float topDiff = 0;
   float bottomDiff = 0;

   int blocksMinX = 70;
   int blocksMinY = 100;
   int blocksMaxX = 100;
   int blocksMaxY = 220;
   int blockSetWidth = 0; //always leave these set to 0 - use the pixel values above
   int blockSetHeight = 0;//always leave these set to 0 - use the pixel values above

   private BlockSet set1;

   private RectF blocksRect;


   private LinkedList<Block> lstBlocks;

   LinkedList<Block> blocksToRemove = new LinkedList<Block>();

   // Constructor
   public GameView(Context context) {
      super(context);
      //parentContext = context;
      parentContext = (Activity)context;
      myContext = this.getContext();

      resetBallSpeed();
      ballBounds = new RectF();
      paddleLeft = new RectF();
      paddleRight = new RectF();
      paddleTop = new RectF();
      paddleBottom = new RectF();

      paint = new Paint();
      this.setFocusable(true);
      this.requestFocus();
      this.setFocusableInTouchMode(true);

      resetGame();
   }

   private void resetBall()
   {
     ballX = 40;
     ballY = 20;
     ballSpeedX = 0;
     ballSpeedY = 0;    
     resetBallSpeed();
     resetPaddles();
   }
   
   private void resetBallSpeed() { 
      int multiplierX, multiplierY;

      if (ballSpeedX != 0) {
        multiplierX = (int) ((0 + ballSpeedX) / Math.abs(ballSpeedX));
      } else { multiplierX = 1; }

      if (ballSpeedY != 0) {
        multiplierY = (int) ((0 + ballSpeedY) / Math.abs(ballSpeedY));
      } else { multiplierY = 1; }

      ballSpeedX = (standardBallSpeedX * multiplierX);
      ballSpeedY = (standardBallSpeedY * multiplierY);
   }

   private void detectBallCollisions() {
       boolean changeX = false;
       boolean fasterX, slowerX = false;
       boolean changeY = false;
       boolean fasterY, slowerY = false;

       if (Math.abs(ballSpeedX) > maxBallSpeed) {
         ballSpeedX = maxBallSpeed * (ballSpeedX / Math.abs(ballSpeedX));
       }

       if (Math.abs(ballSpeedY) > maxBallSpeed) {
         ballSpeedY = maxBallSpeed * (ballSpeedY / Math.abs(ballSpeedY));
       }
       
       if((ballBounds.intersect(paddleTop) || ballBounds.intersect(paddleBottom)) && !isColliding)
       {
           isColliding = true;
           changeX = false;
           ballSpeedY *= -1;
           float paddleHitMultiplier = (ballX - (paddleTop.left + (paddleLength / paddleReboundBuffer))) / (paddleLength / paddleReboundBuffer);
           float paddleMoveMultiplier = (paddleDeltaX / xMax) * 1.25f;

           if (Math.abs(ballSpeedX) >= 1.5) {
             paddleHitMultiplier *= Math.abs(ballSpeedX);
             paddleMoveMultiplier *= Math.abs(ballSpeedX);
           } else {
             paddleHitMultiplier *= 1.7;
             paddleMoveMultiplier *= 1.7;
           }

           if (ballSpeedX != 0)
           {
             ballSpeedX += (Math.abs(paddleMoveMultiplier) * (ballSpeedX / Math.abs(ballSpeedX)));
             ballSpeedX += paddleHitMultiplier;
           }
           else
           {
             ballSpeedX += paddleMoveMultiplier;
             ballSpeedX += paddleHitMultiplier;
           }
       }
       else if((ballBounds.intersect(paddleLeft) || ballBounds.intersect(paddleRight)) && !isColliding) {
           isColliding = true;
           ballSpeedX *= -1;
           changeY = false;

           float paddleHitMultiplier = (ballY - (paddleLeft.top + (paddleLength / paddleReboundBuffer))) / (paddleLength / paddleReboundBuffer);
           float paddleMoveMultiplier = (paddleDeltaY / yMax) * 1.25f;

           if (Math.abs(ballSpeedY) >= 1.5) {
             paddleHitMultiplier *= Math.abs(ballSpeedY);
             paddleMoveMultiplier *= Math.abs(ballSpeedY);
           } else {
             paddleHitMultiplier *= 1.7;
             paddleMoveMultiplier *= 1.7;
           }

           if (ballSpeedY != 0)
           {
             ballSpeedY += (Math.abs(paddleMoveMultiplier) * (ballSpeedY / Math.abs(ballSpeedY)));
             ballSpeedY += paddleHitMultiplier;
           }
           else
           {
             ballSpeedY += paddleMoveMultiplier;
             ballSpeedY += paddleHitMultiplier;
           }
       }
       else
           isColliding = false;

       detectWallCollisions();
       detectBlockCollisions();
   }

   private void detectWallCollisions() {
     //detect collisions with walls
     if(ballX + ballRadius > xMax) {
       if(lives == 0)
       {
          gameOver = true;
       }
       else
       {
           lives--;
       }
       resetBall();

    } else if (ballX - ballRadius < xMin) {
       if(lives == 0)
       {
          gameOver = true;
       }
       else
       {
           lives--;
       }
       resetBall();
    }
    if (ballY + ballRadius > yMax) {
       if(lives == 0)
       {
          gameOver = true;
       }
       else
       {
           lives--;
       }
       resetBall();
    } else if (ballY - ballRadius < yMin) {
       if(lives == 0)
       {
          gameOver = true;
       }
       else
       {
           lives--;
       }
       resetBall();
    }
   }

   private void detectBlockCollisions() {
     if(ballBounds.intersect(blocksRect)){

         for (Block b : lstBlocks)
         {
             RectF blockBounds = b.getRect();
             if (ballBounds.intersect(blockBounds))
             {
                  horizDiff = 0;
                  vertDiff = 0;

                  leftDiff = 0;
                  rightDiff = 0;
                  topDiff = 0;
                  bottomDiff = 0;

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
         if (blocksToRemove.size() > 0) {
             for (Block b : blocksToRemove) { lstBlocks.remove(b); }
             blocksToRemove.clear();
         }
     }
  }

   private void resetGame()
   {
      score = 0;
      lives = 3;
      gameOver = false;

      resetBall();
      resetBlocksAndPaddles();
      beenReset = true;
   }

   // Called back to draw the view. Also called after invalidate().
   @Override
   protected void onDraw(Canvas canvas) {
      // Draw the ball
      if (gameOver)
      {
          if(resetHigh) {
              QPadDataManager d = new QPadDataManager(myContext);
              d.storeInt("high_score", 0);
          }
	  boolean newHigh = false;
          SharedPreferences prefs = parentContext.getSharedPreferences("qpad_prefs", 0);

          int high = prefs.getInt("high_score", 0);
          if (high < score)
          {
            high = score;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("high_score", score);
	          editor.commit();
	          newHigh = true;
          }

	  if (newHigh) {
	      //parentContext.setContentView(R.layout.submit_high);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("new_high", 1);
            editor.commit();
	    parentContext.finish();
	  }
	  else {
	      //parentContext.finish();
	       AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
	       builder.setTitle(parentContext.getText(R.string.finished_title));
	       LayoutInflater inflater = parentContext.getLayoutInflater();
	       View view = inflater.inflate(R.layout.finish, null);
	       TextView t = (TextView)view.findViewById(R.id.txtGameOver);
	       t.setText((String)parentContext.getText(R.string.finished_text) + " " + score + " high: " + high);
	       builder.setView(view);
	       View submitButton =view.findViewById(R.id.closeGame);
	       submitButton.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View clicked) {
		       if(clicked.getId() == R.id.closeGame) {
			   parentContext.finish();
		       }
		   }
	       });
	       AlertDialog finishDialog = builder.create();
	       finishDialog.show();
	  }
      }
      
      else
      {
          ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);

          paddleLeft.set(1 - paddleBuffer, paddleY, paddleWidth + 1, paddleY + paddleLength);
          paddleRight.set(xMax - paddleWidth, paddleY, xMax + paddleBuffer, paddleY + paddleLength);

          paddleTop.set(paddleX, 1 - paddleBuffer, paddleX + paddleLength, paddleWidth + 1);
          paddleBottom.set(paddleX, yMax - paddleWidth - paddleBottomMargin, paddleX + paddleLength, yMax - paddleBottomMargin);

          paint.setColor(Color.GREEN);
          canvas.drawText("Score: " + score, 5, yMax - 5, paint);
          canvas.drawText("Lives: " + lives, xMax - 45, yMax - 5, paint);


          paint.setColor(Color.GRAY);
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
          //try {
          //   Thread.sleep(0);
          //} catch (InterruptedException e) { }

            //invalidate();  // Force a re-draw
          invalidate();
       }
      

   }

   // Detect collision and update the position of the ball.

   private void update() {
      // Get new (x,y) position
      detectBallCollisions();

      ballX += ballSpeedX;
      ballY += ballSpeedY;

      if (!touchingScreen) {
          if (paddleX < -(paddleLength / 5))
              paddleX += 5;
          else if (paddleX < 0)
              paddleX += 1;

          if (paddleY < -(paddleLength / 5))
              paddleY += 5;
          else if (paddleY < 0)
              paddleY += 1;

          if (paddleX > ((xMax - paddleLength) + paddleLength / 5))
              paddleX -= 5;
          else if (paddleX > (xMax - paddleLength))
              paddleX -= 1;

          if (paddleY > ((yMax - paddleLength)- paddleBottomMargin) + paddleLength / 5)
              paddleY -= 5;
          else if (paddleY > (yMax - paddleLength)- paddleBottomMargin)
              paddleY -= 1;
      }

      if (burkeMode) {
          paddleX = ballX - (paddleLength / 2);
          paddleY = ballY - (paddleLength / 2);
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      float currentX = event.getX();
      float currentY = event.getY();
      float deltaX, deltaY;

      switch (event.getAction()) {
         case MotionEvent.ACTION_MOVE:
            deltaX = currentX - previousX;
            deltaY = currentY - previousY;

            if (paddleX >= -(paddleLength -5) && paddleX <= (xMax - 5)) {
                paddleX = paddleX + (currentX - previousX);
                paddleDeltaX = (currentX - previousX);
            }

            if (paddleY >= -(paddleLength - 5) && paddleY <= (yMax - 5)) {
              paddleY = paddleY + (currentY - previousY);
              paddleDeltaY = (currentY - previousY);
            }
            break;
          case MotionEvent.ACTION_UP:
              touchingScreen = false;
              paddleDeltaX = 0;
              paddleDeltaY = 0;
              break;
          case MotionEvent.ACTION_DOWN:
              touchingScreen = true;


      }
      // Save current x, y
      previousX = currentX;
      previousY = currentY;
      return true;  // Event handled
   }

   public void resetPaddles() {
      paddleX = (xMax - paddleLength) / 2;
      paddleY = (yMax - paddleLength) / 2;
   }

   public void resetBlocksAndPaddles() {
      set1 = new BlockSet(xMax/4, (yMax / 6), ((xMax - (xMax/2)) / 12), ((yMax - (yMax / 2)) / 12), 1);
      lstBlocks = set1.getBlocks();
      blocksRect = set1.getBounds();
      paddleX = (xMax - paddleLength) / 2;
      paddleY = (yMax - paddleLength) / 2;
      beenReset = false;
   }

   // Called back when the view is first created or its size changes.
   @Override
   public void onSizeChanged(int w, int h, int oldW, int oldH) {
      // Set the movement bounds for the ball
      xMax = w-1;
      yMax = h-1;

      if (beenReset) {
        resetBlocksAndPaddles();
        beenReset = false;
      }
   }
}

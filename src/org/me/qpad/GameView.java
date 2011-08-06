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
import java.util.Collections;

public class GameView extends View {
  private boolean debug = false;

   private int xMin = 0;          // This view's bounds
   private int xMax;
   private int yMin = 0;
   private int yMax;
   private float ballRadius = 4; // Ball's radius
   private float ballX = ballRadius + 100;  // Ball's center (x,y)
   private float ballY = ballRadius + 100;
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



   // Constructor
   public GameView(Context context) {
      super(context);
      //locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    /*locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      latitude = location.getLatitude();
      longitude = location.getLongitude();

      altitude = location.getAltitude();
      speed = location.getSpeed();

      if (latitude < 0)
          latitude *= -1;
      if (longitude < 0)
          longitude *= -1;

      try{

      drawLong = (longitude - Math.round(Float.parseFloat(Double.toString(longitude)))) * 100;

      //drawLong = (longitude - Float.floatToIntBits(Float.parseFloat(Double.toString(longitude)))) * 100;
      drawLat = (latitude - Math.round(Float.parseFloat(Double.toString(latitude)))) * 100;
        }
      catch (Exception ex)
      {
          exStr = ex.toString();
      }
      // Called when a new location is found by the network location provider.
      //makeUseOfNewLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
  };*/

      ballBounds = new RectF();
      paddleLeft = new RectF();
      paddleRight = new RectF();
      paddleTop = new RectF();
      paddleBottom = new RectF();

      paint = new Paint();
      this.setFocusable(true);
      this.requestFocus();
      this.setFocusableInTouchMode(true);

      //updateGps();
   }

   private void detectBallCollisions()
   {
       if(ballBounds.intersect(paddleTop) || ballBounds.intersect(paddleBottom))
       {
           ballSpeedY *= -1;
       }
       else if(ballBounds.intersect(paddleLeft) || ballBounds.intersect(paddleRight))
       {
           boolean changeX = true;
           if (ballBounds.left > paddleRight.left && ballBounds.right < paddleRight.right)
           {
               ballSpeedY *= -1;
               changeX = false;
           }
           else if(ballBounds.left > paddleLeft.left && ballBounds.right < paddleLeft.right)
           {
               ballSpeedY *= -1;
               changeX = false;
           }
           if(changeX)
           {
               ballSpeedX *= -1;
           }
       }
   }

   // Called back to draw the view. Also called after invalidate().
   @Override
   protected void onDraw(Canvas canvas) {
      // Draw the ball
      ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
      paddleLeft.set(1, paddleY, paddleWidth + 1, paddleY + paddleLength);
      paddleRight.set(xMax - paddleWidth, paddleY, xMax, paddleY + paddleLength);
      paddleTop.set(paddleX, 1, paddleX + paddleLength, paddleWidth + 1);
      paddleBottom.set(paddleX, yMax - paddleWidth, paddleX + paddleLength, yMax);
      paint.setColor(Color.GRAY);
      //paint.setTextSize(20);
      //int test = Float.floatToIntBits(paddleXFixed);
      canvas.drawOval(ballBounds, paint);

      /*if(debug)
      {
        //canvas.drawText("BallX: " + Float.toString(ballX), 50, 30, paint);
        //canvas.drawText("BallY: " + Float.toString(ballY), 50, 40, paint);
        //canvas.drawText("PaddleXFixed: " + Float.toString(paddleXFixed), 50, 50, paint);
        //canvas.drawText("PaddleYFixed: " + Float.toString(paddleYFixed), 50, 60, paint);
        canvas.drawText(exStr, 40, 130, paint);
        //canvas.drawText("Lat: " + Float.parseFloat(Double.toHexString(latitude + (xMax / 2))), 40, 40, paint);
        canvas.drawText("Lat: " + Double.toString(latitude), 40, 40, paint);
        canvas.drawText("Long: " + Double.toString(longitude), 40, 55, paint);
        canvas.drawText("ALT: " + Double.toString(altitude), 40, 70, paint);
        canvas.drawText("Speed: " + Float.toString(speed), 40, 85, paint);
        canvas.drawText("DrawLat: " + Double.toString(drawLat), 40, 100, paint);
        canvas.drawText("DrawLong: " + Double.toString(drawLong), 40, 115, paint);
        //updateGps();

      }*/

      //paint.setColor(Color.GRAY);
      canvas.drawRect(paddleTop, paint);
      canvas.drawRect(paddleBottom, paint);
      canvas.drawRect(paddleLeft, paint);
      canvas.drawRect(paddleRight, paint);
      //canvas.drawPoint(Float.parseFloat(Double.toHexString(longitude + (xMax / 2))), Float.parseFloat(Double.toHexString(latitude + (yMax / 2))), paint);
      //canvas.drawText("HI BECKY!!", ballX, ballX, paint);
      //canvas.drawPoint(Float.parseFloat(Double.toHexString(drawLong + (xMax / 2))), Float.parseFloat(Double.toHexString(drawLat + (yMax / 2))), paint);
      //canvas.drawte
      //updateGps();
      // Update the position of the ball, including collision detection and reaction.
      update();


      // Delay
      try {
         Thread.sleep(20);
      } catch (InterruptedException e) { }

      invalidate();  // Force a re-draw
   }

   // Detect collision and update the position of the ball.
   /*private void updateGps()
    {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }*/

   private void update() {
      // Get new (x,y) position
      detectBallCollisions();
      ballX += ballSpeedX;
      ballY += ballSpeedY;
      // Detect collision and react
      if (ballX + ballRadius > xMax) {
         ballSpeedX = -ballSpeedX;
         ballX = xMax-ballRadius;
      } else if (ballX - ballRadius < xMin) {
         ballSpeedX = -ballSpeedX;
         ballX = xMin+ballRadius;
      }
      if (ballY + ballRadius > yMax) {
         ballSpeedY = -ballSpeedY;
         ballY = yMax - ballRadius;
      } else if (ballY - ballRadius < yMin) {
         ballSpeedY = -ballSpeedY;
         ballY = yMin + ballRadius;
      }
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

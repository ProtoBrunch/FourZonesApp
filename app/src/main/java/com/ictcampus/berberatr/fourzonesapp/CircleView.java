package com.ictcampus.berberatr.fourzonesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class CircleView extends View implements Runnable {
    private ShapeDrawable rectangle;
    private Drawable ob;
    private Paint paint;
    private Thread thread;
    private float currX, currY, touchX, touchY;
    private int width;
    private int height;
    private int counter;
    private int scale = 700;
    private Rect rectTopLeft;
    private Rect rectTopRight;
    private Rect rectBottomLeft;
    private Rect rectBottomRight;
    private Rect userRect;
    boolean touched, hit;
    private int[] colors = new int[]{Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW};
    private int rTLRandomColor, rTRRandomColor, rBLRandomColor, rBRRandomColor, userColor;
    private final int STROKESIZE = 10;

    public CircleView(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x / 2 - STROKESIZE;
        height = size.y / 2 - STROKESIZE;

        newRound(150, 150);
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        this.setBackground(ob);

        rectangle.getPaint().setColor(colors[rTLRandomColor]);
        rectangle.getPaint().setStyle(Paint.Style.STROKE);
        rectangle.getPaint().setStrokeWidth(STROKESIZE);
        rectTopLeft = new Rect((int) currX, (int) currY, width - STROKESIZE / 2, height - STROKESIZE / 2);
        rectangle.setBounds(rectTopLeft);
        rectTopLeft = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rTRRandomColor]);
        rectTopRight.set(width + STROKESIZE / 2, (int) currY, width * 2 - (int) currX, height - STROKESIZE / 2);
        rectangle.setBounds(rectTopRight);
        rectTopRight = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rBLRandomColor]);
        rectBottomLeft.set((int) currX, height + STROKESIZE / 2, width - STROKESIZE / 2, (int) currY);
        rectangle.setBounds(rectBottomLeft);
        rectBottomLeft = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rBRRandomColor]);
        rectBottomRight.set(width + STROKESIZE / 2, height + STROKESIZE / 2, width, (int) currY);
        rectangle.setBounds(rectBottomRight);
        rectBottomRight = rectangle.getBounds();
        rectangle.draw(canvas);

        if (touched) {
            rectangle.getPaint().setColor(colors[userColor]);
            userRect = new Rect((int) touchX - 25, (int) touchY - 25, (int) touchX + 25, (int) touchY + 25);
            rectangle.setBounds(userRect);
            userRect = rectangle.getBounds();
            rectangle.draw(canvas);
        }
    }

    @Override
    public void run() {
        int[] randomNumbers = new int[4];
        int speed = 12;
        int difficultySpeed = 50;
        while (hit) {
            if (currX < 10) {
                hit = false;

                //Check if userRect is inside other rectangles w/ correct color
                if (touchX > width) {
                    if (touchY > height && userColor == rBRRandomColor) {
                        hit = true;
                    } else if (touchY < height && userColor == rTRRandomColor) {
                        hit = true;
                    }
                } else {
                    if (touchY > height && userColor == rBLRandomColor) {
                        hit = true;
                    } else if (touchY < height && userColor == rTLRandomColor) {
                        hit = true;
                    }
                }

                if (hit) {
                    counter++;
                    Log.d("Counter", Integer.toString(counter));

                    if (counter < 44) {
                        difficultySpeed = (int) ((-1 / (50 / (Math.pow(counter, 2)))) + 50);
                    } else {
                        difficultySpeed = 10;
                    }

                    //Reset position of rectangles.
                    currX = width;
                    currY = height;

                    //Reset speed
                    speed = 12;

                    //random Colors
                    rTLRandomColor = (int) (Math.random() * colors.length);
                    randomNumbers[0] = rTLRandomColor;
                    rTRRandomColor = (int) (Math.random() * colors.length);
                    randomNumbers[1] = rTRRandomColor;
                    rBLRandomColor = (int) (Math.random() * colors.length);
                    randomNumbers[2] = rBLRandomColor;
                    rBRRandomColor = (int) (Math.random() * colors.length);
                    randomNumbers[3] = rBRRandomColor;
                    userColor = (int) (Math.random() * randomNumbers.length);
                    userColor = randomNumbers[userColor];
                } else {
                    scale = 200;
                }
                ob = new BitmapDrawable(getResources(), textAsBitmap(Integer.toString(counter), 300, Color.BLACK, scale));
            }

            //adapt speed
            if (currX < 350) {
                speed = 48;
            } else if (currX < 550) {
                speed = 36;
            } else if (currX < 700) {
                speed = 24;
            } else if (currX < 900) {
                speed = 18;
            }

            //set new values for rects
            currX = currX - speed;
            currY = currY - (speed - speed / 3);
            //TODO: calculate exact speed

            postInvalidate();

            try {
                Thread.sleep(difficultySpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap textAsBitmap(String text, float textSize, int textColor, int scale) {
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAlpha(60);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width + scale, height + scale, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void newRound(int userRectPosX, int userRectPosY) {
        touched = true;
        hit = true;

        currX = width;
        currY = height;
        touchX = userRectPosX;
        touchY = userRectPosY;
        counter = 0;
        scale = 700;

        rectTopLeft = new Rect((int) currX, (int) currY, width - 5, height - 5);
        rectTopRight = new Rect(width + STROKESIZE / 2, height * 2 - (int) currY, width * 2 - (int) currX, height - STROKESIZE / 2);
        rectBottomLeft = new Rect(width - STROKESIZE / 2, height * 2 - (int) currY, (int) currX, height + STROKESIZE / 2);
        rectBottomRight = new Rect(width + STROKESIZE / 2, height + STROKESIZE / 2, width, (int) currY);
        userRect = new Rect(50, 50, 50, 50);

        paint = new Paint(Color.BLACK);
        rectangle = new ShapeDrawable(new RectShape());
        thread = new Thread(this);
        ob = new BitmapDrawable(getResources(), textAsBitmap(Integer.toString(counter), 300, Color.BLACK, scale));
    }

    public Thread getThread() {
        return thread;
    }

    public void setTouchX(float position) {
        touchX = position;
    }

    public void setTouchY(float position) {
        touchY = position;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public int getCounter() {
        return counter;
    }
}

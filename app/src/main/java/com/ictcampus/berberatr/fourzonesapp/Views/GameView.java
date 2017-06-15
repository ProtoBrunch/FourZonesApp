package com.ictcampus.berberatr.fourzonesapp.Views;

import android.content.Context;
import android.content.SharedPreferences;
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

import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.ictcampus.berberatr.fourzonesapp.R;

import static android.content.Context.MODE_PRIVATE;

public class GameView extends View implements Runnable {
    private ShapeDrawable rectangle;
    private Drawable ob;
    private Bitmap returnBitmap;
    private Paint paint;
    private Thread thread;
    private float rectsXPos, rectsYPos, touchX, touchY;
    private int width;
    private int height;
    private int counter;
    private int scale;
    private Rect rectTopLeft;
    private Rect rectTopRight;
    private Rect rectBottomLeft;
    private Rect rectBottomRight;
    private Rect userRect;
    boolean touched, hit, start;
    private int[] colors;
    private int rTLRandomColor, rTRRandomColor, rBLRandomColor, rBRRandomColor, userColor;
    private final int STROKESIZE = 10;

    public GameView(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x / 2;
        height = size.y / 2;

        SharedPreferences prefs = context.getSharedPreferences("MyPref", 0);
        if (prefs.getBoolean("colorBlind", false)) {
            colors = context.getResources().getIntArray(R.array.colorBlind);
        }else if(prefs.getBoolean("darkTheme",false)){
            colors = context.getResources().getIntArray(R.array.darkTheme);
        }else{
            colors = context.getResources().getIntArray(R.array.normal);
        }

        newRound(150, 150);
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        this.setBackground(ob);

        rectangle.getPaint().setColor(colors[rTLRandomColor]);
        rectangle.getPaint().setStyle(Paint.Style.STROKE);
        rectangle.getPaint().setStrokeWidth(STROKESIZE);
        rectTopLeft = new Rect((int) rectsXPos, (int)rectsYPos, width - STROKESIZE / 2, height - STROKESIZE / 2);
        rectangle.setBounds(rectTopLeft);
        rectTopLeft = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rTRRandomColor]);
        rectTopRight.set(width + STROKESIZE / 2, (int) rectsYPos, width * 2 - (int) rectsXPos, height - STROKESIZE / 2);
        rectangle.setBounds(rectTopRight);
        rectTopRight = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rBLRandomColor]);
        rectBottomLeft.set((int) rectsXPos, height + STROKESIZE / 2, width - STROKESIZE / 2, (int) rectsYPos);
        rectangle.setBounds(rectBottomLeft);
        rectBottomLeft = rectangle.getBounds();
        rectangle.draw(canvas);

        rectangle.getPaint().setColor(colors[rBRRandomColor]);
        rectBottomRight.set(width + STROKESIZE / 2, height + STROKESIZE / 2, width, (int) rectsYPos);
        rectangle.setBounds(rectBottomRight);
        rectBottomRight = rectangle.getBounds();
        rectangle.draw(canvas);

        if (touched) {
            rectangle.getPaint().setColor(colors[userColor]);
            rectangle.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            int size = 100;
            userRect = new Rect((int) touchX - size, (int) touchY - size, (int) touchX + size, (int) touchY + size);
            rectangle.setBounds(userRect);
            userRect = rectangle.getBounds();
            rectangle.draw(canvas);
        }
    }

    @Override
    public void run() {
        int[] randomNumbers = new int[4];
        int speed = 9;
        int difficultySpeed = 50;
        while (hit) {
            if (rectsXPos < 10) {
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
                    if (counter < 40) {
                        difficultySpeed = (int) ((-1 / (50 / (Math.pow(counter, 2)))) + 50);
                    } else {
                        difficultySpeed = 15;
                    }
                    //Reset position of rectangles.
                    rectsXPos = width;
                    rectsYPos = height;

                    //Reset speed
                    speed = 9;

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

                    ob = new BitmapDrawable(getResources(), textAsBitmap(Integer.toString(counter), 300, Color.BLACK, scale));
                } else {
                    scale = 200;
                    counter--;
                }

            }

            //for starting countdown
            if(start){
                if (rectsXPos < (width/3+100)) {
                    ob = new BitmapDrawable(getResources(), textAsBitmap("0", 300, Color.BLACK, scale));
                    speed = 34;
                    start = false;
                } else if (rectsXPos < (width/3 *2)) {
                    ob = new BitmapDrawable(getResources(), textAsBitmap("1", 300, Color.BLACK, scale));
                    speed = 26;
                } else if (rectsXPos < (width- 100)) {
                    speed = 17;
                    ob = new BitmapDrawable(getResources(), textAsBitmap("2", 300, Color.BLACK, scale));
                }
            }
            //adapt speed
            //TODO: add percentages to if/else if
            if (rectsXPos < 350) {
                speed = 34;
            } else if (rectsXPos < (width/3+100)) {
                speed = 26;
            } else if (rectsXPos < (width/3 *2)) {
                speed = 17;
            } else if (rectsXPos < (width- 100)) {
                speed = 13;
            }

            //set new values for rects
            rectsXPos = rectsXPos - (int)(((double)width/(double)height)*speed);
            rectsYPos = rectsYPos - speed;

            postInvalidate();

            try {
                Thread.sleep(difficultySpeed);
            } catch (InterruptedException e) {
                Log.d("Thread", "Sleep Error: ", e);
            }
        }
    }

    private Bitmap textAsBitmap(String text, float textSize, int textColor, int scale) {
        final String fText = text;
        final float fTextSize = textSize;
        final int fTextColor = textColor;
        final int fScale = scale;

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                paint.setTextSize(fTextSize);
                paint.setColor(fTextColor);
                paint.setAlpha(60);
                paint.setTextAlign(Paint.Align.LEFT);
                float baseline = -paint.ascent(); // ascent() is negative
                int width = (int) (paint.measureText(fText) + 0.5f); // round
                int height = (int) (baseline + paint.descent() + 0.5f);
                Bitmap image = Bitmap.createBitmap(width + fScale, height + fScale, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(image);
                canvas.drawText(fText, 0, baseline, paint);
                return image;
            }
            protected void onPostExecute(Bitmap bitmap){
                returnBitmap = bitmap;
            }
        }.execute();
        return  returnBitmap;
    }

    public void newRound(int userRectPosX, int userRectPosY) {
        touched = true;
        hit = true;
        start = true;

        rectsXPos = width;
        rectsYPos = height;
        touchX = userRectPosX;
        touchY = userRectPosY;
        counter = 0;
        scale = width/2;

        rectTopLeft = new Rect((int) rectsXPos, (int) rectsYPos, width - STROKESIZE / 2, height - STROKESIZE / 2);
        rectTopRight = new Rect(width + STROKESIZE / 2, height * 2 - (int) rectsYPos, width * 2 - (int) rectsXPos, height - STROKESIZE / 2);
        rectBottomLeft = new Rect(width - STROKESIZE / 2, height * 2 - (int) rectsYPos, (int) rectsXPos, height + STROKESIZE / 2);
        rectBottomRight = new Rect(width + STROKESIZE / 2, height + STROKESIZE / 2, width, (int) rectsYPos);
        userRect = new Rect(250, 250, 250, 250);

        paint = new Paint(Color.BLACK);
        rectangle = new ShapeDrawable(new RectShape());
        thread = new Thread(this);

        ob = new BitmapDrawable(getResources(), textAsBitmap("3", 300, Color.BLACK, scale));
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

    public boolean getHit(){
        return hit;
    }
}
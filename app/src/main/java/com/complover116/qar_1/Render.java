

package com.complover116.qar_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class Render extends SurfaceView implements SurfaceHolder.Callback {

    /** Handle to the application context, used to e.g. fetch Drawables. */
    //private SurfaceV mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;
    public static int width = 0;
    public static int height = 0;
    public static int leftBound = 0;
    public static int rightBound = 0;
    public static float canvaScale = 0;
    public static Q1Button rightButton;
    public static Q1Button leftButton;
    public static Q1Button jumpButton;
    public static Q1Button fireButton;
    public Render(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        setFocusable(true); // make sure we get key events
    }


    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int newwidth,
                               int newheight) {
		System.out.println("Surface Changed "+newwidth+":"+newheight);
        width = newwidth;
        height = newheight;
        leftBound = (width - height)/2;
        rightBound = (width - height)/2+height;
        canvaScale = (float)height/(float)760;
        rightButton = new Q1Button("controls_right", new Rectangle(0,0,leftBound,height/2), CharData.getTransformedButton(CharData.Right));
        leftButton = new Q1Button("controls_left", new Rectangle(0,height/2,leftBound,height/2), CharData.getTransformedButton(CharData.Left));
        jumpButton = new Q1Button("controls_jump", new Rectangle(rightBound,0,width - rightBound,height/2), CharData.getTransformedButton(CharData.Up));
        fireButton = new Q1Button("controls_fire", new Rectangle(rightBound,height/2,width - rightBound,height/2), CharData.getTransformedButton(CharData.Down));
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("Surface Created");
		new Thread(new DrawThread(getHolder(), getResources())).start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
		System.exit(0);
    }
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();

		if(event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN||event.getActionMasked()==MotionEvent.ACTION_DOWN){
           // System.out.println(event.toString());
           // System.out.println(event.getActionIndex()+":"+event.getPointerId(event.getActionIndex()));
            rightButton.checkPress(event,false);
            leftButton.checkPress(event,false);
            jumpButton.checkPress(event,false);
            fireButton.checkPress(event,false);
        }
        if(event.getActionMasked()==MotionEvent.ACTION_POINTER_UP||event.getActionMasked()==MotionEvent.ACTION_UP){
           // System.out.println(event.toString());
           // System.out.println(event.getActionIndex()+":"+event.getPointerId(event.getActionIndex()));
            rightButton.checkRelease(event,false);
            leftButton.checkRelease(event,false);
            jumpButton.checkRelease(event,false);
            fireButton.checkRelease(event,false);
        }
		return true;
	}
	
}

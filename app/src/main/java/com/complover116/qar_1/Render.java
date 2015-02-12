

package com.complover116.qar_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;


/**
 * View that draws, takes keystrokes, etc. for a simple LunarLander game.
 *
 * Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
 * current ship physics. All x/y etc. are measured with (0,0) at the lower left.
 * updatePhysics() advances the physics based on realtime. draw() renders the
 * ship, and does an invalidate() to prompt another draw() as soon as possible
 * by the system.
 */
class Render extends SurfaceView implements SurfaceHolder.Callback {

    public static float x = 0;
	public static float y = 0;
    /** Handle to the application context, used to e.g. fetch Drawables. */
    //private SurfaceV mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;

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
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
		System.out.println("Surface Changed "+width+":"+height);	   
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
		x = event.getX();
		y = event.getY();
		System.out.println(event.toString());
		int key = 0;
		if(x>400&&x<800){
			key = CharData.D;
			if(y<400) key = CharData.W;
		}
		if(x<400) {key = CharData.A;
			if(y<400) key = CharData.S;
		}
		
		if(x>800&&x<1300){
			key = CharData.Left;
			if(y<400) key = CharData.Down;
		}
		if(x>1300) {key = CharData.Right;
			if(y<400) key = CharData.Up;
		}
			
			/*if(!Loader.isServer){
				ClientThread.sendKey(e.getKeyCode(), true);
			}*/
		if(event.getActionMasked()==event.ACTION_DOWN||event.getActionMasked()==event.ACTION_POINTER_DOWN){
			for(int i = 0; i < CurGame.lvl.players.size(); i ++)
				CurGame.lvl.players.get(i).keyPressed(key);
		}
		if(event.getAction()==event.ACTION_UP||event.getActionMasked()==event.ACTION_POINTER_UP){
			for(int i = 0; i < CurGame.lvl.players.size(); i ++)
				CurGame.lvl.players.get(i).keyReleased(key);
		}
		return true;
	}
	
}

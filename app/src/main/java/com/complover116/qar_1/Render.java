

package com.complover116.qar_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

class Render extends SurfaceView implements SurfaceHolder.Callback {

    /** Handle to the application context, used to e.g. fetch Drawables. */
    //private SurfaceV mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;
    public static int width = 0;
    public static int height = 0;
    public static int leftBound = 0;
    public static int rightBound = 0;
    public static float canvaScale = 0;
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
			//TODO: Key Sending
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

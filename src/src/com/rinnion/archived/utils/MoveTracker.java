package com.rinnion.archived.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Eloy on 12.01.2016.
 */
public class MoveTracker {

    public static final int MoveDown=1;
    public static final int MoveUp=2;

    private PointF pointStart;
    private PointF pointEnd;

    public void Down(MotionEvent event)
    {
        pointStart=new PointF(event.getRawX(),event.getRawY());
    }

    public int Up(MotionEvent event)
    {
        pointEnd=new PointF(event.getRawX(),event.getRawY());

        if(pointEnd.y-pointStart.y>0)
        {
            return MoveDown;
        }
        else
        {
            return MoveUp;
        }

    }



}

package com.gif.eting.act.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Snow extends View {
	private int size;
	private int speed;

	private int x;
	private int y;

	private Paint Pnt;

	public Snow(Context context) {
		super(context);
		this.Pnt = new Paint();
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public Snow(Context context, int startPosition, int size, int speed) {
		this(context);
		this.x = startPosition;
		this.y = 0;
		this.size = size;
		this.speed = speed;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Pnt.setColor(Color.WHITE);
		// canvas.drawColor(Color.BLACK);
		canvas.drawCircle(x, y, size, Pnt);

		int r = (int) (Math.random() * 9);
		int diff = (int) (Math.random() * 2);
		if (r % 3 == 0) {
			x += diff;
		} else if (r % 3 == 1) {
			x -= diff;
		} else {
		}
		
		y += speed;

	}
}

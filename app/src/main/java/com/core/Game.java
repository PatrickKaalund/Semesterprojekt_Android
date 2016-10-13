package com.core;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamelogic.Player;
import com.gamelogic.Map;

public class Game implements UpdateAndDrawInterface {

    private Map map;
    private Player player;

    public Game(){
        map = new Map();
        player = new Player();
    }

    @Override
    public void update() {
        map.update();
        player.update();
    }

    // Layered drawing!
    @Override
    public void draw(Canvas canvas, Paint paint, Context context) {
        map.draw(canvas, paint, context);
        player.draw(canvas, paint, context);
    }
}

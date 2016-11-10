package com.graphics;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.gamelogic.Enemy;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by PatrickKaalund on 10/11/2016.
 */

public class EnemySpawner {
    private SpriteEntityFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    private PointF sizeOfEnemy;
    private DisplayMetrics displayMetrics;

    public EnemySpawner(Context c){
        this.displayMetrics = c.getResources().getDisplayMetrics();
        sizeOfEnemy = new PointF(350, 350);
        enemyFactory = new SpriteEntityFactory(R.drawable.zombie_topdown, sizeOfEnemy.x, sizeOfEnemy.y, 8, 36, new PointF(0, 0));
        enemies = new ArrayList<>();
    }

    public void spawn(int health, int speed){
        float min = sizeOfEnemy.x/2;
        // Must load screen resolution
        float maxX = displayMetrics.widthPixels - sizeOfEnemy.x/2;
        float maxY = displayMetrics.heightPixels - sizeOfEnemy.y/2;
        Random rand = new Random();

        float randomX = rand.nextFloat() * (maxX - min) + min;
        float randomY = rand.nextFloat() * (maxY - min) + min;

        Enemy enemy = new Enemy(enemyFactory, health, speed, new PointF(randomX, randomY));
        enemies.add(enemy);
    }

    public void spawnEnemies(int health, int speed, int numberOfEnemies){
        for(int counter = 0; counter < numberOfEnemies; counter++){
            spawn(health, speed);
        }
    }

    public void update(Direction velMap){
        for(Enemy e: enemies){
            e.update(velMap);
        }
    }
}

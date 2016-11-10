package com.graphics;

import android.content.Context;
import android.graphics.PointF;

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

    public EnemySpawner(){
        enemyFactory = new SpriteEntityFactory(R.drawable.zombie_topdown, 350, 350, 8, 36, new PointF(0, 0));
        enemies = new ArrayList<>();
    }

    public void spawn(int health, int speed){
        float min = 100.0f;
        // Must load screen resolution
        float maxX = 950.0f;
        float maxY = 1550.0f;
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

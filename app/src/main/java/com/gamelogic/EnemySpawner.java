package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.gamelogic.Enemy;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by PatrickKaalund on 10/11/2016.
 */

public class EnemySpawner {
    private SpriteEntityFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    private PointF sizeOfEnemy;
    private DisplayMetrics displayMetrics;


    public EnemySpawner(Context c) {
        this.displayMetrics = c.getResources().getDisplayMetrics();
        sizeOfEnemy = new PointF(200, 200);
        enemyFactory = new SpriteEntityFactory(R.drawable.zombie_topdown_red_hitcol, sizeOfEnemy.x, sizeOfEnemy.y, 2, 36, new PointF(0, 0));
        enemies = new ArrayList<>();

    }

    public void spawn(int health, int speed) {
        Random rand = new Random();
        int spawnPlace = rand.nextInt(4); // generate random int value: 0-3
        Log.d("EnemySpawner", "Spawn place: " + spawnPlace);
        float minX = 0, minY = 0, maxX = 0, maxY = 0;

        switch (spawnPlace) {
            // left
            case 0:
                minX = 0;
                maxX = 0;
                minY = 0;
                maxY = DataContainer.mapGlobalSize.y;
                break;
            // top
            case 1:
                minX = 0;
                maxX = DataContainer.mapGlobalSize.x;
                minY = DataContainer.mapGlobalSize.y;
                maxY = DataContainer.mapGlobalSize.y;
                break;
            // right
            case 2:
                minX = DataContainer.mapGlobalSize.x;
                maxX = DataContainer.mapGlobalSize.x;
                minY = 0;
                maxY = DataContainer.mapGlobalSize.y;
                break;
            // bottom
            case 3:
                minX = 0;
                maxX = DataContainer.mapGlobalSize.x;
                minY = 0;
                maxY = 0;
                break;
            default:
                Log.d("EnemySpawner", "Unknown spawning place!");
                break;
        }

        float randomX = rand.nextFloat() * (maxX - minX) + minX;
        float randomY = rand.nextFloat() * (maxY - minY) + minY;

        Enemy enemy = new Enemy(enemyFactory.createEntity(), health, speed, new PointF(randomX,randomY));
        enemies.add(enemy);
    }

    public void spawnEnemies(int health, int speed, int numberOfEnemies) {
        for (int counter = 0; counter < numberOfEnemies; counter++) {
            spawn(health, speed);
        }
    }

    public void update(Player player) {
        for (Iterator<Enemy> enemyIterator = enemies.iterator(); enemyIterator.hasNext(); ){
            Enemy enemy = enemyIterator.next();
            if(!enemy.update(player)){
                enemyIterator.remove();
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void deliteEnemy(Enemy e){
        enemies.remove(e);

    }

}

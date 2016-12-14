package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.core.Game;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class EnemySpawner {
    private final Game game;
    private SpriteEntityFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    public int activeEnemys = 0;



    public EnemySpawner(Context c, Game game) {
        PointF sizeOfEnemy = new PointF(235, 235);
        enemyFactory = new SpriteEntityFactory(R.drawable.zombie, sizeOfEnemy.x, sizeOfEnemy.y, 4, 18, new PointF(0, 0));
        //enemyFactory = new SpriteEntityFactory(R.drawable.zombie_topdown_red_hitcol_small, sizeOfEnemy.x, sizeOfEnemy.y, 2, 36, new PointF(0, 0));
        enemies = new ArrayList<>();
        this.game = game;
    }

    public void spawn(BackgroundEntity map, int health, int speed) {
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
                maxY = map.mapGlobalSize.y;
                break;
            // top
            case 1:
                minX = 0;
                maxX =map.mapGlobalSize.x;
                minY =map.mapGlobalSize.y;
                maxY =map.mapGlobalSize.y;
                break;
            // right
            case 2:
                minX = map.mapGlobalSize.x;
                maxX =map.mapGlobalSize.x;
                minY = 0;
                maxY = map.mapGlobalSize.y;
                break;
            // bottom
            case 3:
                minX = 0;
                maxX = map.mapGlobalSize.x;
                minY = 0;
                maxY = 0;
                break;
            default:
                Log.d("EnemySpawner", "Unknown spawning place!");
                break;
        }

        float randomX = rand.nextFloat() * (maxX - minX) + minX;
        float randomY = rand.nextFloat() * (maxY - minY) + minY;

        Enemy enemy = new Enemy(this,map,enemyFactory.createEntity(), health, speed, new PointF(randomX,randomY), game);
        enemies.add(enemy);
    }

    public void spawnEnemies(BackgroundEntity map,int health, int speed, int numberOfEnemies) {
        for (int counter = 0; counter < numberOfEnemies; counter++) {
            spawn(map,health, speed);
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

    public void deleteEnemy(Enemy e){
        enemies.remove(e);
    }
}

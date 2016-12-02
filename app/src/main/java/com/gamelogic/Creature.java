package com.gamelogic;


abstract class Creature {
    int speed;
    int health;

    Creature() {

    }
    public abstract boolean doDamage(int damage);
}

package com.explosion_wands.sharedValues;

public class ExplosionEntities {
    private ExplosionEntities() {}

    //Failsafe in-case we spawn too many entities in one use (trust me, you want this)
    public static int maxEntities = 1028;
    public static int fuse = 0;
    //This makes the most difference to how far the entities fly
    public static float minExplosion = 2F;
    public static float maxExplosion = 4F;
    //Amount of primedTNTs that spawn in the center of the sphere
    public static int minIncrement = 2;
    public static int maxIncrement = 3;
    //Slight randomness to where the entities spawn when forming a sphere
    public static double minRandomPos = -0.1;
    public static double maxRandomPos = 0.1;
    //The core of how the sphere is made
    //First part of the for-loop
    public static double lessThanTheta = Math.PI;
    //Second part of the for-loop
    public static double lessThanPhi = 2 * Math.PI;

    //Shared variables:
    public static double incrementPhi;
    //Decrease: more entities
    //Increase: less entities
    public static double incrementTheta = incrementPhi = 0.4;
    public static int increment = 0;
    public static double theta;
    public static double phi;
    public static double x;
    public static double y;
    //Starts at 0, but increments every iteration
    public static double z = y = x = theta = phi = 0;

    //Radius of the sphere
    public static double r = 2;
    public static int spawnHeight = 20;
    //How far we can spawn entities from the player's current position
    public static int reachEntities = 128;
    public static int reachBlock = 512;
    public static int inflate = 100;

    //Calculates the amount of entities that are spawned before the for loops are set in motion (you want this along with maxEntities)
    public static int spawnedEntities = (int) ((Math.floor(lessThanTheta / incrementTheta) + 1) * (Math.floor(lessThanPhi / incrementPhi) + 1));

    public static int minRandomEntity = 1;
    public static int maxRandomEntity = spawnedEntities;
}

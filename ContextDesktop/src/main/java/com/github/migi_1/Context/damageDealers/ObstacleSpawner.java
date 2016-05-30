package com.github.migi_1.Context.damageDealers;

import java.util.ArrayList;

import com.github.migi_1.Context.model.entity.Commander;
import com.jme3.math.Vector3f;

public class ObstacleSpawner {    

    private static final int NUMBER_OBSTACLES = 10;

    /** Location of next obstacle spawn location. **/
    private Vector3f location;

    /** Abstract factory. **/
    private StaticObstacleFactory staticObstacleFactory;

    /** ArrayList of all geometry pieces. **/
    private ArrayList<StaticObstacle> staticObstacleList;

    private Commander commander;
    
    public ObstacleSpawner(Commander commander) {
        this.location = commander.getModel().getLocalTranslation();
        this.commander = commander;
        this.staticObstacleList = new ArrayList<StaticObstacle>();
        this.staticObstacleFactory = new StaticObstacleFactory();
    }
    /**
     * Create list of damage dealers that are to be spawned in the environment.
     * @return Map with all DamageDealer objects, with as key value their Geometry in the environment.
     */
    public ArrayList<StaticObstacle> getObstacles() {
        while (staticObstacleList.size() < NUMBER_OBSTACLES) {
            StaticObstacle obs = staticObstacleFactory.produce();
            obs.scale(0.3f);
            location = location.add(new Vector3f(-30.f, 0, 0.0f));


            obs.move(location);
            staticObstacleList.add(obs);
        }
        return staticObstacleList;

    }

    /**
     * Remove a damageDealer after collision.
     * @return DamageDealer that is deleted
     */
    public StaticObstacle removeDamageDealer() {
        float distance = Float.MAX_VALUE;
        StaticObstacle closest = staticObstacleList.get(0);
        for (StaticObstacle damageDealer : staticObstacleList) {
            Vector3f checkLocation = damageDealer.getModel().getLocalTranslation();
            float check = checkLocation.distance(commander.getModel().getLocalTranslation());
            if (check < distance) {
                distance = check;
                closest = damageDealer;
            }
        }
        staticObstacleList.remove(closest);
        return closest;
    }
}

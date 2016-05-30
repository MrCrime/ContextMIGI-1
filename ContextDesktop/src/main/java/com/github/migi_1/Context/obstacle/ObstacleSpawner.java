package com.github.migi_1.Context.obstacle;

import java.util.ArrayList;

import com.github.migi_1.Context.model.entity.Commander;
import com.jme3.math.Vector3f;

/**
 * Handles everything that has to do with the spawning of an obstacle.
 * An ObstacleSpawner has an obstaclefactory, which makes it able to switch
 * between dynamic obstacle spawning and static obstacle spawning when the difficulty increases.
 * @author Marcel/Damian
 *
 */
public class ObstacleSpawner {    

    private static final int NUMBER_OBSTACLES = 10;

    /** Location of next obstacle spawn location. **/
    private Vector3f location;

    /** Abstract factory. **/
    private AbstractObstacleFactory obstacleFactory;

    /** ArrayList of all geometry pieces. **/
    private ArrayList<Obstacle> obstacleList;

    private Commander commander;
    
    /**
     * Constructor for the obstacle spawner object.
     * @param commander needed for knowing where to spawn the obstacles.
     */
    public ObstacleSpawner(Commander commander) {
        this.location = commander.getModel().getLocalTranslation();
        this.commander = commander;
        this.obstacleList = new ArrayList<Obstacle>();
        this.obstacleFactory = new StaticObstacleFactory();
    }
    /**
     * Create list of obstacles that are to be spawned in the environment.
     * @return Map with all obstacles, with as key value their Geometry in the environment.
     */
    public ArrayList<Obstacle> getObstacles() {
        while (obstacleList.size() < NUMBER_OBSTACLES) {
            Obstacle obs = obstacleFactory.produce();
            obs.scale(0.3f);
            location = location.add(new Vector3f(-30.f, 0, 0.0f));

            obs.move(location);
            obstacleList.add(obs);
        }
        return obstacleList;

    }

    /**
     * Remove a obstacle after collision.
     * @return obstacles that is deleted
     */
    public Obstacle removeDamageDealer() {
        float distance = Float.MAX_VALUE;
        Obstacle closest = obstacleList.get(0);
        for (Obstacle obstacle : obstacleList) {
            Vector3f checkLocation = obstacle.getModel().getLocalTranslation();
            float check = checkLocation.distance(commander.getModel().getLocalTranslation());
            if (check < distance) {
                distance = check;
                closest = obstacle;
            }
        }
        obstacleList.remove(closest);
        return closest;
    }
    
    
}

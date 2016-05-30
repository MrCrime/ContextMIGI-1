package com.github.migi_1.Context.damageDealers;

import java.util.ArrayList;

import com.github.migi_1.Context.model.entity.Commander;
import com.jme3.math.Vector3f;

public class ObstacleSpawner {    

    private static final int NUMBER_OBSTACLES = 10;

    /** Location of next obstacle spawn location. **/
    private Vector3f location;

    /** Abstract factory. **/
    private DamageDealerFactory damageDealerFactory;

    /** ArrayList of all geometry pieces. **/
    private ArrayList<DamageDealer> damageDealerList;

    private Commander commander;
    
    public ObstacleSpawner(Commander commander) {
        this.location = commander.getModel().getLocalTranslation();
        this.commander = commander;
        this.damageDealerList = new ArrayList<DamageDealer>();
        this.damageDealerFactory = new StaticObstacleFactory();
    }
    /**
     * Create list of damage dealers that are to be spawned in the environment.
     * @return Map with all DamageDealer objects, with as key value their Geometry in the environment.
     */
    public ArrayList<DamageDealer> getObstacles() {
        while (damageDealerList.size() < NUMBER_OBSTACLES) {
            DamageDealer obs = damageDealerFactory.produce();
            obs.scale(0.3f);
            location = location.add(new Vector3f(-50.f, 0, 0.0f));


            obs.move(location);
            damageDealerList.add(obs);
        }
        return damageDealerList;

    }

    /**
     * Remove a damageDealer after collision.
     * @return DamageDealer that is deleted
     */
    public DamageDealer removeDamageDealer() {
        float distance = Float.MAX_VALUE;
        DamageDealer closest = damageDealerList.get(0);
        for (DamageDealer damageDealer : damageDealerList) {
            Vector3f checkLocation = damageDealer.getModel().getLocalTranslation();
            float check = checkLocation.distance(commander.getModel().getLocalTranslation());
            if (check < distance) {
                distance = check;
                closest = damageDealer;
            }
        }
        damageDealerList.remove(closest);
        return closest;
    }
}

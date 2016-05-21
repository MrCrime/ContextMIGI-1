package com.github.migi_1.Context;

import com.jme3.asset.AssetManager;

public class StaticObstacleFactory extends ObstacleFactory {


    @Override
    public StaticObstacle produce(AssetManager assetManager) {
        return new StaticObstacle(assetManager);
    }

}
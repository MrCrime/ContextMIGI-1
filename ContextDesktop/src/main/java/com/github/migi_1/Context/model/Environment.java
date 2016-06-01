package com.github.migi_1.Context.model;

import java.util.ArrayList;
import java.util.Collection;

import com.github.migi_1.Context.main.HUDController;
import com.github.migi_1.Context.main.Main;
import com.github.migi_1.Context.model.entity.Entity;
import com.github.migi_1.Context.model.entity.IDisplayable;
import com.github.migi_1.Context.model.entity.IMovable;
import com.github.migi_1.Context.utility.ProjectAssetManager;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * An Environment represents a world that can contain all kinds
 * of objects that can be added/removed.
 */
public class Environment extends AbstractAppState {

	private Node rootNode;
	private AssetManager assetManager;
	private Collection<IMovable> movables;
	private HUDController hudController;
	private boolean paused;
	private float difficultyFactor = 1.0f;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.paused = false;
		this.rootNode = ((Main) app).getRootNode();
		this.movables = new ArrayList<>();
		this.assetManager = ProjectAssetManager.getInstance().getAssetManager();

		this.assetManager.registerLocator("assets", FileLocator.class);
		hudController = new HUDController(app);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (!paused) {
		    hudController.updateHUD();
		    moveMovables();
		}
	}

	/**
	 * Adds a Displayable object to the world.
	 * Note: Do not add Entities using this method.
	 *
	 * @param displayable
	 * 		The displayable to add.
	 */
	public void addDisplayable(IDisplayable displayable) {
		rootNode.attachChild(displayable.getModel());
	}

	/**
	 * Removes a Displayable object from the world.
	 *
	 * @param displayable
	 * 		The displayable to remove.
	 */
	public void removeDisplayable(IDisplayable displayable) {
		rootNode.detachChild(displayable.getModel());
	}

	/**
	 * Gets the root node of the application.
	 *
	 * @return
	 * 		The root node of the application.
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * Gets the asset manager.
	 *
	 * @return
	 * 		The asset manager.
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Adds an Entity to the world.
	 *
	 * @param entity
	 * 		The entity to add.
	 */
	public void addEntity(Entity entity) {
		addDisplayable(entity);
		movables.add(entity);
	}

	/**
	 * Removes an Entity from the world.
	 *
	 * @param entity
	 * 		The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		removeDisplayable(entity);
		movables.remove(entity);
	}

	/**
	 * Moves all Movable objects in the world using the MoveBehaviours.
	 */
	private void moveMovables() {
		for (IMovable movable : movables) {
			movable.move(addDifficulty(movable.getMoveBehaviour().getMoveVector()));
		}
	}

	/**
	 * Add difficulty to the moveVector of the movables.
	 * @param movableVector
	 */
	private Vector3f addDifficulty(Vector3f movableVector) {
	    return new Vector3f(movableVector.x * difficultyFactor,
	                        movableVector.y * difficultyFactor,
	                        movableVector.z * difficultyFactor);
	}

	/**
	 * Check whether game is paused.
	 * @return paused or not paused
	 */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pause or unpause the game.
     * @param paused pause or unpause
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Set the difficulty of the game.
     * @param newDiff the new difficulty.
     */
    public void setDifficulty(float newDiff) {
        System.out.println("Old difficulty: " + difficultyFactor);
        difficultyFactor = newDiff;
        System.out.println("New difficulty: " + difficultyFactor);
    }

    /**
     * Returns the current difficulty.
     * @return the difficulty of the game.
     */
    public float getDifficulty() {
        return difficultyFactor;
    }
}

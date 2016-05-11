package com.github.migi_1.Context.model;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.FlyByCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * The Environement class handles all visual aspects of the world, excluding the characters and enemies etc.
 * @author Damian
 */
public class Environment {   
    private static final ColorRGBA BACKGROUNDCOLOR = ColorRGBA.Blue;
    private static final Vector3f SUNVECTOR = new Vector3f(-.5f,-.5f,-.5f);
    private static final Vector3f SUNVECTOR_2 = new Vector3f(0, -1f, -.2f);
    
    private static final int SHADOWMAP_SIZE = 1024;
    private static final int SHADOW_SPLITS = 3;
    
    private static final Vector3f WORLD_LOCATION = new Vector3f(0, -20, 0);
    private static final Vector3f PLATFORM_LOCATION = new Vector3f(20, -18, -3);
    private static final Vector3f COMMANDER_LOCATION = new Vector3f(23, -14, -3.5f);
    private static final float COMMANDER_ROTATION = -1.5f;
    
    private static final float PLATFORM_SPEED = 0.02f;    
    
    private ViewPort viewPort;
    private AssetManager assetManager;
    private Node rootNode;
    private FlyByCamera flyCam;
    
    private Spatial testPlatform;
    private Spatial testWorld;
    private Spatial testCommander;
    
    private DirectionalLight sun;
    private DirectionalLight sun2;
    /**
     * Constructor for the environment object
     * @param flyCam, The camera for flying around in the world (will be removed)
     * @param viewPort, Main screen of the game, this will be rendered
     * @param assetManager, loads and manages all assets of the world
     * @param rootNode, origin of the app
     */
    public Environment(FlyByCamera flyCam, ViewPort viewPort, AssetManager assetManager, Node rootNode) {
        this.flyCam = flyCam;
        this.viewPort = viewPort;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }
    
    /**
     * Initializes everything of the game world.
     */
    public void init() {
        //deprecated method, it does however make it possible to load assets from a non default location
        assetManager.registerLocator("assets", FileLocator.class);           
        
        flyCam.setMoveSpeed(50);      
        viewPort.setBackgroundColor(BACKGROUNDCOLOR);
        
        //creates the lights
        initLights();        
        
        //creates shadowmap and attach it to the sun
        initShadows();
        
        //initializes all spatials and set the positions
        initSpatials();

    }
    
    /**
     * Initializes all lights of the scene.
     */
    private void initLights() {
        sun = new DirectionalLight();
        sun2 = new DirectionalLight();

        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(SUNVECTOR).normalizeLocal());    

        sun2.setColor(ColorRGBA.White);
        sun2.setDirection(new Vector3f(SUNVECTOR_2).normalizeLocal());      
        
        //adds the lights to the root pane
        rootNode.addLight(sun);
        rootNode.addLight(sun2);        
    }
    
    /**
     * Initializes all shadows of the scene.
     */
    private void initShadows() {        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, SHADOW_SPLITS);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
        
        //adds shadow filter and attaches it to the viewport
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, SHADOW_SPLITS);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
    }
    
    /**
     * Initializes all objects and translations/rotations of the scene.
     */
    private void initSpatials() {
        testWorld = assetManager.loadModel("Models/testWorld.j3o");
        testWorld.move(WORLD_LOCATION);
        testPlatform = assetManager.loadModel("Models/testPlatform.j3o");
        testPlatform.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        testPlatform.move(PLATFORM_LOCATION);
        testCommander = assetManager.loadModel("Models/ninja.j3o");
        testCommander.rotate(0, COMMANDER_ROTATION, 0);
        testCommander.move(COMMANDER_LOCATION);
        
        //attach all objects to the root pane
        rootNode.attachChild(testWorld);
        rootNode.attachChild(testPlatform);
        rootNode.attachChild(testCommander);
    }
    
    /**
     * update the entities
     */
    public void update() {
        testPlatform.move(-PLATFORM_SPEED, 0, 0);
        testCommander.move(-PLATFORM_SPEED, 0, 0);
    }
    
    /**
     * render the entities
     * @param rm manager of the renderengine
     */
    public void render(RenderManager rm) {        
        
    }
}
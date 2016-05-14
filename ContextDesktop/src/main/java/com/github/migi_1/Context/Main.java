package com.github.migi_1.Context;

import com.github.migi_1.Context.model.Environment;
import com.github.migi_1.Context.screens.MainMenu;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

import jmevr.app.VRApplication;

/**
 * Creates the main desktop application, it initializes the main menu on startup,
 * all things that have to do with the application as a whole can be found in this class
 * @author Damian
 */
public class Main extends VRApplication {
    private MainMenu mainMenu;
    private Environment env;
    private static Main main;
    /**
     * main function of the appication, sets some meta-parameters of the application
     * and starts it.
     * @param args 
     */
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Carried Away");
        settings.setResolution(1280, 720);
        
        main = new Main();
        main.setSettings(settings);
        main.configureVR();
        main.start();
    }
    
    /**
     * initializes the main menu and starts it.
     */
    @Override
    public void simpleInitApp() {     
        initInputs();
        main.setPauseOnLostFocus(true);
        mainMenu = new MainMenu();
        env = new Environment(this.rootNode);
        
        this.getStateManager().attach(mainMenu);
        
        
    }
    
    /**
     * handles all updates.
     * @param tpf 
     */
    @Override
    public void simpleUpdate(float tpf) {
        if (getStateManager().hasState(env)){              
            getStateManager().getState(Environment.class).update(tpf);
        }        
    }
    
    /**
     * handles everything that needs rendering.
     * @param rm 
     */
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initInputs() {
        InputManager inputManager = getInputManager();
        inputManager.addMapping("exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if(name.equals("exit") && keyPressed){
                    System.exit(0);
                }
            }

        };
        inputManager.addListener(acl, "exit");
   }
    
    private void configureVR() {
        main.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
        main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW, true); // show gui even if it is behind things
        main.preconfigureVRApp(PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false); // faster VR rendering, requires some vertex shader changes (see jmevr/shaders/Unshaded.j3md)
        main.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
        main.preconfigureFrustrumNearFar(0.1f, 512f); // set frustum distances here before app starts
        main.preconfigureVRApp(PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false); // use full screen distortion, maximum FOV, possibly quicker (not compatible with instancing)
        main.preconfigureVRApp(PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, false); // runs faster when set to false, but will allow mirroring
        main.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE, false); // render two eyes, regardless of SteamVR
        main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);// you can downsample for performance reasons
    }

    /**
     * @return the mainMenu
     */
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    /**
     * @return the env
     */
    public Environment getEnv() {
        return env;
    }
    
}
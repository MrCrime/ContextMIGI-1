package com.github.migi_1.Context.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.migi_1.Context.model.MainEnvironment;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import jmevr.app.VRApplication;

/**
 * Test suite for the InputHandler.
 * @author Nils
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VRApplication.class, Quaternion.class, Vector3f.class})
public class TestInputHandler {

    private InputHandler inputHandler;
    private InputManager inputManager;
    private MainEnvironment envState;
    private Quaternion quat;
    private Vector3f vec;
    private Main main;

    /**
     * Setup for this test class.
     */
    @Before
    public void setUp() {
        main = Mockito.mock(Main.class);
        inputManager = Mockito.mock(InputManager.class);
        envState = Mockito.mock(MainEnvironment.class);
        quat = PowerMockito.mock(Quaternion.class);
        vec = Mockito.mock(Vector3f.class);
        PowerMockito.mockStatic(VRApplication.class);
        BDDMockito.given(VRApplication.getFinalObserverRotation()).willReturn(quat);
        BDDMockito.given(quat.getRotationColumn(Mockito.anyInt())).willReturn(vec);
        BDDMockito.given(vec.mult(Mockito.anyFloat())).willReturn(vec);

        Mockito.when(main.getInputManager()).thenReturn(inputManager);
        Mockito.when(main.getEnv()).thenReturn(envState);
        Mockito.when(envState.getFlyCamActive()).thenReturn(true);

        inputHandler = new InputHandler(main);
        inputHandler.initInputs(main);
    }

    /**
     * Tests if all keys are mapped and all listeners are bound.
     */
    @Test
    public void initInputTest() {
        //Verify all keys are mapped correctly.
        Mockito.verify(inputManager, Mockito.times(13)).addMapping(Mockito.anyString(), Mockito.any());
        //Verify all listeners are bound.
        Mockito.verify(inputManager, Mockito.times(13)).addListener(Mockito.any(), Mockito.anyString());
    }

    /**
     * Tests if the camera moves forwards correctly.
     */
    @Test
    public void moveCamForwardsTest() {
        inputHandler.getActionListener().onAction("forwards", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isForwards());
        Mockito.verify(envState).moveCam(Mockito.any());
    }

    /**
     * Tests if the camera moves backwards correctly.
     */
    @Test
    public void moveCamBackwardsTest() {
        inputHandler.getActionListener().onAction("backwards", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isBack());
        Mockito.verify(envState).moveCam(Mockito.any());
    }

    /**
     * Tests if the camera moves left correctly.
     */
    @Test
    public void moveCamLeftTest() {
        inputHandler.getActionListener().onAction("left", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isLeft());
        Mockito.verify(envState).rotateCam(Mockito.any());
    }

    /**
     * Tests if the camera moves right correctly.
     */
    @Test
    public void moveCamRightTest() {
        inputHandler.getActionListener().onAction("right", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isRight());
        Mockito.verify(envState).rotateCam(Mockito.any());
    }

    /**
     * Tests if the camera moves up correctly.
     */
    @Test
    public void moveCamUpTest() {
        inputHandler.getActionListener().onAction("up", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isUp());
        Mockito.verify(envState).moveCam(Mockito.any());
    }

    /**
     * Tests if the camera moves down correctly.
     */
    @Test
    public void moveCamDownTest() {
        inputHandler.getActionListener().onAction("down", true, 0f);
        inputHandler.moveCamera(0f);
        assertTrue(inputHandler.isDown());
        Mockito.verify(envState).moveCam(Mockito.any());
    }

    //The following tests test the action listener.

    /**
     * Test if the actionlistener correctly detects the forwards key press.
     */
    @Test
    public void testForwards() {
        assertFalse(inputHandler.isForwards());
        inputHandler.getActionListener().onAction("forwards", true, 0f);
        assertTrue(inputHandler.isForwards());
    }

    /**
     * Test if the actionlistener correctly detects the backwards key press.
     */
    @Test
    public void testBackwards() {
        assertFalse(inputHandler.isBack());
        inputHandler.getActionListener().onAction("backwards", true, 0f);
        assertTrue(inputHandler.isBack());
    }

    /**
     * Test if the actionlistener correctly detects the left key press.
     */
    @Test
    public void testLeft() {
        assertFalse(inputHandler.isLeft());
        inputHandler.getActionListener().onAction("left", true, 0f);
        assertTrue(inputHandler.isLeft());
    }

    /**
     * Test if the actionlistener correctly detects the right key press.
     */
    @Test
    public void testRight() {
        assertFalse(inputHandler.isRight());
        inputHandler.getActionListener().onAction("right", true, 0f);
        assertTrue(inputHandler.isRight());
    }

    /**
     * Test if the actionlistener correctly detects the up key press.
     */
    @Test
    public void testUp() {
        assertFalse(inputHandler.isUp());
        inputHandler.getActionListener().onAction("up", true, 0f);
        assertTrue(inputHandler.isUp());
    }

    /**
     * Test if the actionlistener correctly detects the down key press.
     */
    @Test
    public void testDown() {
        assertFalse(inputHandler.isDown());
        inputHandler.getActionListener().onAction("down", true, 0f);
        assertTrue(inputHandler.isDown());
    }

    /**
     * Test if pausing works correctly.
     */
    @Test
    public void testPauseNotPaused() {
        //Verify the game is not paused.
        assertFalse(envState.isPaused());
        inputHandler.getActionListener().onAction("pause", true, 0f);
        //Only verifying that in a real scenario, the game would be paused now.
        Mockito.verify(envState).setPaused(true);
    }

    /**
     * Test if unpausing works correctly.
     */
    @Test
    public void testPausePaused() {
        //To simulate the game is paused.
        Mockito.when(envState.isPaused()).thenReturn(true);
        //Trivial assert that the game is actually paused.
        assertTrue(envState.isPaused());
        inputHandler.getActionListener().onAction("pause", true, 0f);
        //Only verifying that in a real scenario, the game would unpause now.
        Mockito.verify(envState).setPaused(false);
    }

    /**
     *
     */
    @Test
    public void testRestart() {
        AppStateManager stateManager = Mockito.mock(AppStateManager.class);
        Mockito.when(main.getStateManager()).thenReturn(stateManager);
        Mockito.when(stateManager.hasState(envState)).thenReturn(true);
        inputHandler.getActionListener().onAction("restart", true, 0f);
        Mockito.verify(envState).cleanup();
        Mockito.verify(envState).initialize(Mockito.any(), Mockito.any());
    }

    /**
     * Test if the actionlistener correctly does nothing
     *  when an unused key is pressed.
     */
    @Test
    public void testUnknownButton() {
        assertFalse(inputHandler.isDown());
        inputHandler.getActionListener().onAction("unusedName", true, 0f);
        assertFalse(inputHandler.isDown());
    }

    /**
     * Tests if the actionListener ignores movement
     * for the flycam when it is not active.
     */
    @Test
    public void testNoFlyCamButtonPress() {
        //Overwrite method stub for this method only.
        Mockito.when(envState.getFlyCamActive()).thenReturn(false);
        assertFalse(inputHandler.isDown());
        inputHandler.getActionListener().onAction("down", true, 0f);
        assertFalse(inputHandler.isDown());
    }

    /**
     * Test if the actionlistener correctly detects the exit key press.
     */
    @Test
    public void testExit() {
        Mockito.verify(main, Mockito.never()).destroy();
        inputHandler.getActionListener().onAction("exit", false, 0f);
        Mockito.verify(main, Mockito.never()).destroy();
        inputHandler.getActionListener().onAction("exit", true, 0f);
        Mockito.verify(main).destroy();
    }

    /**
     * Test if the actionlistener correctly detects the cam_switch key press.
     */
    @Test
    public void testCamSwitch() {
        Mockito.verify(envState, Mockito.never()).swapCamera();
        inputHandler.getActionListener().onAction("cam_switch", false, 0f);
        Mockito.verify(envState, Mockito.never()).swapCamera();
        inputHandler.getActionListener().onAction("cam_switch", true, 0f);
        Mockito.verify(envState).swapCamera();
    }

    /**
     * Verifies steering left.
     */
    @Test
    public void testCheckSteerLeft() {
        Mockito.verify(envState, Mockito.never()).steer(Mockito.anyFloat());
        inputHandler.getActionListener().onAction("steer_left", false, 0f);
        Mockito.verify(envState, Mockito.never()).steer(-1.0f);
        inputHandler.getActionListener().onAction("steer_left", true, 0f);
        Mockito.verify(envState).steer(-1.0f);
    }

    /**
     * Verifies steering right.
     */
    @Test
    public void testCheckSteerRight() {
        Mockito.verify(envState, Mockito.never()).steer(Mockito.anyFloat());
        inputHandler.getActionListener().onAction("steer_right", false, 0f);
        Mockito.verify(envState, Mockito.never()).steer(1.0f);
        inputHandler.getActionListener().onAction("steer_right", true, 0f);
        Mockito.verify(envState).steer(1.0f);
    }

    /**
     * Verifies resetting the steering angle when not steering.
     */
    @Test
    public void testCheckSteerReset() {
        Mockito.verify(envState, Mockito.never()).steer(Mockito.anyFloat());
        inputHandler.getActionListener().onAction("steer_left", true, 0f);
        Mockito.verify(envState, Mockito.never()).steer(0.0f);
        inputHandler.getActionListener().onAction("steer_right", true, 0f);
        Mockito.verify(envState, Mockito.never()).steer(0.0f);
        inputHandler.getActionListener().onAction("steer_right", false, 0f);
        Mockito.verify(envState).steer(1.0f);
    }
}
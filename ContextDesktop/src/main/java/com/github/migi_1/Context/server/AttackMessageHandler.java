package com.github.migi_1.Context.server;

import com.github.migi_1.Context.main.Main;
import com.github.migi_1.Context.model.entity.Carrier;
import com.github.migi_1.ContextMessages.AttackMessage;
import com.github.migi_1.ContextMessages.Direction;
import com.github.migi_1.ContextMessages.MessageListener;
import com.github.migi_1.ContextMessages.PlatformPosition;

/**
 * Handles the AttackMessages when they are received.
 */
public class AttackMessageHandler extends MessageListener<AttackMessage> {
    
    private Carrier carrier;
    private PlatformPosition position;
    
    /**
     * Creates an AttackMessageHandler.
     * @param main
     * 			the main application in which this function is called
     * @param carrier
     *                  the carrier by which this function is called
     * @param position
     *                  the position of the carrier
     */
    @SuppressWarnings("unchecked")
    public AttackMessageHandler(Main main, Carrier carrier, PlatformPosition position) {
        this.carrier = carrier;
        this.position = position;
        main.getServer().getServer().addMessageListener(this);
    }
    
    /**
     * Processes AttackMessages when they are received.
     * @param source
     *              the source of the message
     * @param message
     *              the message itself
     */
    @Override
    public void messageReceived(Object source, AttackMessage message) {
        PlatformPosition pos = message.getPosition();
        if (position.equals(pos)) {
            Direction dir = message.getDirection();
            carrier.handleAttack(dir);
        }
    }
    
    @Override
    public Class<AttackMessage> getMessageClass() {
        return AttackMessage.class;
    }
}

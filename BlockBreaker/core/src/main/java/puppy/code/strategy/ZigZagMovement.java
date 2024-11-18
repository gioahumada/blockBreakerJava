package puppy.code.strategy;

import puppy.code.entities.PingBall;

public class ZigZagMovement implements MovementStrategy {
    private int zigzagCounter = 0;
    private static final int ZIGZAG_INTERVAL = 20;
    
    @Override
    public void move(PingBall ball) {
        if (ball.estaQuieto()) return;
        
        zigzagCounter++;
        int extraX = (zigzagCounter % ZIGZAG_INTERVAL < ZIGZAG_INTERVAL/2) ? 2 : -2;
        
        ball.setXY(
            ball.getX() + ball.getXSpeed() + extraX,
            ball.getY() + ball.getYSpeed()
        );
    }
} 
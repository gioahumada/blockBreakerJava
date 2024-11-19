package puppy.code.strategy;

import puppy.code.entities.PingBall;

public class WaveMovement implements MovementStrategy {
    private int waveCounter = 0;
    
    @Override
    public void move(PingBall ball) {
        if (ball.estaQuieto()) return;
        
        waveCounter++;
        double extraX = Math.sin(waveCounter * 0.1) * 3;
        
        ball.setXY(
            ball.getX() + ball.getXSpeed() + (int)extraX,
            ball.getY() + ball.getYSpeed()
        );
    }
} 
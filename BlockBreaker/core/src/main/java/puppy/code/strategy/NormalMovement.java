package puppy.code.strategy;

import puppy.code.entities.PingBall;

public class NormalMovement implements MovementStrategy {
    @Override
    public void move(PingBall ball) {
        if (ball.estaQuieto()) return;
        
        ball.setXY(
            ball.getX() + ball.getXSpeed(),
            ball.getY() + ball.getYSpeed()
        );
    }
} 
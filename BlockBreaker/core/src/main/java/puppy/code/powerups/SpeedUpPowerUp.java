package puppy.code.powerups;

import puppy.code.entities.PingBall;
import puppy.code.game.BlockBreakerGame;
import com.badlogic.gdx.utils.Timer;

public class SpeedUpPowerUp extends FallingPowerUp {

    public SpeedUpPowerUp(int x, int y) {
        super(x, y, 20, 1, "speed.png");  // Usar la textura "speed.png"
    }

    @Override
    public void activate(final BlockBreakerGame game) {
        for (PingBall bola : game.getBolasActivas()) {
            bola.aumentarVelocidad();
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for (PingBall bola : game.getBolasActivas()) {
                    bola.restaurarVelocidad();
                }
            }
        }, 5);
        game.mostrarNotificacionPowerUp(this.getClass().getSimpleName());
    }
}

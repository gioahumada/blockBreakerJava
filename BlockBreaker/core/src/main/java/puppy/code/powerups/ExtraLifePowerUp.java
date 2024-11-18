// ExtraLifePowerUp.java
package puppy.code.powerups;

import puppy.code.game.BlockBreakerGame;

public class ExtraLifePowerUp extends FallingPowerUp {
    public ExtraLifePowerUp(int x, int y) {
        super(x, y, 20, 1, "extralife.png");  // Usar la textura "extralife.png"
    }

    @Override
    public void activate(BlockBreakerGame game) {
        game.incrementarVidas();
        System.out.println("PowerUp activado: ¡Vida extra!");
    }
}
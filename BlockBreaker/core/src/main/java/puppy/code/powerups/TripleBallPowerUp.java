package puppy.code.powerups;

import puppy.code.game.BlockBreakerGame;

public class TripleBallPowerUp extends FallingPowerUp {

    public TripleBallPowerUp(int x, int y) {
        super(x, y, 20, 1, "multiplicator.png");  // Usar la textura "multiplicator.png"
    }

    @Override
    public void activate(BlockBreakerGame game) {
        // Crear dos bolas nuevas adicionales con velocidades ligeramente diferentes
        game.agregarNuevaBola(2);  // Bola con velocidad X aumentada en 2
        game.agregarNuevaBola(-2); // Bola con velocidad X disminuida en 2
    }
}

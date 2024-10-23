package puppy.code.powerups;

import com.badlogic.gdx.graphics.Color;
import puppy.code.game.BlockBreakerGame;

public class TripleBallPowerUp extends FallingPowerUp {

    public TripleBallPowerUp(int x, int y) {
        super(x, y, 20, 1, Color.BLUE);  // Tamaño 20, velocidad de caída más lenta (1)
    }

    @Override
    public void activate(BlockBreakerGame game) {
        // Crear 3 bolas nuevas adicionales
        for (int i = 0; i < 3; i++) {
            game.agregarNuevaBola();  // Método que añade una nueva bola al juego
        }
    }
}

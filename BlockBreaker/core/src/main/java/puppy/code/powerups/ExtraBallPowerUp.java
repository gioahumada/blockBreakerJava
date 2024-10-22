package puppy.code.powerups;

import puppy.code.game.BlockBreakerGame;

public class ExtraBallPowerUp implements PowerUp {

    @Override
    public void activate(BlockBreakerGame game) {
        game.agregarNuevaBola();  // Método que deberías implementar para agregar una bola al juego
        System.out.println("PowerUp activado: ¡Bola extra agregada!");
    }
}

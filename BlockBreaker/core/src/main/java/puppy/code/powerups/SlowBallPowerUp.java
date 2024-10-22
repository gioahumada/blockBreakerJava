package puppy.code.powerups;

import puppy.code.game.BlockBreakerGame;

public class SlowBallPowerUp implements PowerUp {

    @Override
    public void activate(BlockBreakerGame game) {
        game.reducirVelocidadBolas();  // Método que deberías implementar para reducir la velocidad de las bolas
        System.out.println("PowerUp activado: ¡Las bolas se han ralentizado!");
    }
}

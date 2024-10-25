package puppy.code.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.entities.Paddle;
import puppy.code.game.BlockBreakerGame;

public abstract class FallingPowerUp {
    protected int x, y, size;
    protected int ySpeed;
    protected Texture texture;

    public FallingPowerUp(int x, int y, int size, int ySpeed, String texturePath) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.ySpeed = ySpeed;
        this.texture = new Texture(Gdx.files.internal(texturePath));
    }

    // Método para dibujar el power-up
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, size, size);
    }

    // Actualizar la posición del power-up
    public void update() {
        y -= ySpeed;
    }

    // Verificar si colisiona con la paleta
    public boolean collidesWith(Paddle paddle) {
        return (paddle.getX() < x + size && paddle.getX() + paddle.getWidth() > x &&
            paddle.getY() < y + size && paddle.getY() + paddle.getHeight() > y);
    }

    // Activar el efecto del power-up
    public abstract void activate(BlockBreakerGame game);

    // Liberar recursos
    public void dispose() {
        texture.dispose();
    }
}

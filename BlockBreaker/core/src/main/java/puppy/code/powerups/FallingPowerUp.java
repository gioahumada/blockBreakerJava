package puppy.code.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.entities.Paddle;
import puppy.code.game.BlockBreakerGame;

public abstract class FallingPowerUp {
    protected int x, y, size;
    protected int ySpeed;
    protected Color color;

    public FallingPowerUp(int x, int y, int size, int ySpeed, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.ySpeed = ySpeed;
        this.color = color;
    }

    // Método para dibujar el power-up
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, size, size);
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
}

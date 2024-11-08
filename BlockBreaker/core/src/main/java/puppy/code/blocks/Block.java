package puppy.code.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.game.BlockBreakerGame;

public abstract class Block {
    protected int x, y;
    protected int width, height;
    protected Color color;
    protected boolean destroyed;

    public Block(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.destroyed = false;
    }

    public abstract void draw(ShapeRenderer shapeRenderer);

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            BlockBreakerGame.breakSound.play();
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

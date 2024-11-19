package puppy.code.blocks;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Block {
    void draw(ShapeRenderer shape);
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    boolean isDestroyed();
}

package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NormalBlock extends Block {
    public NormalBlock(int x, int y, int width, int height) {
        super(x, y, width, height, Color.BLUE); // Color azul para bloques normales
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
    }
}

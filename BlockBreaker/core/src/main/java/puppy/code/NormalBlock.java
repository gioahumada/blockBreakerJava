
package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class NormalBlock extends Block {

    public NormalBlock(int x, int y, int width, int height) {
        super(x, y, width, height, getRandomColor()); // Color verde para bloques normales
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
    }
    // Método estático para generar un color aleatorio
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1); // Color aleatorio
    }
}

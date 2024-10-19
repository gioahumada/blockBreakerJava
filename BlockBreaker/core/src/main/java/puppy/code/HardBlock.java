package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HardBlock extends Block {
    private int hitsRemaining;

    public HardBlock(int x, int y, int width, int height) {
        super(x, y, width, height, Color.RED); // Color rojo para HardBlock
        this.hitsRemaining = 3; // El bloque puede recibir 3 golpes antes de destruirse
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
    }

    // Método para reducir la cantidad de golpes restantes
    public void hit() {
        hitsRemaining--;
        if (hitsRemaining <= 0) {
            destroyed = true;
        }
    }

    // Método para verificar cuántos golpes quedan
    public int getHitsRemaining() {
        return hitsRemaining;
    }
}
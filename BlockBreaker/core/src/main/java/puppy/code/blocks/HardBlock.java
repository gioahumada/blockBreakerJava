package puppy.code.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.interfaces.Damageable;

public class HardBlock extends Block implements Damageable {
    private int hitsRemaining;
    private Color borderColor;  // Color para el borde
    private float borderWidth;  // Ancho del borde

    public HardBlock(int x, int y, int width, int height) {
        super(x, y, width, height, Color.valueOf("306230")); // Color verde oscuro
        this.hitsRemaining = 3;  // El bloque puede recibir 3 golpes antes de destruirse
        this.borderColor = Color.valueOf("0f380f");  // Asignar color del borde
        this.borderWidth = 3f;  // Ancho del borde
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        // Dibujar el borde
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(x, y, width, height);  // Dibujar el rectángulo completo

        // Dibujar el bloque interno (con un tamaño reducido por el ancho del borde)
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x + borderWidth, y + borderWidth,
            width - 2 * borderWidth, height - 2 * borderWidth);
    }

    @Override
    public void takeDamage(int amount) {
        hitsRemaining -= amount;
        if (hitsRemaining <= 0) {
            destroy();  // Método de la clase Block para marcar el bloque como destruido
        }
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;  // Utiliza el campo 'destroyed' de la clase Block
    }

    public int getHitsRemaining() {
        return hitsRemaining;
    }
}


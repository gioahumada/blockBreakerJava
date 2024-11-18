package puppy.code.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.interfaces.Damageable;

public class HardBlock extends BlockTemplate implements Damageable {
    private int hitsRemaining;

    public HardBlock(int x, int y, int width, int height) {
        super(x, y, width, height, Color.valueOf("306230"));
    }

    @Override
    protected void inicializarPropiedades() {
        this.borderColor = Color.valueOf("0f380f");
        this.borderWidth = 3f;
        this.hitsRemaining = 3;
    }

    @Override
    protected void aplicarEfectosEspeciales(ShapeRenderer shapeRenderer) {
        // Aquí podrías agregar efectos visuales basados en hitsRemaining
    }

    @Override
    public void takeDamage(int amount) {
        hitsRemaining -= amount;
        if (hitsRemaining <= 0) {
            destroy();
        }
    }

    public int getHitsRemaining() {
        return hitsRemaining;
    }
}


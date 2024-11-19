package puppy.code.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.interfaces.Damageable;

public class NormalBlock extends BlockTemplate implements Damageable {

    public NormalBlock(int x, int y, int width, int height) {
        super(x, y, width, height, Color.valueOf("8bac0f"));
    }

    @Override
    protected void inicializarPropiedades() {
        this.borderColor = Color.valueOf("306230");
        this.borderWidth = 3f;
    }

    @Override
    protected void aplicarEfectosEspeciales(ShapeRenderer shapeRenderer) {
        // Los bloques normales no tienen efectos especiales
    }

    @Override
    public void takeDamage(int amount) {
        destroy();
    }
}

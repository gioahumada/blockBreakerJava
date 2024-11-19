package puppy.code.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.game.BlockBreakerGame;

public abstract class BlockTemplate implements Block {
    protected int x, y;
    protected int width, height;
    protected Color color;
    protected Color borderColor;
    protected float borderWidth;
    protected boolean destroyed;

    public BlockTemplate(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.destroyed = false;
        inicializarPropiedades();
    }

    // Template Method que define el algoritmo general para dibujar un bloque
    public final void renderBlock(ShapeRenderer shapeRenderer) {
        dibujarBorde(shapeRenderer);
        dibujarInterior(shapeRenderer);
        aplicarEfectosEspeciales(shapeRenderer);
    }

    // Métodos abstractos que deben implementar las subclases
    protected abstract void inicializarPropiedades();
    protected abstract void aplicarEfectosEspeciales(ShapeRenderer shapeRenderer);

    // Métodos concretos que son comunes para todos los bloques
    protected void dibujarBorde(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(x, y, width, height);
    }

    protected void dibujarInterior(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x + borderWidth, y + borderWidth,
            width - 2 * borderWidth, height - 2 * borderWidth);
    }

    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            BlockBreakerGame.breakSound.play();
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        renderBlock(shape);
    }

    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }
    @Override
    public boolean isDestroyed() { return destroyed; }
} 
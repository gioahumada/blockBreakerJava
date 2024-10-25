package puppy.code.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {
    private int x = 20;
    private int y = 20;
    private int width = 100;
    private int height = 10;

    public Paddle(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        width = ancho;
        height = alto;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void draw(ShapeRenderer shape) {
        int x2 = x;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x2 = x - 15;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x2 = x + 15;
        if (x2 > 0 && x2 + width < Gdx.graphics.getWidth()) {
            x = x2;
        }
    
        float borderWidth = 2f;  // Ancho del borde
    
        // Dibujar el borde
        shape.setColor(Color.valueOf("306230"));  // Color del borde verde
        shape.rect(x - borderWidth, y - borderWidth, width + 2 * borderWidth, height + 2 * borderWidth);
    
        // Dibujar la paleta interna
        shape.setColor(Color.valueOf("0f380f"));
        shape.rect(x, y, width, height);
    }    
}

package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.game.BlockBreakerGame;

public class PauseScreen {
    private BlockBreakerGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public PauseScreen(BlockBreakerGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(3, 2);
    }

    public void render() {
        batch.begin();
        game.getFont().setColor(Color.valueOf("ff0000")); // Cambiar a color rojo
        game.getFont().draw(batch, "PAUSADO", 350, 300);
        game.getFont().setColor(Color.valueOf("ff8800")); // Cambiar a color naranja
        game.getFont().draw(batch, "Presiona ESC para continuar", 250, 200);
        game.getFont().draw(batch, "Presiona Q para salir", 250, 150);
        game.getFont().draw(batch, "PUNTAJE: " + game.getPuntaje(), 10, 500);
        game.getFont().draw(batch, "VIDAS: " + game.getVidas(), 10, 450);
        game.getFont().draw(batch, "NIVEL: " + game.getNivel(), 10, 400);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.exit(); // Salir del programa
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

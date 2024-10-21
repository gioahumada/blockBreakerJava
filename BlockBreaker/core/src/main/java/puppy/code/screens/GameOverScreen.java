package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class GameOverScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameOverScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    public void render(int puntajeMaximo) {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Mostrar Game Over y puntaje máximo
        font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 100);
        font.draw(batch, "Puntaje Maximo: " + puntajeMaximo, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
        font.draw(batch, "Presiona ESPACIO para reiniciar", Gdx.graphics.getWidth() / 2 - 180, Gdx.graphics.getHeight() / 2 - 50);
        font.draw(batch, "Presiona ESC para volver al menú", Gdx.graphics.getWidth() / 2 - 180, Gdx.graphics.getHeight() / 2 - 100);

        batch.end();

        // Reiniciar el juego si se presiona ESPACIO
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.reiniciarJuego();
        }

        // Volver al menú principal si se presiona ESC
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.volverAlMenu();
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

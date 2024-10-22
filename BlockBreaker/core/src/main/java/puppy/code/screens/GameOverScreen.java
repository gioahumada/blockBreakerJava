package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class GameOverScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    // Añadir la textura de la imagen de Game Over
    private Texture gameOverImage;

    public GameOverScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);

        // Cargar la imagen de Game Over
        gameOverImage = new Texture(Gdx.files.internal("assets/gameover.png"));
    }

    public void render(int puntajeMaximo) {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar la imagen de Game Over
        batch.draw(gameOverImage, 0, 0, 800, 480);  // Ajusta el tamaño y posición según necesites

        // Mostrar el puntaje máximo
        font.draw(batch, "Puntaje Maximo: " + puntajeMaximo, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 50);

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
        gameOverImage.dispose();  // Liberar la textura de la imagen de Game Over
    }
}

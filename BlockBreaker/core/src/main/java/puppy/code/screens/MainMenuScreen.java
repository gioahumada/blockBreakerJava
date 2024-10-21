package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class MainMenuScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture menuBackground;  // Cargar la imagen de fondo del menú

    public MainMenuScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);  // Ajustar la cámara a 800x600
        batch = new SpriteBatch();

        // Cargar la imagen desde Assets
        menuBackground = new Texture("mainscreen.png");
    }

    public void render() {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Dibujar la imagen del menú de fondo
        batch.draw(menuBackground, 0, 0, 800, 600);  // Asegúrate de que la imagen se ajuste a 800x600

        batch.end();

        // Funcionalidad para iniciar el juego y mostrar el tutorial
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            game.startGame();  // Iniciar el juego
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            // Mostrar el tutorial, lógica futura
            System.out.println("Tutorial (a implementar)");
        }
    }

    public void dispose() {
        batch.dispose();
        menuBackground.dispose();  // Liberar la textura de la imagen
    }
}

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
    private Texture menuBackground;

    // Nueva pantalla para el tutorial
    private TutorialScreen tutorialScreen;

    public MainMenuScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();
        menuBackground = new Texture("mainscreen.png");

        // Crear la pantalla del tutorial
        tutorialScreen = new TutorialScreen(game);
    }

    public void render() {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(menuBackground, 0, 0, 800, 600);
        batch.end();

        // Verificar si el usuario presiona el botón para empezar el juego o el tutorial
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            game.startGame();  // Iniciar el juego
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            game.activarTutorial();  // Activar la pantalla del tutorial
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            Gdx.app.exit();  // Salir del juego
        }
    }

    public void dispose() {
        batch.dispose();
        menuBackground.dispose();
    }
}

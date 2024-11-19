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
    private TutorialScreen tutorialScreen;

    public MainMenuScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();
        menuBackground = new Texture("mainscreen.png");
        tutorialScreen = new TutorialScreen(game);
    }

    public void render() {
        handleInput();
        
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(menuBackground, 0, 0, 800, 600);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setDificultad("FACIL");
            game.mainMenuMusic.stop();
            game.gameMusic.play();
            game.startGame();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setDificultad("DIFICIL");
            game.mainMenuMusic.stop();
            game.gameMusic.play();
            game.startGame();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.mainMenuMusic.stop();
            game.tutorialMusic.play();
            game.activarTutorial();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            Gdx.app.exit();
        }
    }

    public void dispose() {
        batch.dispose();
        menuBackground.dispose();
    }
}

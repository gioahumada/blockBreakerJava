package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class TutorialScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture[] tutorialImages;
    private int currentImageIndex;

    public TutorialScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();

        // Cargar las imágenes del tutorial
        tutorialImages = new Texture[3];
        tutorialImages[0] = new Texture("tutorial1.png");  // Reemplazar con las rutas de tus imágenes
        tutorialImages[1] = new Texture("tutorial2.png");
        tutorialImages[2] = new Texture("tutorial3.png");

        currentImageIndex = 0;  // Empezar con la primera imagen
    }

    public void render() {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tutorialImages[currentImageIndex], 0, 0, 800, 600);  // Dibujar la imagen actual
        batch.end();

        // Navegar entre las imágenes usando las flechas izquierda y derecha
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            currentImageIndex++;
            if (currentImageIndex >= tutorialImages.length) {
                currentImageIndex = tutorialImages.length - 1;  // No ir más allá de la última imagen
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            currentImageIndex--;
            if (currentImageIndex < 0) {
                currentImageIndex = 0;  // No ir antes de la primera imagen
            }
        }

        // Salir del tutorial con la tecla ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.tutorialMusic.stop();
            game.mainMenuMusic.play();
            game.volverAlMenu();
        }
    }

    public void dispose() {
        for (Texture image : tutorialImages) {
            image.dispose();  // Liberar los recursos de las texturas
        }
        batch.dispose();
    }
}

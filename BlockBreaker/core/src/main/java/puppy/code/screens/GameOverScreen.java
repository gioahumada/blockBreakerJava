package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class GameOverScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture gameOverImage;
    private GlyphLayout layout;

    public GameOverScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Estilo de la fuente
        font = new BitmapFont();
        font.getData().setScale(3); // Aumentar tamaño de la fuente

        layout = new GlyphLayout(); // Ayuda a medir el texto

        // Cargar la imagen de Game Over
        gameOverImage = new Texture(Gdx.files.internal("gameover.png"));
    }

    public void render(int puntajeMaximo) {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar la imagen de Game Over
        batch.draw(gameOverImage, 0, 0, 800, 480);

        // Preparar el texto del puntaje máximo
        String textoPuntaje = "Puntaje Máximo: " + puntajeMaximo;
        layout.setText(font, textoPuntaje);

        // Calcular posición para centrar el texto
        float textX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float textY = (Gdx.graphics.getHeight() / 2) - 50;

        // Dibujar sombra negra para el texto
        font.setColor(Color.BLACK);
        font.draw(batch, textoPuntaje, textX + 2, textY - 2);

        // Dibujar el texto principal con el color #306230
        font.setColor(new Color(0x306230ff));  // El color personalizado
        font.draw(batch, textoPuntaje, textX, textY);

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
        gameOverImage.dispose();
    }
}

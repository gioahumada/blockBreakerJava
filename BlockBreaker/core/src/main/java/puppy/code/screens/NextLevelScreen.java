package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class NextLevelScreen {
    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;

    public NextLevelScreen(BlockBreakerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Cargar y configurar la fuente
        font = new BitmapFont();
        font.getData().setScale(2);

        layout = new GlyphLayout(); // Para medir el texto
    }

    public void render(int nivelActual) {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Preparar el texto que muestra el nivel al que se pasa
        String textoNivel = "¡Nivel " + nivelActual + " completado!";
        layout.setText(font, textoNivel);

        // Calcular posición para centrar el texto
        float textX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float textY = (Gdx.graphics.getHeight() / 2) - 50;

        // Dibujar el texto con sombra negra
        font.setColor(Color.BLACK); // Negro
        font.draw(batch, textoNivel, textX + 2, textY - 2);

        // Dibujar el texto principal en color verde (#306230)
        font.setColor(Color.valueOf("306230"));
        font.draw(batch, textoNivel, textX, textY);

        batch.end();

        // Continuar al siguiente nivel si se presiona ESPACIO
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.cargarSiguienteNivel();
        }

        // Volver al menú si se presiona ESC
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.volverAlMenu();
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

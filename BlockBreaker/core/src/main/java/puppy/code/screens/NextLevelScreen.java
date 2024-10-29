package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import puppy.code.game.BlockBreakerGame;

public class NextLevelScreen {
    private static final Color OVERLAY_COLOR = Color.valueOf("0f380f"); // Fondo translúcido
    private static final Color TEXT_COLOR = Color.valueOf("9bbc0f");      // Verde oscuro
    private static final Color SHADOW_COLOR = Color.valueOf("306230");   // Sombra negra translúcida

    private BlockBreakerGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private ShapeRenderer shapeRenderer;

    public NextLevelScreen(BlockBreakerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 480);
        this.batch = new SpriteBatch();

        // Cargar y configurar la fuente
        this.font = new BitmapFont();
        this.font.getData().setScale(2.5f);
        this.layout = new GlyphLayout(); // Para medir el texto
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(int nivelActual) {
        // Dibujar fondo translúcido
        drawBackgroundOverlay();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Preparar el texto de nivel completado
        String textoNivel = "¡Nivel " + nivelActual + " completado!";
        layout.setText(font, textoNivel);

        // Calcular posición para centrar el texto
        float textX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float textY = (Gdx.graphics.getHeight() / 2) + layout.height;

        // Dibujar sombra del texto
        font.setColor(SHADOW_COLOR);
        font.draw(batch, textoNivel, textX + 3, textY - 3);

        // Dibujar texto principal
        font.setColor(TEXT_COLOR);
        font.draw(batch, textoNivel, textX, textY);

        // Dibujar instrucciones para continuar o salir
        String textoContinuar = "Presiona ESPACIO para continuar";
        String textoSalir = "Presiona ESC para volver al menú";

        layout.setText(font, textoContinuar);
        font.draw(batch, textoContinuar, (Gdx.graphics.getWidth() - layout.width) / 2, textY - 60);

        layout.setText(font, textoSalir);
        font.draw(batch, textoSalir, (Gdx.graphics.getWidth() - layout.width) / 2, textY - 100);

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

    private void drawBackgroundOverlay() {
        // Fondo translúcido que oscurece el juego mientras muestra el texto
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(OVERLAY_COLOR);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

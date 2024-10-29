package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.game.BlockBreakerGame;

public class PauseScreen {
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0.7f); // Fondo negro translúcido
    private static final Color RECT_COLOR = Color.valueOf("306230");         // Verde oscuro
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 0.5f);      // Sombra negra translúcida
    private static final Color TEXT_COLOR = Color.valueOf("9bbc0f");         // Verde claro

    private BlockBreakerGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    private static final int MARGIN = 50;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int RECT_WIDTH = SCREEN_WIDTH - 2 * MARGIN;
    private static final int RECT_HEIGHT = SCREEN_HEIGHT - 2 * MARGIN;
    private static final int SHADOW_OFFSET = 10;
    private static final int TEXT_SPACING = 40; // Espaciado entre líneas de texto

    public PauseScreen(BlockBreakerGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2.0f);  // Ajustamos el tamaño del texto para mejor claridad
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
    }

    public void render() {
        drawBackgroundOverlay();
        drawShadowRectangle();
        drawRoundedRectangle();

        // Comenzamos en una posición más alta para evitar que el texto se superponga
        int textYPosition = SCREEN_HEIGHT / 2 + 150;

        // Dibujamos cada línea con un espaciado adecuado para evitar colisiones
        drawTextCentered("PAUSADO", textYPosition);
        textYPosition -= TEXT_SPACING * 2;

        drawTextCentered("Presiona ESC para continuar", textYPosition);
        textYPosition -= TEXT_SPACING;

        drawTextCentered("Presiona Q para salir", textYPosition);
        textYPosition -= TEXT_SPACING * 2;

        // Información de puntaje, vidas y nivel
        drawTextCentered("PUNTAJE: " + game.getPuntaje(), textYPosition);
        textYPosition -= TEXT_SPACING;

        drawTextCentered("VIDAS: " + game.getVidas(), textYPosition);
        textYPosition -= TEXT_SPACING;

        drawTextCentered("NIVEL: " + game.getNivel(), textYPosition);

        // Detectar tecla Q para salir
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.exit(); // Salir del programa
        }
    }

    private void drawBackgroundOverlay() {
        // Fondo translúcido para oscurecer el juego mientras está en pausa
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
    }

    private void drawShadowRectangle() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(SHADOW_COLOR);
        shapeRenderer.rect(MARGIN + SHADOW_OFFSET, MARGIN - SHADOW_OFFSET, RECT_WIDTH, RECT_HEIGHT);
        shapeRenderer.end();
    }

    private void drawRoundedRectangle() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(RECT_COLOR);

        // Dibujar el rectángulo principal con bordes redondeados simulados
        int cornerRadius = 15;

        shapeRenderer.rect(MARGIN + cornerRadius, MARGIN, RECT_WIDTH - 2 * cornerRadius, RECT_HEIGHT); // Centro
        shapeRenderer.rect(MARGIN, MARGIN + cornerRadius, RECT_WIDTH, RECT_HEIGHT - 2 * cornerRadius); // Lados
        shapeRenderer.circle(MARGIN + cornerRadius, MARGIN + cornerRadius, cornerRadius); // Esquina inferior izquierda
        shapeRenderer.circle(MARGIN + RECT_WIDTH - cornerRadius, MARGIN + cornerRadius, cornerRadius); // Esquina inferior derecha
        shapeRenderer.circle(MARGIN + cornerRadius, MARGIN + RECT_HEIGHT - cornerRadius, cornerRadius); // Esquina superior izquierda
        shapeRenderer.circle(MARGIN + RECT_WIDTH - cornerRadius, MARGIN + RECT_HEIGHT - cornerRadius, cornerRadius); // Esquina superior derecha

        shapeRenderer.end();
    }

    private void drawTextCentered(String text, float yPosition) {
        batch.begin();
        font.setColor(TEXT_COLOR);
        layout.setText(font, text);
        float textX = MARGIN + RECT_WIDTH / 2 - layout.width / 2;
        font.draw(batch, text, textX, yPosition);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

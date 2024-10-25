package puppy.code.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.game.BlockBreakerGame;

public class PauseScreen {
    private BlockBreakerGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public PauseScreen(BlockBreakerGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2.5f, 2.5f);  // Ajustamos el tamaño del texto
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render() {
        // Color de fondo para el texto (usando uno de los colores de la imagen)
        Color backgroundColor = Color.valueOf("306230");  // Fondo oscuro
        Color textColor = Color.valueOf("9bbc0f"); // Color claro del texto

        // Dimensiones del rectángulo verde, ajustado para centrado
        int margin = 50;
        int rectWidth = 800 - 2 * margin;
        int rectHeight = 600 - 2 * margin;
        int rectX = margin;
        int rectY = margin;

        // Dibujar el rectángulo de fondo (verde)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(rectX, rectY, rectWidth, rectHeight);
        shapeRenderer.end();

        // Iniciar el batch para dibujar el texto
        batch.begin();

        // Variables comunes para facilitar el centrado relativo al fondo verde
        float rectCenterX = rectX + rectWidth / 2;
        float screenHeight = 600;

        // Texto principal "PAUSADO" centrado con respecto al rectángulo verde
        game.getFont().setColor(textColor);
        String pausedText = "PAUSADO";
        float pausedTextWidth = game.getFont().getRegion().getRegionWidth();
        float pausedTextX = rectCenterX - (pausedTextWidth / 2);
        float pausedTextY = screenHeight / 2 + 100;  // Centrado más alto
        game.getFont().draw(batch, pausedText, pausedTextX, pausedTextY);

        // Centrar y mostrar otros textos debajo del "PAUSADO", también centrados con respecto al fondo
        String continueText = "Presiona ESC para continuar";
        float continueTextWidth = game.getFont().getRegion().getRegionWidth();
        float continueTextX = rectCenterX - (continueTextWidth / 2);
        game.getFont().draw(batch, continueText, continueTextX, pausedTextY - 100);

        String quitText = "Presiona Q para salir";
        float quitTextWidth = game.getFont().getRegion().getRegionWidth();
        float quitTextX = rectCenterX - (quitTextWidth / 2);
        game.getFont().draw(batch, quitText, quitTextX, pausedTextY - 150);

        // Centrar puntaje, vidas y nivel también con respecto al rectángulo verde
        String puntajeText = "PUNTAJE: " + game.getPuntaje();
        float puntajeTextWidth = game.getFont().getRegion().getRegionWidth();
        float puntajeTextX = rectCenterX - (puntajeTextWidth / 2);
        game.getFont().draw(batch, puntajeText, puntajeTextX, pausedTextY + 150);

        String vidasText = "VIDAS: " + game.getVidas();
        float vidasTextWidth = game.getFont().getRegion().getRegionWidth();
        float vidasTextX = rectCenterX - (vidasTextWidth / 2);
        game.getFont().draw(batch, vidasText, vidasTextX, pausedTextY + 100);

        String nivelText = "NIVEL: " + game.getNivel();
        float nivelTextWidth = game.getFont().getRegion().getRegionWidth();
        float nivelTextX = rectCenterX - (nivelTextWidth / 2);
        game.getFont().draw(batch, nivelText, nivelTextX, pausedTextY + 50);

        batch.end();

        // Detectar tecla Q para salir
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.exit(); // Salir del programa
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

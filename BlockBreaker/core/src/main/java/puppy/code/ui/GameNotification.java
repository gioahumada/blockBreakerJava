package puppy.code.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameNotification {
    private static final float DISPLAY_TIME = 2f;
    private static final float FADE_TIME = 0.5f;
    
    private String mensaje;
    private float tiempoRestante;
    private BitmapFont font;
    private GlyphLayout layout;
    private SpriteBatch batch;
    private Color colorTexto;
    
    public GameNotification() {
        font = new BitmapFont();
        font.getData().setScale(2f);
        layout = new GlyphLayout();
        batch = new SpriteBatch();
        colorTexto = new Color(0.188f, 0.384f, 0.188f, 1f); // Color #306230
    }
    
    public void mostrarPowerUp(String powerUp) {
        switch (powerUp) {
            case "TripleBallPowerUp":
                mensaje = "¡Triple Bola!";
                colorTexto = Color.valueOf("9bbc0f"); // Verde claro
                break;
            case "SpeedUpPowerUp":
                mensaje = "¡Velocidad Aumentada!";
                colorTexto = Color.valueOf("306230"); // Verde oscuro
                break;
            case "ExtraLifePowerUp":
                mensaje = "¡Vida Extra!";
                colorTexto = Color.valueOf("8bac0f"); // Verde medio
                break;
            default:
                mensaje = "¡Power Up!";
        }
        tiempoRestante = DISPLAY_TIME;
    }
    
    public void mostrarMovimiento(String estrategia) {
        mensaje = "¡Movimiento " + traducirEstrategia(estrategia) + "!";
        colorTexto = Color.valueOf("306230");
        tiempoRestante = DISPLAY_TIME;
    }
    
    private String traducirEstrategia(String estrategia) {
        switch (estrategia) {
            case "NormalMovement": return "Normal";
            case "ZigZagMovement": return "Zigzag";
            case "WaveMovement": return "Ondulado";
            default: return estrategia;
        }
    }
    
    public void render() {
        if (tiempoRestante <= 0) return;
        
        tiempoRestante -= Gdx.graphics.getDeltaTime();
        float alpha = tiempoRestante < FADE_TIME ? tiempoRestante / FADE_TIME : 1f;
        
        layout.setText(font, mensaje);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2;
        
        batch.begin();
        
        // Dibujar sombra
        font.setColor(0, 0, 0, alpha * 0.5f);
        font.draw(batch, mensaje, x + 2, y - 2);
        
        // Dibujar texto principal
        colorTexto.a = alpha;
        font.setColor(colorTexto);
        font.draw(batch, mensaje, x, y);
        
        batch.end();
    }
    
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
} 
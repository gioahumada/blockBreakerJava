package puppy.code.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StrategyNotification {
    private static final float DISPLAY_TIME = 2f;
    private static final float FADE_TIME = 0.5f;
    
    private String mensaje;
    private float tiempoRestante;
    private BitmapFont font;
    private GlyphLayout layout;
    private SpriteBatch batch;
    
    public StrategyNotification() {
        font = new BitmapFont();
        font.getData().setScale(2f);
        layout = new GlyphLayout();
        batch = new SpriteBatch();
    }
    
    public void mostrarMensaje(String estrategia) {
        mensaje = "¡Movimiento " + traducirEstrategia(estrategia) + "!";
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
        font.setColor(0.188f, 0.384f, 0.188f, alpha);
        font.draw(batch, mensaje, x, y);
        
        batch.end();
    }
    
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
} 
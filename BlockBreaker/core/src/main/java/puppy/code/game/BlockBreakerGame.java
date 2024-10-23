
package puppy.code.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;
import puppy.code.blocks.Block;
import puppy.code.blocks.HardBlock;
import puppy.code.blocks.NormalBlock;
import puppy.code.entities.Paddle;
import puppy.code.entities.PingBall;
import puppy.code.interfaces.Damageable;
import puppy.code.powerups.FallingPowerUp;
import puppy.code.powerups.TripleBallPowerUp;
import puppy.code.powerups.SpeedUpPowerUp;
import puppy.code.screens.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakerGame extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private PingBall ball;
    private Paddle pad;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int vidas;
    private int puntaje;

    private boolean powerUpBolaExtraActivado = false; // Bandera para saber si el PowerUp de bola extra ya se activó
    private boolean powerUpRalentizacionActivado = false;   // Bandera para saber si el PowerUp de ralentización ya se activó

    private int nivel;
    private int puntajeMaximo;
    private boolean nivelCompletado;

    private Texture backgroundTexture;
    private Color backgroundColor;

    private NextLevelScreen nextLevelScreen;
    private PauseScreen pauseScreen;
    private MainMenuScreen mainMenuScreen;
    private GameOverScreen gameOverScreen;
    private boolean paused = false;
    private TutorialScreen tutorialScreen;  // Pantalla de tutorial
    private boolean menuPrincipal = true;
    private boolean gameOver = false;
    private boolean tutorialActivo = false; // Estado para indicar si el tutorial está activo
    private List<PingBall> bolasActivas = new ArrayList<>(); // Lista de bolas activas

    private ArrayList<FallingPowerUp> fallingPowerUps = new ArrayList<>(); // Lista de power-ups en caída

    /* Musica */
    public static Music breakSound;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3, 2);

        shape = new ShapeRenderer();

        backgroundTexture = new Texture("background.png");
        backgroundColor = new Color(0, 0, 0, 1);

        mainMenuScreen = new MainMenuScreen(this);
        gameOverScreen = new GameOverScreen(this);
        tutorialScreen = new TutorialScreen(this);  // Inicializar la pantalla de tutorial
        nextLevelScreen = new NextLevelScreen(this);
        pauseScreen = new PauseScreen(this);
        breakSound = Gdx.audio.newMusic(Gdx.files.internal("break_sound.mp3"));

        iniciarJuego();
    }

    public void iniciarJuego() {
        nivel = 1;
        vidas = 3;
        puntaje = 0;
        puntajeMaximo = 0;
        nivelCompletado = false;
        gameOver = false;

        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10); // Initialize pad first
        int velocidadBase = 5 + nivel; // Aumentar la velocidad base en función del nivel
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, pad.getY() + pad.getHeight() + 11, 10, velocidadBase, velocidadBase, true);

        crearBloques(2 + nivel);
        bolasActivas.clear(); // Limpiar bolas activas
        bolasActivas.add(ball); // Añadir la bola principal a la lista de bolas activas

        menuPrincipal = true;
        tutorialActivo = false;  // Asegurarse de que el tutorial no esté activo
    }

    public void crearBloques(int filas) {
        blocks.clear();
        int blockWidth = 70;
        int blockHeight = 26;
        int y = Gdx.graphics.getHeight();
        for (int cont = 0; cont < filas; cont++) {
            y -= blockHeight + 10;
            for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
                if (Math.random() > 0.8) {
                    blocks.add(new HardBlock(x, y, blockWidth, blockHeight));
                } else {
                    blocks.add(new NormalBlock(x, y, blockWidth, blockHeight));
                }
            }
        }
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        if (paused) {
            pauseScreen.render();  // Show the pause screen
            return;  // Skip the rest of the render method
        }

        if (menuPrincipal) {
            mainMenuScreen.render();  // Show the main menu
        } else if (gameOver) {
            gameOverScreen.render(puntajeMaximo);  // Show the Game Over screen
        } else if (tutorialActivo) {
            tutorialScreen.render();  // Show the tutorial screen if active
        } else if (nivelCompletado) {
            nextLevelScreen.render(nivel);  // Show the Next Level screen
        } else {
            Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw(backgroundTexture, 0, 0, 800, 600);
            batch.end();

            shape.begin(ShapeRenderer.ShapeType.Filled);
            pad.draw(shape);

            // Dibujar y actualizar power-ups
            for (FallingPowerUp powerUp : fallingPowerUps) {
                powerUp.update();
                powerUp.draw(shape);

                // Verificar si el paddle recoge el power-up
                if (powerUp.collidesWith(pad)) {
                    powerUp.activate(this);
                    fallingPowerUps.remove(powerUp);
                    break; // Evitar modificar la lista mientras se itera
                }
            }

            for (PingBall bola : bolasActivas) {
                if (bola.estaQuieto()) {
                    bola.setXY(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11);
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        bola.setEstaQuieto(false);
                    }
                } else {
                    bola.update();
                }

                // Chequeo de colisiones con los bordes
                if (bola.getY() < 0) {
                    vidas--;
                    if (puntaje > puntajeMaximo) {
                        puntajeMaximo = puntaje;
                    }
                    if (vidas < 0) {
                        gameOver = true;
                    } else {
                        bola.setXY(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11);
                        bola.setEstaQuieto(true);
                    }
                }

                for (Block b : blocks) {
                    b.draw(shape);
                    bola.checkCollision(b);
                }

                bola.checkCollision(pad);
                bola.draw(shape);
            }

            // Remover bloques destruidos y generar power-ups
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b instanceof Damageable) {
                    Damageable damageable = (Damageable) b;
                    if (damageable.isDestroyed()) {
                        puntaje++;
                        blocks.remove(b);
                        generarPowerUp(b.getX(), b.getY());  // Generar power-up al destruir un bloque
                        i--;
                    }
                }
            }

            activarPowerUps(); // Asegúrate de que este método esté definido correctamente

            if (blocks.isEmpty()) {
                nivelCompletado = true;  // Marcar el nivel como completado
            }

            shape.end();

            batch.begin();
            font.getData().setScale(2);
            font.setColor(Color.valueOf("9bbc0f"));
            font.draw(batch, "PUNTAJE: " + puntaje, 10, 25);
            font.draw(batch, "VIDAS: " + vidas, Gdx.graphics.getWidth() - 150, 25);
            font.draw(batch, "NIVEL: " + nivel, Gdx.graphics.getWidth() / 2 - 40, 25);
            batch.end();
        }
    }

    // Método para verificar y activar los power-ups
    private void activarPowerUps() {
        // Aquí podrías implementar la lógica para activar ciertos power-ups según el puntaje o nivel
    }

    // Método para generar power-ups aleatorios
    private void generarPowerUp(int x, int y) {
        double random = Math.random();
        if (random < 0.2) {  // 20% de probabilidad de generar un power-up
            if (Math.random() < 0.5) {
                fallingPowerUps.add(new TripleBallPowerUp(x, y));  // Añadir power-up de 3 bolas
            } else {
                fallingPowerUps.add(new SpeedUpPowerUp(x, y));     // Añadir power-up de velocidad
            }
        }
    }

    public void agregarNuevaBola() {
        // Verificar que haya bolas en juego
        if (!bolasActivas.isEmpty()) {
            // Elegir la primera bola existente para duplicar
            PingBall bolaOriginal = bolasActivas.get(0);

            // Crear una nueva bola con las mismas propiedades que la bola original
            PingBall nuevaBola = new PingBall(
                bolaOriginal.getX(),  // Misma posición que la original
                bolaOriginal.getY(),
                bolaOriginal.getSize(),
                bolaOriginal.getXSpeed(),
                bolaOriginal.getYSpeed(),
                false); // La nueva bola no es la principal

            // Añadir la nueva bola a la lista de bolas activas
            bolasActivas.add(nuevaBola);
        }
    }

    public void startGame() {
        menuPrincipal = false;
        nivel = 1;
        vidas = 3;
        puntaje = 0;
        puntajeMaximo = 0;
        nivelCompletado = false;

        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        bolasActivas.clear(); // Limpiar bolas activas
        bolasActivas.add(ball); // Añadir la bola principal a la lista de bolas activas
        crearBloques(2 + nivel);
    }

    public void reiniciarJuego() {
        gameOver = false;
        iniciarNuevoJuego();
    }

    public void volverAlMenu() {
        menuPrincipal = true;
        iniciarJuego();
    }

    public void iniciarNuevoJuego() {
        nivel = 1;
        vidas = 3;
        puntaje = 0;  // Reiniciar solo el puntaje actual
        nivelCompletado = false;
        gameOver = false;
        blocks.clear(); // Limpiar bloques
        bolasActivas.clear(); // Limpiar bolas activas

        // Reiniciar la bola y la paleta
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, pad.getY() + pad.getHeight() + 11, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        bolasActivas.add(ball); // Añadir la bola principal a la lista de bolas activas

        crearBloques(2 + nivel);  // Crear nuevos bloques

        // Reiniciar banderas de power-ups
        powerUpBolaExtraActivado = false;
        powerUpRalentizacionActivado = false;
    }

    public void cargarSiguienteNivel() {
        nivel++;  // Increment the level
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);  // Reset ball position
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);  // Reset paddle position
        crearBloques(2 + nivel);  // Create new blocks for the next level
        nivelCompletado = false;  // Reset the level completed flag
    }

    public List<PingBall> getBolasActivas() {
        return bolasActivas;
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        gameOverScreen.dispose();
        tutorialScreen.dispose();  // Liberar recursos del tutorial
        pauseScreen.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        shape.dispose();
        breakSound.dispose();
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public int getNivel() {
        return nivel;
    }

    public void activarTutorial() {
        tutorialActivo = true;
        menuPrincipal = false;
    }


}

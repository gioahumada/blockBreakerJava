package puppy.code.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    private boolean powerUpBolaExtraActivado = false;
    private boolean powerUpRalentizacionActivado = false;

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
    private TutorialScreen tutorialScreen;
    private boolean menuPrincipal = true;
    private boolean gameOver = false;
    private boolean tutorialActivo = false;
    private List<PingBall> bolasActivas = new ArrayList<>();

    private ArrayList<FallingPowerUp> fallingPowerUps = new ArrayList<>();

    /* Musica */
    public static Music breakSound;
    public Music mainMenuMusic;
    public Music tutorialMusic;
    public Music gameMusic;
    private Sound hardBlockHitSound;
    private Sound powerUpPickSound;
    private Sound paddleHitSound;
    private Music gameOverMusic;

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
        tutorialScreen = new TutorialScreen(this);
        nextLevelScreen = new NextLevelScreen(this);
        pauseScreen = new PauseScreen(this);

        breakSound = Gdx.audio.newMusic(Gdx.files.internal("break_sound.mp3"));
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("main_menu_music.mp3"));
        tutorialMusic = Gdx.audio.newMusic(Gdx.files.internal("tutorial_music.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game_music.mp3"));
        hardBlockHitSound = Gdx.audio.newSound(Gdx.files.internal("hard_block_hit.mp3"));
        powerUpPickSound = Gdx.audio.newSound(Gdx.files.internal("powerup_pick.mp3"));
        paddleHitSound = Gdx.audio.newSound(Gdx.files.internal("paddle_hit.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("game_over.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.play();

        iniciarJuego();
    }

    public void iniciarJuego() {
        nivel = 1;
        vidas = 3;
        puntaje = 0;
        puntajeMaximo = 0;
        nivelCompletado = false;
        gameOver = false;

        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        int velocidadBase = 5 + nivel;
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, pad.getY() + pad.getHeight() + 11, 10, velocidadBase, velocidadBase, true);

        crearBloques(2 + nivel);
        bolasActivas.clear();
        bolasActivas.add(ball);

        menuPrincipal = true;
        tutorialActivo = false;
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
            if (!menuPrincipal && !gameOver && !tutorialActivo && !nivelCompletado) {
                paused = !paused;
            }
        }

        if (paused) {
            pauseScreen.render();
            return;
        }

        if (menuPrincipal) {
            mainMenuScreen.render();
        } else if (gameOver) {
            gameOverScreen.render(puntajeMaximo);
        } else if (tutorialActivo) {
            tutorialScreen.render();
        } else if (nivelCompletado) {
            nextLevelScreen.render(nivel);
        } else {
            Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Dibujar el fondo
            batch.begin();
            batch.draw(backgroundTexture, 0, 0, 800, 600);
            batch.end();

            // Dibujar elementos con ShapeRenderer
            shape.begin(ShapeRenderer.ShapeType.Filled);
            pad.draw(shape);

            for (PingBall bola : new ArrayList<>(bolasActivas)) {
                bola.draw(shape);
            }

            for (Block b : blocks) {
                b.draw(shape);
            }

            shape.end();

            // Dibujar y actualizar power-ups con SpriteBatch
            batch.begin();

            for (FallingPowerUp powerUp : new ArrayList<>(fallingPowerUps)) {
                powerUp.update();
                powerUp.draw(batch);

                // Verificar si el paddle recoge el power-up
                if (powerUp.collidesWith(pad)) {
                    powerUp.activate(this);
                    fallingPowerUps.remove(powerUp);
                    powerUp.dispose(); // Liberar recursos

                    // Reproducir sonido de recolección de power-up
                    powerUpPickSound.play();

                    break;
                }
            }

            batch.end();

            // Actualizar lógica de juego (colisiones, movimientos, etc.)
            for (PingBall bola : new ArrayList<>(bolasActivas)) {
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
                    bolasActivas.remove(bola);
                    break;
                }

                for (Block b : blocks) {
                    bola.checkCollision(b);
                }

                bola.checkCollision(pad);
            }

            // Si no quedan pelotas activas, restar una vida y reiniciar
            if (bolasActivas.isEmpty()) {
                vidas--;
                if (puntaje > puntajeMaximo) {
                    puntajeMaximo = puntaje;
                }
                if (vidas < 0) {
                    gameOver = true;

                    // Detener música del juego y reproducir música de Game Over
                    gameMusic.stop();
                    gameOverMusic.play();
                } else {
                    // Crear una nueva pelota en estado quieto sobre la paleta
                    PingBall nuevaPelota = new PingBall(
                        pad.getX() + pad.getWidth() / 2 - 5,
                        pad.getY() + pad.getHeight() + 11,
                        10, 5 + nivel, 7 + nivel, true);
                    bolasActivas.add(nuevaPelota);
                }
            }

            // Remover bloques destruidos y generar power-ups
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b instanceof Damageable) {
                    Damageable damageable = (Damageable) b;
                    if (damageable.isDestroyed()) {
                        puntaje++;
                        blocks.remove(b);
                        generarPowerUp(b.getX(), b.getY());
                        i--;
                    }
                }
            }

            if (blocks.isEmpty()) {
                nivelCompletado = true;
            }

            // Dibujar texto de puntaje, vidas y nivel
            batch.begin();
            font.getData().setScale(2);
            font.setColor(Color.valueOf("9bbc0f"));
            font.draw(batch, "PUNTAJE: " + puntaje, 10, 25);
            font.draw(batch, "VIDAS: " + vidas, Gdx.graphics.getWidth() - 150, 25);
            font.draw(batch, "NIVEL: " + nivel, Gdx.graphics.getWidth() / 2 - 40, 25);
            batch.end();
        }
    }

    // Método para generar power-ups aleatorios
    private void generarPowerUp(int x, int y) {
        double random = Math.random();
        if (random < 0.2) {  // 20% de probabilidad de generar un power-up
            if (Math.random() < 0.5) {
                fallingPowerUps.add(new TripleBallPowerUp(x, y));
            } else {
                fallingPowerUps.add(new SpeedUpPowerUp(x, y));
            }
        }
    }

    public void agregarNuevaBola(int deltaXSpeed) {
        // Verificar que haya bolas en juego
        if (!bolasActivas.isEmpty()) {
            // Elegir la primera bola existente para duplicar
            PingBall bolaOriginal = bolasActivas.get(0);

            // Crear una nueva bola con las mismas propiedades que la bola original
            PingBall nuevaBola = new PingBall(
                bolaOriginal.getX(),
                bolaOriginal.getY(),
                bolaOriginal.getSize(),
                bolaOriginal.getXSpeed() + deltaXSpeed,
                bolaOriginal.getYSpeed(),
                false);

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

        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        ball = new PingBall(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11, 10, 5 + nivel, 7 + nivel, true);
        bolasActivas.clear();
        bolasActivas.add(ball);
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
        puntaje = 0;
        nivelCompletado = false;
        gameOver = false;
        blocks.clear();
        bolasActivas.clear();

        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        ball = new PingBall(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11, 10, 5 + nivel, 7 + nivel, true);
        bolasActivas.add(ball);

        crearBloques(2 + nivel);

        powerUpBolaExtraActivado = false;
        powerUpRalentizacionActivado = false;
    }

    public void cargarSiguienteNivel() {
        nivel++;
        nivelCompletado = false;

        // Reiniciar la paleta
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);

        // Reiniciar la pelota y establecerla en estado quieto
        ball = new PingBall(
            pad.getX() + pad.getWidth() / 2 - 5,
            pad.getY() + pad.getHeight() + 11,
            10, 5 + nivel, 7 + nivel, true);

        // Limpiar y añadir la pelota a la lista de bolas activas
        bolasActivas.clear();
        bolasActivas.add(ball);

        // Crear nuevos bloques para el siguiente nivel
        crearBloques(2 + nivel);
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
        tutorialScreen.dispose();
        pauseScreen.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        shape.dispose();
        breakSound.dispose();

        mainMenuMusic.dispose();
        tutorialMusic.dispose();
        gameMusic.dispose();
        hardBlockHitSound.dispose();
        powerUpPickSound.dispose();
        paddleHitSound.dispose();
        gameOverMusic.dispose();

        // Liberar recursos de los power-ups
        for (FallingPowerUp powerUp : fallingPowerUps) {
            powerUp.dispose();
        }
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

package puppy.code.game;

import com.badlogic.gdx.audio.Music;
import puppy.code.blocks.Block;
import puppy.code.blocks.HardBlock;
import puppy.code.blocks.NormalBlock;
import puppy.code.entities.Paddle;
import puppy.code.entities.PingBall;
import puppy.code.interfaces.Damageable;
import puppy.code.screens.MainMenuScreen;
import puppy.code.screens.GameOverScreen;
import puppy.code.screens.NextLevelScreen;
import puppy.code.screens.TutorialScreen;

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
    private int nivel;
    private int puntajeMaximo;
    private boolean nivelCompletado;

    private Texture backgroundTexture;
    private Color backgroundColor;

    private NextLevelScreen nextLevelScreen;

    private MainMenuScreen mainMenuScreen;
    private GameOverScreen gameOverScreen;
    private TutorialScreen tutorialScreen;  // Pantalla de tutorial
    private boolean menuPrincipal = true;
    private boolean gameOver = false;
    private boolean tutorialActivo = false; // Estado para indicar si el tutorial está activo

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

        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
        crearBloques(2 + nivel);

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
        if (menuPrincipal) {
            mainMenuScreen.render();  // Show the main menu
        } else if (gameOver) {
            gameOverScreen.render(puntajeMaximo);  // Show the Game Over screen
        } else if (tutorialActivo) {
            tutorialScreen.render();  // Show the tutorial screen if active
        } else if (nivelCompletado) {
            nextLevelScreen.render(nivel);  // Show the Next Level screen
        } else{
            Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw(backgroundTexture, 0, 0, 800, 600);
            batch.end();

            shape.begin(ShapeRenderer.ShapeType.Filled);
            pad.draw(shape);

            if (ball.estaQuieto()) {
                ball.setXY(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11);
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    ball.setEstaQuieto(false);
                }
            } else {
                ball.update();
            }

            if (ball.getY() < 0) {
                vidas--;
                if (puntaje > puntajeMaximo) {
                    puntajeMaximo = puntaje;
                }
                if (vidas < 0) {
                    gameOver = true;
                } else {
                    ball = new PingBall(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11, 10, 5, 7, true);
                }
            }

            for (Block b : blocks) {
                b.draw(shape);
                ball.checkCollision(b);
            }

            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b instanceof Damageable) {
                    Damageable damageable = (Damageable) b;
                    if (damageable.isDestroyed()) {
                        puntaje++;
                        blocks.remove(b);
                        i--;
                    }
                }
            }

            // Verificar si todos los bloques han sido destruidos
            if (blocks.isEmpty())
            {
                nivelCompletado = true;  // Marcar el nivel como completado


            }

            ball.checkCollision(pad);
            ball.draw(shape);
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

    public void startGame() {
        menuPrincipal = false;
        nivel = 1;
        vidas = 3;
        puntaje = 0;
        puntajeMaximo = 0;
        nivelCompletado = false;

        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);
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

    public void iniciarNuevoJuego()
    {
        nivel = 1;
        vidas = 3;
        puntaje = 0;  // Reiniciar solo el puntaje actual
        nivelCompletado = false;
        gameOver = false;

        // Reiniciar la bola y la paleta
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);

        crearBloques(2 + nivel);  // Crear nuevos bloques
    }

    // Método para activar la pantalla de tutorial
    public void setScreen(TutorialScreen tutorialScreen) {
        tutorialActivo = true;  // Activa el tutorial
        menuPrincipal = false;  // Desactiva el menú principal
    }
    public void cargarSiguienteNivel() {
        nivel++;  // Increment the level
        ball = new PingBall(Gdx.graphics.getWidth() / 2 - 10, 41, 10, 5, 7, true);  // Reset ball position
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 40, 100, 10);  // Reset paddle position
        crearBloques(2 + nivel);  // Create new blocks for the next level
        nivelCompletado = false;  // Reset the level completed flag
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        gameOverScreen.dispose();
        tutorialScreen.dispose();  // Liberar recursos del tutorial
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        shape.dispose();
        breakSound.dispose();
    }

}

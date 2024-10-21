package puppy.code.game;

import puppy.code.blocks.Block;
import puppy.code.blocks.HardBlock;
import puppy.code.blocks.NormalBlock;
import puppy.code.entities.Paddle;
import puppy.code.entities.PingBall;
import puppy.code.interfaces.Damageable;
import puppy.code.screens.MainMenuScreen;
import puppy.code.screens.GameOverScreen;

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

    private MainMenuScreen mainMenuScreen;
    private GameOverScreen gameOverScreen;
    private boolean menuPrincipal = true;
    private boolean gameOver = false;

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
            mainMenuScreen.render();
        } else if (gameOver) {
            gameOverScreen.render(puntajeMaximo);
        } else {
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
        startGame();
    }

    public void volverAlMenu() {
        menuPrincipal = true;
        iniciarJuego();
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        gameOverScreen.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        shape.dispose();
    }
}
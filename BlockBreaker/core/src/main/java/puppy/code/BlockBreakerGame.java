package puppy.code;

import java.util.ArrayList;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


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
    private boolean gameOver; // Variable para controlar si es Game Over
    private float fadeOpacity; // Opacidad para el efecto de fade-in
    private int puntajeMaximo; // Almacena el puntaje máximo
    private boolean nivelCompletado = false;
    private float tiempoTransicion = 2.0f;  // Duración de la pantalla de transición en segundos

    // Variables para el menú principal
    private boolean menuPrincipal = true;  // Estado del menú principal
    private int opcionSeleccionada = 0;  // Opción seleccionada en el menú
    private boolean mostrarControlesActivo = false;


    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3, 2);
        nivel = 1;
        crearBloques(2+nivel);

        shape = new ShapeRenderer();
        ball = new PingBall(Gdx.graphics.getWidth()/2-10, 41, 10, 5, 7, true);
        pad = new Paddle(Gdx.graphics.getWidth()/2-50,40,100,10);
        vidas = 3;
        puntaje = 0;
        gameOver = false; // Inicialmente no es Game Over
        fadeOpacity = 0f; // Opacidad inicial para el efecto fade-in
        puntajeMaximo = 0; // Inicializamos el puntaje máximo en 0

    }
    public void crearBloques(int filas)
    {
        blocks.clear();
        int blockWidth = 70;
        int blockHeight = 26;
        int y = Gdx.graphics.getHeight();
        for (int cont = 0; cont < filas; cont++) {
            y -= blockHeight + 10;
            for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
                // Crear HardBlock aleatoriamente
                if (Math.random() > 0.8) {
                    blocks.add(new HardBlock(x, y, blockWidth, blockHeight)); // Bloque duro con durabilidad
                } else {
                    blocks.add(new NormalBlock(x, y, blockWidth, blockHeight)); // Bloque normal
                }
            }
        }
    }
    public void dibujaTextos() {
        //actualizar matrices de la cámara
        camera.update();
        //actualizar
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //dibujar textos
        font.draw(batch, "Puntos: " + puntaje, 10, 25);
        font.draw(batch, "Vidas : " + vidas, Gdx.graphics.getWidth()-20, 25);
        batch.end();
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpiar la pantalla

        // Si estamos en el menú principal, mostramos el menú
        if (mostrarControlesActivo) {
            mostrarControles();  // Mostrar controles si está activo
        } else if (menuPrincipal) {
            mostrarMenuPrincipal();  // Mostrar menú principal si no está jugando
        } else {

            Gdx.gl.glClearColor(0, 0, 0, 1); // Limpiar la pantalla con un fondo negro
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (gameOver) {
                mostrarGameOver(); // Muestra la pantalla de Game Over
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    reiniciarJuego(); // Reinicia el juego si se presiona ESPACIO
                }
                return; // Salimos del método para evitar que el juego siga ejecutándose
            }

            // Verificar si se debe mostrar la pantalla de transición entre niveles
            if (nivelCompletado) {
                tiempoTransicion -= Gdx.graphics.getDeltaTime();  // Reducir el tiempo de transición

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    // Al presionar espacio, se reinicia el estado de nivel y se oculta la pantalla de transición
                    nivelCompletado = false;  // Cambia el estado para continuar el juego
                    tiempoTransicion = 2.0f;  // Restablecer el tiempo de transición
                    return;  // Detener el render mientras se muestra la transición
                }

                if (tiempoTransicion <= 0) {
                    nivelCompletado = false;  // Finaliza la transición
                    tiempoTransicion = 2.0f;  // Restablecer el tiempo de transición para el próximo nivel

                    // Aquí puedes configurar el siguiente nivel
                    nivel++;
                    int filas = 3 + nivel;  // Aumentar el número de filas
                    int columnas = 10 + nivel;  // Aumentar el número de columnas
                    crearBloques(filas); // Crear bloques con mayor dificultad

                    // Aumentar la velocidad de la pelota
                    ball.setXY(ball.getXSpeed() + 1, ball.getYSpeed() + 1);
                } else {
                    mostrarTransicionNivel();  // Mostrar la pantalla de transición
                    return;  // Detener el render del juego mientras se muestra la transición
                }
            }

            // Aquí va la lógica del juego si no se está en transición
            shape.begin(ShapeRenderer.ShapeType.Filled);
            pad.draw(shape);

            // Lógica para monitorear el inicio del juego
            if (ball.estaQuieto()) {
                ball.setXY(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11);
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    ball.setEstaQuieto(false); // Lanzar la pelota solo si no está quieta
                }
            } else {
                ball.update();
            }


            // Verificar si la bola cae
            if (ball.getY() < 0) {
                vidas--;  // Restamos una vida cuando la pelota cae
                if (puntaje > puntajeMaximo) { // Verificar y actualizar el puntaje máximo
                    puntajeMaximo = puntaje;
                }
                if (vidas < 0) {
                    gameOver = true;  // Si no quedan vidas, activamos el estado de "Game Over"
                    fadeOpacity = 0f; // Reiniciar el fade-in al iniciar el Game Over
                } else {
                    // Reseteamos la pelota y continuamos el juego
                    ball = new PingBall(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11, 10, 5, 7, true);
                }
            }

            // Verificar si el nivel se terminó
            if (blocks.size() == 0) {
                nivelCompletado = true;  // Activa la transición al siguiente nivel
            }

            // Dibujar bloques
            for (Block b : blocks) {
                b.draw(shape);
                ball.checkCollision(b);
            }

            // Actualizar estado de los bloques
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b.destroyed) {
                    puntaje++;
                    blocks.remove(b);
                    i--; // Para no saltarse 1 tras eliminar del ArrayList
                }
            }

            ball.checkCollision(pad);
            ball.draw(shape);

            shape.end();
            dibujaTextos();
        }
    }

    @Override
    public void dispose () {

    }


    private void mostrarMenuPrincipal() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Limpiar pantalla
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Título del menú principal
        font.setColor(Color.YELLOW);
        font.getData().setScale(3);
        font.draw(batch, "Block Breaker Game", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 + 150);

        // Opciones del menú
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        font.draw(batch, "1. Iniciar Juego", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 + 50);
        font.draw(batch, "2. Controles", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
        font.draw(batch, "3. Salir", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 50);

        // Detectar entrada del usuario para navegar en el menú
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            menuPrincipal = false;  // Iniciar el juego
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            mostrarControlesActivo = true;  // Activar la pantalla de controles
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            Gdx.app.exit();  // Salir del juego
        }

        batch.end();
    }

    // Método para mostrar los controles e instrucciones del juego
    private void mostrarControles() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Limpiar pantalla
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Título de controles
        font.setColor(Color.CYAN);
        font.getData().setScale(2.5f);
        font.draw(batch, "Controles del Juego", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 + 150);

        // Controles e instrucciones
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        font.draw(batch, "Movimiento: Izquierda/Derecha con las teclas del teclado", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 + 50);
        font.draw(batch, "Lanzar la pelota: Presionar ESPACIO", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
        font.draw(batch, "Reiniciar Game Over: Presionar ESPACIO", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 - 50);
        font.draw(batch, "Objetivo: Destruir todos los bloques y evitar perder todas las vidas", Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 100);

        font.setColor(Color.YELLOW);
        font.draw(batch, "Presiona 0 para volver al menú principal", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 200);

        // Regresar al menú principal con ESCAPE
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            mostrarControlesActivo = false;  // Volver al menú principal
        }

        batch.end();
    }

    private void mostrarGameOver() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpiar pantalla

        // Incrementar la opacidad para el efecto de fade-in
        if (fadeOpacity < 1) {
            fadeOpacity += 0.01f; // Incrementar suavemente
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Ajustar color con opacidad para el fade-in
        font.setColor(1, 1, 1, fadeOpacity);
        font.draw(batch, "GAME OVER", (float) Gdx.graphics.getWidth() / 2 - 100, (float) Gdx.graphics.getHeight() / 2);
        font.draw(batch, "Presiona ESPACIO para reiniciar", (float) Gdx.graphics.getWidth() / 2 - 200, (float) Gdx.graphics.getHeight() / 2 - 50);
        font.draw(batch, "Puntaje Máximo: " + puntajeMaximo, (float) Gdx.graphics.getWidth() / 2 - 150, (float) Gdx.graphics.getHeight() / 2 - 100); // Mostrar el puntaje máximo

        batch.end();
    }

    private void reiniciarJuego() {
        gameOver = false;
        vidas = 3;
        puntaje = 0;
        nivel = 1;
        crearBloques(2 + nivel);
        ball = new PingBall(pad.getX() + pad.getWidth() / 2 - 5, pad.getY() + pad.getHeight() + 11, 10, 5, 7, true);

        // Restablecer color de la fuente y opacidad
        fadeOpacity = 0f; // Reiniciar la opacidad de fade-in a 0
        font.setColor(Color.WHITE); // Asegúrate de que el color de la fuente sea completamente blanco
    }

    private void mostrarTransicionNivel()
    {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "¡Nivel Completado!", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);
        batch.end();
    }
}

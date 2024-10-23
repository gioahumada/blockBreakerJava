package puppy.code.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import puppy.code.blocks.Block;
import puppy.code.interfaces.Damageable;
import puppy.code.entities.Paddle;

public class PingBall {
    private int x;
    private int y;
    private int size;
    private int xSpeed;
    private int ySpeed;
    private Color color = Color.valueOf("0f380f");
    private boolean estaQuieto;
    private int velocidadMinima = 2; // Velocidad mínima para evitar que la bola se detenga
    private boolean active;
    private int velocidadOriginalX;
    private int velocidadOriginalY;


    public PingBall(int x, int y, int size, int xSpeed, int ySpeed, boolean iniciaQuieto) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        velocidadOriginalX = xSpeed;
        velocidadOriginalY = ySpeed;
        estaQuieto = iniciaQuieto;

    }

    // Método para verificar si la bola está quieta
    public boolean estaQuieto() {
        return estaQuieto;
    }

    // Método para cambiar el estado de quietud de la bola
    public void setEstaQuieto(boolean estaQuieto) {
        this.estaQuieto = estaQuieto;
    }

    // Método para establecer la posición de la bola
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public int getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(int newYSpeed) {
        this.ySpeed = newYSpeed;
    }

    // Método para reducir la velocidad de la bola
    public void reducirVelocidad() {
        // Reducimos la velocidad a la mitad pero aseguramos que no baje de la velocidad mínima
        xSpeed = (int)(xSpeed * 0.5);
        ySpeed = (int)(ySpeed * 0.5);

        if (Math.abs(xSpeed) < velocidadMinima) {
            xSpeed = velocidadMinima * (xSpeed < 0 ? -1 : 1); // Mantener la dirección
        }
        if (Math.abs(ySpeed) < velocidadMinima) {
            ySpeed = velocidadMinima * (ySpeed < 0 ? -1 : 1); // Mantener la dirección
        }

        System.out.println("Velocidad reducida: xSpeed=" + xSpeed + ", ySpeed=" + ySpeed);
    }

    public void restaurarVelocidad() {
        xSpeed = velocidadOriginalX;
        ySpeed = velocidadOriginalY;
        System.out.println("Velocidad restaurada: xSpeed=" + xSpeed + ", ySpeed=" + ySpeed);
    }


    // Método para dibujar la bola en pantalla
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, size);
    }
    public boolean isActive() {
        return active;
    }

    // Method to set the ball's active state
    public void setActive(boolean active) {
        this.active = active;
    }

    // Actualiza la posición de la bola en función de su velocidad
    public void update() {
        if (estaQuieto) return;

        x += xSpeed;
        y += ySpeed;

        // Rebotar si toca los bordes de la pantalla
        if (x - size < 0 || x + size > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }
        if (y + size > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    // Verifica colisiones con la paleta
    public void checkCollision(Paddle paddle) {
        if (collidesWith(paddle)) {
            color = Color.GREEN;
            ySpeed = -ySpeed;
        } else {
            color = Color.valueOf("0f380f");
        }
    }

    // Verifica colisiones con un bloque
    public void checkCollision(Block block) {
        if (collidesWith(block)) {
            ySpeed = -ySpeed;
            if (block instanceof Damageable) {
                Damageable damageableBlock = (Damageable) block;
                damageableBlock.takeDamage(1);  // Aplicar 1 punto de daño al bloque
            }
        }
    }

    // Método privado que verifica si la bola colisiona con un objeto del tipo Block
    private boolean collidesWith(Block block) {
        boolean intersectaX = (block.getX() + block.getWidth() >= x - size) &&
            (block.getX() <= x + size);
        boolean intersectaY = (block.getY() + block.getHeight() >= y - size) &&
            (block.getY() <= y + size);
        return intersectaX && intersectaY;
    }

    // Método privado que verifica si la bola colisiona con la paleta
    private boolean collidesWith(Paddle paddle) {
        boolean intersectaX = (paddle.getX() + paddle.getWidth() >= x - size) &&
            (paddle.getX() <= x + size);
        boolean intersectaY = (paddle.getY() + paddle.getHeight() >= y - size) &&
            (paddle.getY() <= y + size);
        return intersectaX && intersectaY;
    }
}

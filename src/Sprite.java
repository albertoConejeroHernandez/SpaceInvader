import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author AlbertoConejeroHernandez Clase Sprite. Representa un elemento
 *         pintable y colisionable del juego.
 */
public class Sprite {

	private BufferedImage buffer;
	private Color color = Color.BLUE;
	// Variables de dimensión
	private int ancho;
	private int alto;
	// Variables de colocación
	private int posX;
	private int posY;
	// Variables para la velocidad
	private int velocidadX;
	private int velocidadY;
	// ruta imagen SpriteAsteriod
	private String rutaImagen;
	private BufferedImage imagenSpriteMarciano;
	private Image imagenSpriteMarcianoReescalado;

	/**
	 * Constructor simple para un Sprite sin imagen y sin velocidad.
	 * 
	 * @param ancho Ancho que ocupa el Sprite (en pixels)
	 * @param alto  Altura que ocupa el Sprite (en pixels)
	 * @param posX  posición horizontal del sprite en el mundo.
	 * @param posY  posición vertical del Sprite en el mundo. El origen se sitúa
	 *              en la parte superior.
	 */
	public Sprite(int ancho, int alto, int posX, int posY, String rutaImagen) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.rutaImagen = rutaImagen;
		actualizarBuffer();
	}

	/**
	 * Constructor para un Sprite sin imagen.
	 * 
	 * @param ancho      Ancho que ocupa el Sprite (en pixels)
	 * @param alto       Altura que ocupa el Sprite (en pixels)
	 * @param posX       posición horizontal del sprite en el mundo.
	 * @param posY       posición vertical del Sprite en el mundo. El origen se
	 *                   sitúa en la parte superior.
	 * @param velocidadX velocidad horizontal del Sprite.
	 * @param velocidadY velocidad vertical del Sprite.
	 */
	public Sprite(int ancho, int alto, int posX, int posY, int velocidadX, int velocidadY, String rutaImagen) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.velocidadX = velocidadX;
		this.velocidadY = velocidadY;
		this.rutaImagen = rutaImagen;
		actualizarBuffer();
	}

	/**
	 * Método para actualizar el buffer que guarda cada Sprite. Por ahora sólo
	 * guarda un bufferedImage que está completamente relleno de un color.
	 */
	public void actualizarBuffer() {
		buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		try {
			imagenSpriteMarciano = ImageIO.read(new File(rutaImagen));
			imagenSpriteMarcianoReescalado = imagenSpriteMarciano.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
			g.drawImage(imagenSpriteMarcianoReescalado, 0, 0, null);
		} catch (Exception e) {
			g.setColor(color);
			g.fillRect(0, 0, ancho, alto);
			g.dispose();
		}

	}

	/**
	 * Método para mover el Sprite por el mundo.
	 * 
	 * @param anchoMundo ancho del mundo sobre el que se mueve el Sprite
	 * @param altoMundo  alto del mundo sobre el que se mueve el Sprite
	 */
	public void moverSprite(int anchoMundo, int altoMundo) {
		if (posX >= (anchoMundo-(anchoMundo/100*16)) - ancho) { // por la derecha
			velocidadX -= 5;
			posY = posY+3;
		}
		if (posX <=(anchoMundo-(anchoMundo/100*84)) - ancho) {// por la izquierda
			velocidadX += 5;
			posY = posY+3;
		}

		posX = posX + velocidadX;
		posY = posY + velocidadY;
	}

	/**
	 * Método que pinta el Sprite en el mundo teniendo en cuenta las
	 * características propias del Sprite.
	 * 
	 * @param g Es el Graphics del mundo que se utilizará para pintar el Sprite.
	 */
	public void pintarSpriteEnMundo(Graphics g) {
		g.drawImage(buffer, posX, posY, null);
	}

	// Métodos para obtener:
	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public BufferedImage getBuffer() {
		return buffer;
	}

	public int getVelocidadX() {
		return velocidadX;
	}

	public int getVelocidadY() {
		return velocidadY;
	}

	// métodos para cambiar:
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setBuffer(BufferedImage buffer) {
		this.buffer = buffer;
	}

	public void setVelocidadX(int velocidadX) {
		this.velocidadX = velocidadX;
	}

	public void setAceleracionY(int velocidadY) {
		this.velocidadY = velocidadY;
	}

}

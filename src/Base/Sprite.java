package Base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

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
	private Color color = Color.RED;
	// Variables de dimensi贸n
	private int ancho;
	private int alto;
	// Variables de colocaci贸n
	private int posX;
	private int posY;
	// Variables para la velocidad
	private int velocidadX;
	private int velocidadY;
	// ruta imagen SpriteAsteriod
	private String rutaImagen;
	private BufferedImage imagenSprite;
	private Image imagenSpriteReescalado;
	// contador de rebotes del sprite
	int contadorDisparo = 0;
	// Vida de la nave
	int vida;
	// Puntuacion de los marcianitos
	int puntuacion;

	/**
	 * Constructor simple para un Sprite sin imagen y sin velocidad.
	 * 
	 * @param ancho
	 *            Ancho que ocupa el Sprite (en pixels)
	 * @param alto
	 *            Altura que ocupa el Sprite (en pixels)
	 * @param posX
	 *            posici贸n horizontal del sprite en el mundo.
	 * @param posY
	 *            posici贸n vertical del Sprite en el mundo. El origen se sit煤a en
	 *            la parte superior.
	 */
	public Sprite(int ancho, int alto, int posX, int posY, String rutaImagen) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.rutaImagen = rutaImagen;
		actualizarBuffer();
	}

	public Image getImagenSpriteReescalado() {
		return imagenSpriteReescalado;
	}

	public void setImagenSpriteReescalado(Image imagenSpriteReescalado) {
		this.imagenSpriteReescalado = imagenSpriteReescalado;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntucion) {
		this.puntuacion = puntucion;
	}

	/**
	 * Constructor para un Sprite sin imagen.
	 * 
	 * @param ancho
	 *            Ancho que ocupa el Sprite (en pixels)
	 * @param alto
	 *            Altura que ocupa el Sprite (en pixels)
	 * @param posX
	 *            posici贸n horizontal del sprite en el mundo.
	 * @param posY
	 *            posici贸n vertical del Sprite en el mundo. El origen se sit煤a en
	 *            la parte superior.
	 * @param velocidadX
	 *            velocidad horizontal del Sprite.
	 * @param velocidadY
	 *            velocidad vertical del Sprite.
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

	public Sprite(int ancho, int alto, int posX, int posY, int velocidadX, int velocidadY) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.velocidadX = velocidadX;
		this.velocidadY = velocidadY;
	}

	public Sprite(int ancho, int alto, int posX, int posY, String rutaImagen, int contadorDisparos) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.rutaImagen = rutaImagen;
		this.contadorDisparo = contadorDisparos;
		actualizarBuffer();
	}

	/**
	 * M茅todo para actualizar el buffer que guarda cada Sprite. Por ahora s贸lo
	 * guarda un bufferedImage que est谩 completamente relleno de un color.
	 */
	public void actualizarBuffer() {
		buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		try {
			imagenSprite = ImageIO.read(new File(rutaImagen));
			imagenSpriteReescalado = imagenSprite.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
			g.drawImage(imagenSpriteReescalado, 0, 0, null);
		} catch (Exception e) {
			g.setColor(color);
			g.fillRect(0, 0, ancho, alto);
			g.dispose();
		}

	}

	public void moverSprite(int anchoIzquierda, int anchoDerecha) {
		if (posX >= anchoDerecha - ancho) { // por la derecha
			velocidadX = -1 * Math.abs(velocidadX);
		}
		if (posX <= anchoIzquierda) {// por la izquierda
			velocidadX = Math.abs(velocidadX);
		}

		posX = posX + velocidadX;
		posY = posY + velocidadY;
	}

	public boolean colisiona(Sprite otro) {
		boolean colisionEjeX = false;
		// 緾u醠 m醩 a la izq??:
		if (posX > otro.posX) { // El de de la izquierda es el otro:
			if (otro.getPosX() + otro.getAncho() >= posX) {
				colisionEjeX = true;
			}
		} else { // si no yo soy el de la izq.
			if (posX + ancho >= otro.getPosX()) {
				colisionEjeX = true;
			}
		}
		// EJE Y
		boolean colisionEjeY = false;
		if (posY > otro.posY) { // El de de la izquierda es el otro:
			if (otro.getPosY() + otro.getAlto() >= posY) {
				colisionEjeY = true;
			}
		} else { // si no yo soy el de la izq.
			if (posY + alto >= otro.getPosY()) {
				colisionEjeY = true;
			}
		}

		return colisionEjeX && colisionEjeY;

	}

	/**
	 * M茅todo que pinta el Sprite en el mundo teniendo en cuenta las
	 * caracter铆sticas propias del Sprite.
	 * 
	 * @param g
	 *            Es el Graphics del mundo que se utilizar谩 para pintar el Sprite.
	 */
	public void pintarSpriteEnMundo(Graphics g) {
		g.drawImage(buffer, posX, posY, null);
	}

	// M茅todos para obtener:

	public int getAncho() {
		return ancho;
	}

	public int getContadorDisparo() {
		return contadorDisparo;
	}

	public void setContadorDisparo(int contadorDisparo) {
		this.contadorDisparo = contadorDisparo;
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

	// m茅todos para cambiar:
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

	public int getExtraVelX() {
		return velocidadX;
	}

	public void setExtraVelX(int extraVelX) {
		this.velocidadX = extraVelX;
	}

	

	public void moverSprite() {
		posX = posX + velocidadX;
		posY = posY + velocidadY;

	}

}

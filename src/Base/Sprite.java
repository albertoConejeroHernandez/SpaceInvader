package Base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author AlbertoConejeroHernandez Clase Sprite. Representa un elemento
 *         pintable y colisionable del juego.
 */
public class Sprite {

	static int velocidadXBloque;
	
	private BufferedImage buffer;
	private Color color = Color.BLUE;
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
	private BufferedImage imagenSpriteMarciano;
	private Image imagenSpriteMarcianoReescalado;
	// contador de rebotes del sprite
	static int contador = 0;
	
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

	/**
	 * M茅todo para actualizar el buffer que guarda cada Sprite. Por ahora s贸lo
	 * guarda un bufferedImage que est谩 completamente relleno de un color.
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
	 * M茅todo para mover el Sprite por el mundo.
	 * 
	 * @param anchoMundo
	 *            ancho del mundo sobre el que se mueve el Sprite
	 * @param altoMundo
	 *            alto del mundo sobre el que se mueve el Sprite
	 */
	public void moverSprite(int derecha, int izquierda, int abajo) {

		if (posX >= izquierda + ancho) {// por la derecha

			if (contador == 5) {
				posY = 1 + posY;
				contador = 0;
			}
			contador++;
			velocidadX -= 1;

		}
		if (posX <= derecha - ancho) {// por la izquierda
			velocidadX += 1;

		}

		if (posY >= abajo - alto) {
			posY = abajo;
		}

		posX = posX + velocidadX;
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

	public void moverSprite() {
		posX = posX + velocidadX;
		posY = posY + velocidadY;

	}


	public void moverSprite(int derecha, int izquierda, int abajo, ArrayList<Sprite> bloqueMarcianitos) {

			if (bloqueMarcianitos.get(0).getPosX() < izquierda+ancho ) {// por la derecha

//				if (contador == 10) {
//					posY = 10 + posY;
//					contador = 0;
//				}
//				contador++;
				velocidadX = Math.abs(velocidadX);
				

			} 
		
			
		
		if (bloqueMarcianitos.get(bloqueMarcianitos.size()-1).getPosX() >= derecha - ancho) {// por la izquierda
			velocidadX = -1*Math.abs(velocidadX);

		}

		if (posY >= abajo - alto) {
			for (int i = 0; i < bloqueMarcianitos.size(); i++) {
				bloqueMarcianitos.remove(i);
			}
		}
		posX = posX + velocidadX;
		
		
		
		
	}

}

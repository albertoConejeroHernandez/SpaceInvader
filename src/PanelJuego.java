import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author AlbertoConejeroHernandez Clase PanelJuego. Controla los gráficos del
 *         Juego. Por ahora también controla la lógica del Juego. Extiende de
 *         JPanel. Todos los gráficos se gestionan mediante los gráficos de un
 *         JPanel. Implementa Runnable porque en el constructor se lanza un hilo
 *         que permite actualizar el Juego periódicamente. Implementa
 *         MouseListener para que pueda capturar las pulsaciones del ratón.
 */
public class PanelJuego extends JPanel implements Runnable, MouseListener, ComponentListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int anchoSpriteMarcianito = 40;
	BufferedImage imagenFondo;
	Sprite nave;
	ArrayList<Sprite> marcianitosInvaders;
	Image imagenRescalada;
	String[] imagenesMarcianos = { "imagenesSpaceInvaders/Marcianos/M1.PNG", "imagenesSpaceInvaders/Marcianos/M2.PNG",
			"imagenesSpaceInvaders/Marcianos/M3.PNG", "imagenesSpaceInvaders/Marcianos/M4.PNG",
			"imagenesSpaceInvaders/Marcianos/M5.PNG", "imagenesSpaceInvaders/Marcianos/M6.PNG" };
	int alto = VentanaPrincipal.altoLienzo;
	int ancho = VentanaPrincipal.anchoLienzo;

	/**
	 * Constructor de PanelJuego. - Inicializa el arrayList de cuadrados. - Asigna
	 * el mouse listener que implementa la propia clase para lanazar nuevos
	 * cuadrados. - Inicia un hilo para actualizar el juego periódicamente.
	 */
	public PanelJuego() {
		marcianitosInvaders = new ArrayList<Sprite>();
		// MouseListener:
		this.addMouseListener(this);
		this.addComponentListener(this);
		this.addMouseMotionListener(this);
		// Lanzo el hilo.
		new Thread(this).start();
		try {
			imagenFondo = ImageIO.read(new File("imagenesSpaceInvaders/FondoJuego.PNG"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		nave = new Sprite(40, 40, 800, 600, "imagenesSpaceInvaders/naves/Nave1.PNG");
		int posX = 0;
		int posY = 0;
		int anchoMundo = ancho / 10;
		int altoMundo = alto / 15;
		for (int i = 0; i < 12; i++) {

			posY += altoMundo;

			Sprite creador;
			int rd = new Random().nextInt(6);
			posX += anchoMundo;

			creador = new Sprite(anchoSpriteMarcianito, anchoSpriteMarcianito, posX, posY, imagenesMarcianos[rd]);
			marcianitosInvaders.add(creador);

		}
	}

	/**
	 * Sobreescritura del método paintComponent. Este método se llama
	 * automáticamente cuando se inicia el componente, se redimensiona o bien
	 * cuando se llama al método "repaint()". Nunca llamarlo directamente.
	 * 
	 * @param g Es un Graphics que nos proveé JPanel para poner pintar el
	 *          componente a nuestro antojo.
	 */
	@Override
	public void paintComponent(Graphics g) {
		rellenarFondo(g);
		// Pintamos los cuadrados:
		int contador = 0;
		for (Sprite marcianito : marcianitosInvaders) {
			contador++;
			marcianito.pintarSpriteEnMundo(g);
			System.out.println(contador);
		}
		// pintamos nave
		nave.pintarSpriteEnMundo(g);

	}

	/**
	 * Método que se utiliza para rellenar el fondo del JPanel.
	 * 
	 * @param g Es el gráficos sobre el que vamos a pintar el fondo.
	 */
	private void rellenarFondo(Graphics g) {

		g.drawImage(imagenRescalada, 0, 0, null);

	}

	/**
	 * Método para mover todos los Sprites del juego.
	 */
	private void moverSprites() {
		int altoPantallaJuego, anchoPantallaJuego;

		for (int i = 0; i < marcianitosInvaders.size(); i++) {
			Sprite aux = marcianitosInvaders.get(i);
			aux.moverSprite(getWidth(), getHeight());
		}
	}

	@Override
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Movemos todos los cuadrados:
			moverSprites();

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		//Creamos un sprite Random cuando se pulsa el botón:
//		if(SwingUtilities.isLeftMouseButton(e)){
//			Random rd = new Random();
//			Sprite creador;
//			int posX = e.getX()-(anchoCuadrado/2);
//			int poxY = e.getY()-(anchoCuadrado/2);
//			int velocidadX = rd.nextInt(31)-15;
//			int velocidadY = rd.nextInt(31)-15;
//			
//			creador = new Sprite(anchoCuadrado, anchoCuadrado, posX, poxY, velocidadX, velocidadY, "imagenes/asteroide.png");
//			cuadrados.add(creador);
//		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void rescalarImagen() {
		imagenRescalada = imagenFondo.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		rescalarImagen();

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		nave.setPosX(e.getX() - (anchoSpriteMarcianito / 2));

	}

}
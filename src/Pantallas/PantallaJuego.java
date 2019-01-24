package Pantallas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.plaf.SplitPaneUI;

import Base.PanelJuego;
import Base.Pantalla;
import Base.Sprite;

public class PantallaJuego implements Pantalla {

	PanelJuego panelJuego;

	private static final int anchoSprite = 40;
	BufferedImage imagenFondo;
	BufferedImage trasFondo;
	Sprite nave;
	Sprite disparo;
	ArrayList<Sprite> proteccion;
	ArrayList<Sprite> bloqueMarcianitos;
	Image imagenRescalada;
	Image imagenRescaladaTrasFondo;
	int contador = 0;
	// ImagenesSprite
	String[] proteccionesImagenes = { "imagenesSpaceInvaders/Protecciones/ProteccionCompleta.PNG",
			"imagenesSpaceInvaders/Protecciones/ProteccionPocoDañada.PNG",
			"imagenesSpaceInvaders/Protecciones/ProteccionDañada1.PNG",
			"imagenesSpaceInvaders/Protecciones/ProteccionMinima.PNG" };
	String[] parejasMarcianosFR = { "imagenesSpaceInvaders/Marcianos/M1.PNG",
			"imagenesSpaceInvaders/Marcianos/M2.PNG" };
	String[] parejasMarcianosSR = { "imagenesSpaceInvaders/Marcianos/M4.PNG",
			"imagenesSpaceInvaders/Marcianos/M3.PNG", };
	String[] parejasMarcianosTR = { "imagenesSpaceInvaders/Marcianos/M5.PNG",
			"imagenesSpaceInvaders/Marcianos/M6.PNG" };
	String bossMarcianos = "imagenesSpaceInvaders/Marcianos/MBoss.PNG";
	// Limites de la pantalla de juego
	int izquierda;
	int derecha;
	int arriba;
	int abajo;
	// tamañp de la paantalla de juego
	int anchoPanelJuego;
	int altoPanelJuego;

	public PantallaJuego(PanelJuego panelJuego) {
		super();
		this.panelJuego = panelJuego;
		derecha = panelJuego.getWidth() / 100 * 80;
		izquierda = panelJuego.getWidth() / 100 * 20;
		arriba = panelJuego.getHeight() / 100 * 20;
		abajo = panelJuego.getHeight() / 100 * 80;
		anchoPanelJuego = derecha - izquierda;
		altoPanelJuego = abajo - arriba;
		System.out.println("Limite derecho: " + derecha);
		System.out.println("Limite Izquierdo: " + izquierda);
		System.out.println("Ancho del mapa: " + anchoPanelJuego);
		System.out.println("Alto del mapa: " + altoPanelJuego);
	}

	@Override
	public void inicializarPantalla() {
		bloqueMarcianitos = new ArrayList<>();
		proteccion = new ArrayList<>();
		try {
			imagenFondo = ImageIO.read(new File("imagenesSpaceInvaders/FondoJuegoBueno.PNG"));
			trasFondo = ImageIO.read(new File("imagenesSpaceInvaders/fondoMapa.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		nave = new Sprite(40, 40, altoPanelJuego, abajo, "ImagenesSpaceInvaders/naves/Nave1.PNG");
		int posX = 0;
		int posY = 0;
		String aux = "";

		for (int i = 0; i < 6; i++) {
			posX = (i * 85 + 1) + izquierda;
			for (int j = 0; j < 4; j++) {
				posY = (j * 70 + 1) + arriba;
				switch (j) {
				case 0:
					aux = bossMarcianos;
					break;
				case 1:
					aux = parejasMarcianosFR[0];
					break;
				case 2:
					aux = parejasMarcianosSR[0];
					break;
				case 3:
					aux = parejasMarcianosTR[0];
					break;

				default:
					break;
				}

				Sprite creador = new Sprite(anchoSprite, anchoSprite, posX, posY, 4, 0, aux);

				bloqueMarcianitos.add(creador);
			}
		}
		for (int i = 0; i < 4; i++) {
			Sprite protecciones = new Sprite(anchoSprite * 2, anchoSprite, ((anchoPanelJuego / 4) * i + 250), 650,
					proteccionesImagenes[0], 0);
			proteccion.add(protecciones);

		}
		rescalarImagen();

		rescalarImagenFondo();

	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenRescaladaTrasFondo, izquierda - 45, arriba - 45, null);
		// Pintamos los marcianitos:

		for (int i = 0; i < bloqueMarcianitos.size(); i++) {

			bloqueMarcianitos.get(i).pintarSpriteEnMundo(g);

		}
		if (disparo != null) {
			disparo.pintarSpriteEnMundo(g);
		}
		// Pintamos los bloques de proteccion
		for (int i = 0; i < proteccion.size(); i++) {
			proteccion.get(i).pintarSpriteEnMundo(g);
		}
		// pintamos nave
		if (nave.getPosX() <= izquierda - nave.getAncho()) {
			nave.setPosX(izquierda);
		} else {
			nave.pintarSpriteEnMundo(g);
		}
		if (nave.getPosX() >= derecha - nave.getAncho()) {
			nave.setPosX(derecha);
		} else {
			nave.pintarSpriteEnMundo(g);
		}

		rellenarFondo(g);

	}

	private void rellenarFondo(Graphics g) {
		g.drawImage(imagenRescalada, 0, 0, null);

	}

	@Override
	public void ejecutarFrame() {
		comprobarColision();

		comprobarColisionNave();
		moverSprites();

	}

	private void comprobarColisionNave() {
		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			if (bloqueMarcianitos.get(i).colisiona(nave)) {
				bloqueMarcianitos.remove(i);

				PantallaPerder pantallaMuerte = new PantallaPerder(panelJuego);
				pantallaMuerte.inicializarPantalla();
				panelJuego.setPantallaActual(pantallaMuerte);
			}
		}

	}

	private void moverSprites() {

		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			bloqueMarcianitos.get(i).moverSprite(derecha, izquierda, abajo, bloqueMarcianitos);
		}
		if (disparo != null) {
			disparo.moverSprite();
			if (disparo.getPosY() + disparo.getAlto() <= arriba) {
				disparo = null;
			}
		}
	}

	private void comprobarColision() {
		// Compruebo las colisiones del disparo y la nave con los marcianos
		for (int i = 0; i < bloqueMarcianitos.size() && disparo != null; i++) {

			if (bloqueMarcianitos.get(i).colisiona(disparo)) {
				disparo = null;
				bloqueMarcianitos.remove(i);
				if (bloqueMarcianitos.size() == 0) {
					PantallaGanador pantallaGanador = new PantallaGanador(panelJuego);
					pantallaGanador.inicializarPantalla();
					panelJuego.setPantallaActual(pantallaGanador);
				}
			}

		}
		for (int i = 0; i < proteccion.size() && disparo != null; i++) {

			if (proteccion.get(i).colisiona(disparo)) {
				disparo = null;

				switch (proteccion.get(i).getContadorDisparo()) {
				case 0:
					try {
						proteccion.get(i).actualizarBuffer();
						proteccion.get(i).setBuffer(ImageIO.read(new File(proteccionesImagenes[1])));

					} catch (IOException e) {

					}
					proteccion.get(i).setContadorDisparo(1);
					break;
				case 1:
					try {
						proteccion.get(i).actualizarBuffer();
						proteccion.get(i).setBuffer(ImageIO.read(new File(proteccionesImagenes[2])));

					} catch (IOException e) {

					}
					proteccion.get(i).setContadorDisparo(2);
					break;
				case 2:
					try {
						proteccion.get(i).actualizarBuffer();
						proteccion.get(i).setBuffer(ImageIO.read(new File(proteccionesImagenes[3])));

					} catch (IOException e) {

					}
					proteccion.get(i).setContadorDisparo(3);
					break;
				case 3:
					proteccion.remove(i);
					break;
				default:
					break;
				}

			}

		}

	}

	@Override
	public void moverRaton(MouseEvent e) {
		if (nave.getPosX() <= izquierda - nave.getAncho()) {
			nave.setPosX(izquierda);
		} else {
			nave.setPosX(e.getX() - (anchoSprite / 2));
		}
		if (nave.getPosX() >= derecha - nave.getAncho()) {
			nave.setPosX(derecha);
		} else {
			nave.setPosX(e.getX() - (anchoSprite / 2));
		}

	}

	@Override
	public void pulsarRaton(MouseEvent e) {

		if (disparo == null) {
			disparo = new Sprite(nave.getAncho() / 4, nave.getAlto(), nave.getPosX() + nave.getAncho() / 4,
					nave.getPosY(), 0, -35, "ImagenesSpaceInvaders/Disparos/disparoNaveRecto.PNG");

		}

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		rescalarImagenFondo();
	}

	private void rescalarImagen() {
		imagenRescaladaTrasFondo = trasFondo.getScaledInstance(anchoPanelJuego + 100, altoPanelJuego + 100,
				Image.SCALE_SMOOTH);
	}

	private void rescalarImagenFondo() {
		imagenRescalada = imagenFondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);
	}
}
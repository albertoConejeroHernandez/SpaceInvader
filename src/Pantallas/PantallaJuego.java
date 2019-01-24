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
	Sprite[] proteccion;
	ArrayList<Sprite> bloqueMarcianitos;
	Image imagenRescalada;
	Image imagenRescaladaTrasFondo;
	// m2-m1
	// m5-m6
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
		proteccion = new Sprite[4];
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
		int rd;
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

				Sprite creador = new Sprite(anchoSprite, anchoSprite, posX, posY, 7, 0, aux);

				bloqueMarcianitos.add(creador);
			}
		}
		for (int i = 0; i < proteccion.length; i++) {
			proteccion[i] = new Sprite(anchoSprite * 2, anchoSprite * 2, (i * anchoPanelJuego / 4) / 2 + izquierda,
					abajo + 150, "ImagenesSpaceInvaders/Protecciones/ProteccionCompleta.PNG");
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
		for (int i = 0; i < proteccion.length; i++) {
			proteccion[i].pintarSpriteEnMundo(g);
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
//		System.out.println("Posicion antes de mover el sprite: " + bloqueMarcianitos.get(0).getPosX());
//		System.out.println("Posicion antes de mover el sprite: " + bloqueMarcianitos.get(1).getPosX());
		comprobarColisionNave();
		moverSprites();
//		System.out.println("Posicion despues de mover el sprite: " + bloqueMarcianitos.get(0).getPosX());
//		System.out.println("Posicion despues de mover el sprite: " + bloqueMarcianitos.get(1).getPosX());

	}

	private void comprobarColisionNave() {
		for (int i = 0; i < bloqueMarcianitos.size() ; i++) {
			if (bloqueMarcianitos.get(i).colisiona(nave)) {
				nave= null;
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
				bloqueMarcianitos.get(i)
						.actualizarImagen("ImagenesSpaceInvaders/MuerteExplosiones/explosionMarciano.PNG");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bloqueMarcianitos.remove(i);
				if (bloqueMarcianitos.size() == 0) {
					PantallaPerder pantallaGanador = new PantallaPerder(panelJuego);
					pantallaGanador.inicializarPantalla();
					panelJuego.setPantallaActual(pantallaGanador);
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
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
	static final int aceleracion = 3;

	// Sprites y funcionalidad;
	Sprite nave;
	Sprite disparo;
	Sprite disparoMarciano;
	Sprite liderMarciano;
	ArrayList<Sprite> proteccion;
	ArrayList<Sprite> bloqueMarcianitos;
	int contadorDisparoMarcianos = 0;

	// Imagenes fondo y trasFondo
	Image imagenRescalada;
	Image imagenRescaladaTrasFondo;
	BufferedImage imagenFondo;
	BufferedImage trasFondo;

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
		liderMarciano = new Sprite(anchoSprite, anchoSprite, 85 + izquierda, 70 + arriba, 0, 0);
		generarMarcianitos();
		generarProtecciones();
		rescalarImagen();

		rescalarImagenFondo();

	}

	private void generarProtecciones() {
		for (int i = 0; i < 4; i++) {
			Sprite protecciones = new Sprite(anchoSprite * 2, anchoSprite, ((anchoPanelJuego / 4) * i + 250), 650,
					proteccionesImagenes[0], 0);
			proteccion.add(protecciones);

		}

	}

	public void generarMarcianitos() {
		String aux = "";
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
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
				}
				Sprite creador = new Sprite(anchoSprite, anchoSprite, (80*i)+1+izquierda,
						liderMarciano.getPosY()*j , aux);

				bloqueMarcianitos.add(creador);
			}
		}
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenRescaladaTrasFondo, izquierda - 45, arriba - 45, null);
		// Pintamos los marcianitos:
		liderMarciano.pintarSpriteEnMundo(g);
		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			bloqueMarcianitos.get(i).pintarSpriteEnMundo(g);

		}
		if (disparo != null) {
			disparo.pintarSpriteEnMundo(g);
		}
		if (disparoMarciano != null) {
			disparoMarciano.pintarSpriteEnMundo(g);
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
		disparoMarcianito();
	}

	private void disparoMarcianito() {
		if (bloqueMarcianitos.size() > 0) {
			int rd = new Random().nextInt(bloqueMarcianitos.size());
			if (contadorDisparoMarcianos == 50 && disparoMarciano == null) {
				disparoMarciano = new Sprite(bloqueMarcianitos.get(rd).getAncho() / 4,
						bloqueMarcianitos.get(rd).getAlto(),
						bloqueMarcianitos.get(rd).getPosX() + bloqueMarcianitos.get(rd).getAncho() / 4,
						bloqueMarcianitos.get(rd).getPosY(), 0, +15,
						"ImagenesSpaceInvaders/Disparos/disparoMarcianitos.PNG");
				contadorDisparoMarcianos = 0;
			}
			contadorDisparoMarcianos++;
		}
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

		liderMarciano.moverSprite(derecha, izquierda);
//		for (Sprite sprite : bloqueMarcianitos) {
//			sprite.setPosX(liderMarciano.getPosX());
//		}

		if (disparo != null) {
			disparo.moverSprite();
			if (disparo.getPosY() + disparo.getAlto() <= arriba) {
				disparo = null;
			}
		}
		if (disparoMarciano != null) {
			disparoMarciano.moverSprite();
			if (disparoMarciano.getPosY() + disparoMarciano.getAlto() > abajo) {
				disparoMarciano = null;
			}
		}
	}

	private void comprobarColision() {

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

		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			for (int j = 0; j < proteccion.size(); j++) {
				if (bloqueMarcianitos.get(i).colisiona(proteccion.get(j))) {
					proteccion.remove(j);
				}
			}
		}
		for (int i = 0; i < proteccion.size() && disparo != null; i++) {

			if (proteccion.get(i).colisiona(disparo)) {
				disparo = null;
				proteccion.get(i).setContadorDisparo(proteccion.get(i).getContadorDisparo() + 1);
				switch (proteccion.get(i).getContadorDisparo()) {
				case 3:
					proteccion.get(i).setContadorDisparo(0);
					proteccion.remove(i);
					break;
				}
			}
		}
		for (int i = 0; i < proteccion.size() && disparoMarciano != null; i++) {

			if (proteccion.get(i).colisiona(disparoMarciano)) {
				disparoMarciano = null;
				proteccion.get(i).setContadorDisparo(proteccion.get(i).getContadorDisparo() + 1);
				switch (proteccion.get(i).getContadorDisparo()) {
				case 3:
					proteccion.get(i).setContadorDisparo(0);
					proteccion.remove(i);
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
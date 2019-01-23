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

	private static final int anchoSpriteMarcianito = 40;
	BufferedImage imagenFondo;
	BufferedImage trasFondo;
	Sprite nave;
	Sprite disparo;
	ArrayList<Sprite> bloqueMarcianitos;
	Image imagenRescalada;
	String[] imagenesMarcianos = { "imagenesSpaceInvaders/Marcianos/M1.PNG", "imagenesSpaceInvaders/Marcianos/M2.PNG",
			"imagenesSpaceInvaders/Marcianos/M4.PNG", "imagenesSpaceInvaders/Marcianos/M5.PNG",
			"imagenesSpaceInvaders/Marcianos/M6.PNG", "imagenesSpaceInvaders/Marcianos/MBoss.PNG" };
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

		try {
			imagenFondo = ImageIO.read(new File("imagenesSpaceInvaders/FondoJuegoBueno.PNG"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		nave = new Sprite(40, 40, altoPanelJuego, abajo, "imagenesSpaceInvaders/naves/Nave1.PNG");
		int posX = 0;
		int posY = 0;

		int rd;
		for (int i = 0; i < 6; i++) {

			posX = (i * 70 + 1) + arriba;
			for (int j = 0; j < 4; j++) {

				Sprite creador;
				if (j == 0) {
					rd = 5;
				} else {
					rd = j;
				}

				posY = (j * 70 + 1) + izquierda;
				creador = new Sprite(anchoSpriteMarcianito, anchoSpriteMarcianito, posX, posY, 3, 0,
						imagenesMarcianos[rd]);

				bloqueMarcianitos.add(creador);
			}
		}

		rescalarImagen();

	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, panelJuego.getWidth(), panelJuego.getHeight());

		// Pintamos los marcianitos:

		for (int i = 0; i < bloqueMarcianitos.size(); i++) {

			bloqueMarcianitos.get(i).pintarSpriteEnMundo(g);

		}
		if (disparo != null) {
			disparo.pintarSpriteEnMundo(g);
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
		comprobarPosicion();
		moverSprites();
		

	}

	private void comprobarPosicion() {

		if (bloqueMarcianitos.get(0).getPosX() <= izquierda + bloqueMarcianitos.get(0).getAncho()) {
			for (Sprite sprite : bloqueMarcianitos) {
				sprite.setPosY(sprite.getPosY() + 10);
			}
		} else {
			for (Sprite sprite : bloqueMarcianitos) {
				sprite.setPosY(sprite.getPosY());
				
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
		// Compruebo las colisiones del disparo y el asteroid
		for (int i = 0; i < bloqueMarcianitos.size() && disparo != null; i++) {

			if (bloqueMarcianitos.get(i).colisiona(disparo)) {
				disparo = null;
				bloqueMarcianitos.remove(i);

			}

		}

	}

	@Override
	public void moverRaton(MouseEvent e) {
		if (nave.getPosX() <= izquierda - nave.getAncho()) {
			nave.setPosX(izquierda);
		} else {
			nave.setPosX(e.getX() - (anchoSpriteMarcianito / 2));
		}
		if (nave.getPosX() >= derecha - nave.getAncho()) {
			nave.setPosX(derecha);
		} else {
			nave.setPosX(e.getX() - (anchoSpriteMarcianito / 2));
		}

	}

	@Override
	public void pulsarRaton(MouseEvent e) {

		if (disparo == null) {
			disparo = new Sprite(nave.getAncho() / 4, nave.getAlto(), nave.getPosX() + nave.getAncho() / 4,
					nave.getPosY(), 0, -15, "ImagenesSpaceInvaders/Disparos/disparoMarcianito.PNG");

		}

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		rescalarImagen();
	}

	private void rescalarImagen() {
		imagenRescalada = imagenFondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);
	}
}
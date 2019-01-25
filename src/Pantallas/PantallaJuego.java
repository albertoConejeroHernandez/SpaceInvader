package Pantallas;

import java.awt.Color;
import java.awt.Font;
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
	// Vidas nave
	int contadorVidas;
	ArrayList<Sprite> vidasNave;
	// Puntuacion jugador
	int puntuacion;
	Color colorLetra;
	Font fuenteIncial;
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
	String[] parejasMarcianos = { "imagenesSpaceInvaders/Marcianos/M1.PNG", "imagenesSpaceInvaders/Marcianos/M2.PNG",
			"imagenesSpaceInvaders/Marcianos/M4.PNG", "imagenesSpaceInvaders/Marcianos/M3.PNG",
			"imagenesSpaceInvaders/Marcianos/M5.PNG", "imagenesSpaceInvaders/Marcianos/M6.PNG" };

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
		System.out.println("limite Arriba: " + arriba);
		System.out.println("limite abajo:" + abajo);
		System.out.println("Ancho del mapa: " + anchoPanelJuego);
		System.out.println("Alto del mapa: " + altoPanelJuego);
	}

	@Override
	public void inicializarPantalla() {
		bloqueMarcianitos = new ArrayList<>();
		proteccion = new ArrayList<>();
		vidasNave = new ArrayList<>();
		try {
			imagenFondo = ImageIO.read(new File("imagenesSpaceInvaders/FondoJuegoBueno.PNG"));
			trasFondo = ImageIO.read(new File("imagenesSpaceInvaders/fondoMapa.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		inicializarNave();
		inicializarVidasNave();
		liderMarciano = new Sprite(anchoSprite, anchoSprite, 1 + izquierda, 1 + arriba, 3, 0);
		generarMarcianitos();
		generarProtecciones();
		puntuacion();
		rescalarImagen();
		rescalarImagenFondo();

	}

	private void puntuacion() {
		fuenteIncial = new Font("Arial", Font.BOLD, 40);
		colorLetra = Color.GREEN;
		puntuacion = 0;
	}

	private void inicializarVidasNave() {
		for (int i = 0; i < 3; i++) {
			Sprite vida = new Sprite(40, 40, (panelJuego.getWidth() - (anchoSprite * i) - 100),
					panelJuego.getHeight() / 100 * 4, "ImagenesSpaceInvaders/naves/Nave1.PNG");
			vidasNave.add(vida);
		}
		contadorVidas = vidasNave.size();

	}

	private void inicializarNave() {
		nave = new Sprite(40, 40, altoPanelJuego, abajo, "ImagenesSpaceInvaders/naves/Nave1.PNG");
		nave.setVida(3);
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
		int posXi = izquierda + 5;
		int posYi = arriba + 5;
		int separacion = anchoSprite * 2;
		int velX = 3;

		for (int i = 0; i < 24; i++) {
			aux = asignarImagen(i);
			Sprite creador = new Sprite(anchoSprite, anchoSprite, posXi + (separacion * (i % 6)),
					posYi + (separacion * (i / 6)), velX, 0, aux);
			asignarPuntuacion(i, creador);
			bloqueMarcianitos.add(creador);

		}
	}

	private String asignarImagen(int i) {
		int rd = new Random().nextInt(6);
		String aux = "";
		switch (i) {
		case 0:
			aux = bossMarcianos;
			break;
		case 1:
			aux = bossMarcianos;
			break;
		case 2:
			aux = bossMarcianos;
			break;
		case 3:
			aux = bossMarcianos;
			break;
		case 4:
			aux = bossMarcianos;
			break;
		case 5:
			aux = bossMarcianos;
			break;
		default:
			aux = parejasMarcianos[rd];
			break;
		}
		return aux;
	}

	private void asignarPuntuacion(int j, Sprite creador) {
		int rd = new Random().nextInt(10);
		switch (j) {
		case 0:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		case 1:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		case 2:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		case 3:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		case 4:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		case 5:
			creador.setPuntuacion(55);
			creador.setVida(2);
			break;
		default:
			creador.setPuntuacion(15 + rd);
			break;
		}

	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenRescaladaTrasFondo, izquierda - 45, arriba - 45, null);
		// Pintamos los marcianitos:

		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			bloqueMarcianitos.get(i).pintarSpriteEnMundo(g);

		}
		// pintamos el disparo de la nave
		if (disparo != null) {
			disparo.pintarSpriteEnMundo(g);
		}
		// Pintamos el disparo del marciano
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
		// Pintamos el marco del juego
		rellenarFondo(g);
		g.setFont(fuenteIncial);
		g.setColor(colorLetra);
		g.drawString("VIDAS: ", 688, panelJuego.getHeight() / 100 * 8);
		for (Sprite sprite : vidasNave) {
			sprite.pintarSpriteEnMundo(g);
		}
		g.setFont(fuenteIncial);
		g.setColor(colorLetra);
		g.drawString("Puntuacion: " + puntuacionPlayer(), panelJuego.getWidth() / 2 - 500,
				panelJuego.getHeight() / 100 * 10);

	}

	private int puntuacionPlayer() {
		return puntuacion;
	}

	private void rellenarFondo(Graphics g) {
		g.drawImage(imagenRescalada, 0, 0, null);

	}

	public int getMarcianoIzq() {
		int posIzquierda = 0;
		if (bloqueMarcianitos.size() != 0) {
			posIzquierda = bloqueMarcianitos.get(0).getPosX();
			for (Sprite sprite : bloqueMarcianitos) {
				if (sprite.getPosX() < posIzquierda) {
					posIzquierda = sprite.getPosX();
				}
			}

		}
		return posIzquierda;
	}

	public int getMarcianoDer() {
		int posderecha = 0;
		if (bloqueMarcianitos.size() != 0) {
			posderecha = bloqueMarcianitos.get(0).getPosX() + anchoSprite;
			for (Sprite sprite : bloqueMarcianitos) {
				if (sprite.getPosX() + anchoSprite > posderecha) {
					posderecha = sprite.getPosX() + anchoSprite;
				}
			}
		}
		return posderecha;
	}

	@Override
	public void ejecutarFrame() {

		comprobarColision();
		comprobarColisionNave();
		comprobarDisparoProteccion();
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
						"ImagenesSpaceInvaders/Disparos/disparoMarcianito.PNG");
				contadorDisparoMarcianos = 0;
			}
			contadorDisparoMarcianos++;
		}
	}

	private void comprobarColisionNave() {
		for (int i = 0; i < bloqueMarcianitos.size(); i++) {
			if (bloqueMarcianitos.get(i).colisiona(nave)) {
				bloqueMarcianitos.remove(i);

				PantallaPerder pantallaMuerte = new PantallaPerder(panelJuego, puntuacion);
				pantallaMuerte.inicializarPantalla();
				panelJuego.setPantallaActual(pantallaMuerte);
			}
		}

		if (disparoMarciano != null) {
			if (nave.colisiona(disparoMarciano)) {
				disparoMarciano = null;
				nave.setVida(nave.getVida() - 1);
				vidasNave.remove(contadorVidas - 1);
				contadorVidas--;
				if (nave.getVida() == 0) {
					PantallaPerder pantallaMuerte = new PantallaPerder(panelJuego, puntuacion);
					pantallaMuerte.inicializarPantalla();
					panelJuego.setPantallaActual(pantallaMuerte);
				}
			}
		}
	}

	private void moverSprites() {
		for (Sprite sprite : bloqueMarcianitos) {
			sprite.moverSprite(izquierda, derecha);
		}

		if (getMarcianoDer() >= derecha) {
			for (Sprite sprite : bloqueMarcianitos) {
				sprite.setVelocidadX(-1 * (Math.abs(3) + 1));
				sprite.setPosY(sprite.getPosY() + 5);
			}
		}
		if (getMarcianoIzq() <= izquierda) {
			for (Sprite sprite : bloqueMarcianitos) {
				sprite.setVelocidadX((Math.abs(3) + 1));
				sprite.setPosY(sprite.getPosY() + 5);
			}
		}
		if (disparo != null) {
			disparo.moverSprite();
			if (disparo.getPosY() + disparo.getAlto() <= arriba) {
				disparo = null;
			}
		}
		if (disparoMarciano != null) {
			disparoMarciano.moverSprite();
			if (disparoMarciano.getPosY() + disparoMarciano.getAlto() > panelJuego.getHeight() - 100) {
				disparoMarciano = null;
			}
		}
	}

	public void comprobarDisparoProteccion() {
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
		// separar en metodos
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

	private void comprobarColision() {

		for (int i = 0; i < bloqueMarcianitos.size() && disparo != null; i++) {
			if (bloqueMarcianitos.get(i).colisiona(disparo)) {
				disparo = null;
				if (bloqueMarcianitos.get(i).getVida() != 0) {
					bloqueMarcianitos.get(i).setVida(bloqueMarcianitos.get(i).getVida() - 1);
				} else {
					puntuacion += bloqueMarcianitos.get(i).getPuntuacion();
					bloqueMarcianitos.remove(i);

				}
				if (bloqueMarcianitos.size() == 0) {
					PantallaGanador pantallaGanador = new PantallaGanador(panelJuego, puntuacion);
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
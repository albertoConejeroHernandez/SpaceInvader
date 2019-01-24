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

import javax.imageio.ImageIO;

import Base.PanelJuego;
import Base.Pantalla;
import Base.Sprite;
import Pantallas.PantallaJuego;

public class PantallaGanador implements Pantalla {
	PanelJuego panelJuego;

	// Variables para trabajar la imagen
	BufferedImage imagenFondo;
	Image imagenRescalada;

	// Variables para trabajar el color de letras con parpadeos
	Color colorLetra;
	Font fuenteIncial;
	int contadorColorFrames = 0;
	static final int cambioColorInicio = 10;

	// Variables para usar "Botones" de jugar de nuevo o cerrar
	Sprite botonJugarDeNuevo;
	Sprite botonSalir;

	public PantallaGanador(PanelJuego panelJuego) {
		super();
		this.panelJuego = panelJuego;
	}

	@Override
	public void inicializarPantalla() {
		try {
			imagenFondo = ImageIO.read(new File("imagenes/galaxia.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		botonJugarDeNuevo = new Sprite(400, 50, panelJuego.getWidth() / 2 - 450, panelJuego.getHeight() / 2 + 370,
				null);
		botonSalir = new Sprite(200, 50, panelJuego.getWidth() / 2 + 200, panelJuego.getHeight() / 2 + 370, null);
		fuenteIncial = new Font("Arial", Font.BOLD, 20);
		colorLetra = Color.YELLOW;
		reescalarImagen();
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenRescalada, 0, 0, null);
		g.setFont(fuenteIncial);
		g.setColor(colorLetra);
		botonJugarDeNuevo.pintarSpriteEnMundo(g);
		botonSalir.pintarSpriteEnMundo(g);
		g.drawString("Cerrar", panelJuego.getWidth() / 2 + 250, panelJuego.getHeight() / 2 + 400);
		g.drawString("Pulsa para Jugar de nuevo", panelJuego.getWidth() / 2 - 400, panelJuego.getHeight() / 2 + 400);

	}

	@Override
	public void ejecutarFrame() {
		contadorColorFrames++;
		if (contadorColorFrames % cambioColorInicio == 0) {
			if (colorLetra == Color.RED) {
				colorLetra = Color.yellow;

			} else {
				if (colorLetra == Color.yellow) {
					colorLetra = Color.RED;
				}
			}
		}

	}

	@Override
	public void moverRaton(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		if (e.getX() == botonJugarDeNuevo.getPosX() && e.getY() == botonJugarDeNuevo.getPosY()) {
			PantallaJuego pantallaJuego = new PantallaJuego(panelJuego);
			pantallaJuego.inicializarPantalla();
			panelJuego.setPantallaActual(pantallaJuego);
		} else {
			if (e.getX() == botonSalir.getPosX() && e.getY() == botonSalir.getPosY()) {
				System.exit(1);
			}

		}

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		reescalarImagen();

	}

	private void reescalarImagen() {
		imagenRescalada = imagenFondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);
	}

}

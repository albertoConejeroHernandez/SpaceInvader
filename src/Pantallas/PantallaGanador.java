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


	//Variable para mostrar la puntuacion
	int puntuacion;
	public PantallaGanador(PanelJuego panelJuego, int puntuacion) {
		super();
		this.panelJuego = panelJuego;
		this.puntuacion = puntuacion;
	}

	@Override
	public void inicializarPantalla() {
		try {
			imagenFondo = ImageIO.read(new File("imagenesSpaceInvaders/win.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		fuenteIncial = new Font("Arial", Font.BOLD, 20);
		colorLetra = Color.YELLOW;
		reescalarImagen();
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.drawImage(imagenRescalada, 0, 0, null);
		g.setFont(fuenteIncial);
		g.setColor(colorLetra);
	
		g.drawString("Puntuacion: "+puntuacion, panelJuego.getWidth() / 2 + 250, panelJuego.getHeight() / 2 + 400);
		g.drawString("Cerrar", panelJuego.getWidth() / 2 - 400, panelJuego.getHeight() / 2 + 400);

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
		System.exit(0);

		

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

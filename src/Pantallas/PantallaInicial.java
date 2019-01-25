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
import Pantallas.PantallaJuego;

public class PantallaInicial implements Pantalla{
	PanelJuego panelJuego;

	BufferedImage imagenFondo;
	Image imagenRescalada;

	Color colorLetra;
	Font fuenteIncial;
	int contadorColorFrames = 0;
	static final int cambioColorInicio = 10;

	public PantallaInicial(PanelJuego panelJuego) {
		super();
		this.panelJuego = panelJuego;
	}

	@Override
	public void inicializarPantalla() {
		
		
		try {
			imagenFondo = ImageIO.read(new File("ImagenesSpaceInvaders/PantallaPrincipal.PNG"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		fuenteIncial = new Font("Arial", Font.BOLD, 40);
		colorLetra = Color.YELLOW;
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, panelJuego.getWidth(), panelJuego.getHeight());

		g.drawImage(imagenRescalada, 0, 0, null);
		g.setFont(fuenteIncial);
		g.setColor(colorLetra);
		g.drawString("Pulsa para Jugar", panelJuego.getWidth() / 2 - 300, panelJuego.getHeight() / 2 + 250);

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
		PantallaJuego pantallaJuego = new PantallaJuego(panelJuego);
		pantallaJuego.inicializarPantalla();
		panelJuego.setPantallaActual(pantallaJuego);

	}

	@Override
	public void redimensionarPantalla(ComponentEvent e) {
		imagenRescalada = imagenFondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				Image.SCALE_SMOOTH);

	}

}

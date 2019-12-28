import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Labyrinthe extends JPanel implements ActionListener{

	private int longueur = 25;
	private int nombre_carre = 15;
	private int taille_fenetre = nombre_carre * longueur;
	private Timer timer;
	private Color bleu = new Color(0, 0, 200);
	private Color rouge = new Color(200,0,0);
	private Color vert = new Color(0,200,0);
	private Color rose = new Color(100,0,100);
	Heros h = new Heros();
	Monstre m = new Monstre();


	// gauche = 1
	// haut = 2
	// droite = 4
	// bas = 8
	// rien = 16


	static int[] lab;

	private void genererLabyrinthe(Graphics2D g2d,int level) {
		if (level==0) {
			lab=ReadFile.read("labyrinthe_vide");
		}
		if (level==1) {
			lab=ReadFile.read("lab_intial");
		}
		if (level==2) {
			lab=ReadFile.read("hardcorelele");
		}
		if (level==3) {
			lab=ReadFile.read("hardcorelele2");
		}
		g2d.setColor(rose);
		int i = 0;
		for (int y = 0; y < taille_fenetre; y += longueur) {
			for (int x = 0; x < taille_fenetre; x += longueur) {
				if ((lab[i] & 1) != 0) { 
					g2d.drawLine(x,y,x,y+longueur-1);
				}
				if ((lab[i] & 2) != 0) { 
					g2d.drawLine(x,y,x+longueur-1,y);
				}
				if ((lab[i] & 4) != 0) { 
					g2d.drawLine(x+longueur-1,y,x+longueur-1,y+longueur-1);
				}
				if ((lab[i] & 8) != 0) { 
					g2d.drawLine(x, y+longueur-1,x+longueur-1,y+longueur-1);
				}
				//				if ((lab[i] & 16) != 0) { // voir si on met des points à gagner plus tard
				//					g2d.fillRect(x + 11, y + 11, 2, 2);
				//				}

				i++;
			}
		}
	}

	public void afficherVie(Graphics2D g2d) {
		g2d.setColor(bleu);
		for (int i=0; i<h.vitalite; i++) {
			g2d.fillRect(15*i, 15*26, 5, 5);
		}
		if (m.x == h.x & m.y == h.y) {
			h.vitalite--;
		}
		if (h.vitalite==0) {
			setVisible(false);
			System.out.println("GAME OVER");
		}
		if (h.x == 7*25 && h.y == 7*25) {
			for (int j=0; h.vitalite<3; j++) {

				h.vitalite++;
			}

		}

	}

	public void chargerImage(Graphics2D g2d,int dxx, int dyy) {

		String adressedufichier = System.getProperty("user.dir") + "/" + "Ressources" + "/";

		try {

			File input1 = new File(adressedufichier + "heros.gif");
			h.image_heros = ImageIO.read(input1);
			g2d.drawImage(h.image_heros, dxx, dyy, 25, 25,null);

			File input2 = new File(adressedufichier + "tresor.png"); //plus tard creer une classe tresor
			g2d.drawImage(ImageIO.read(input2), 14*25, 14*25, 25, 25, null);

			File input3 = new File(adressedufichier + "soin.png");
			g2d.drawImage(ImageIO.read(input3), 7*25, 7*25, 25, 25, null);

		} catch (IOException ie) {
			System.out.println("Erreur :"+ie.getMessage());
		}

	}

	public void chargerImageMonstre(Graphics g2d, int x, int y) {
		String adressedufichier = System.getProperty("user.dir") + "/" + "Ressources" + "/";
		try {		
			File input = new File(adressedufichier + "monstre.png");
			m.image_monstre = ImageIO.read(input);
			g2d.drawImage(m.image_monstre, x, y, 25, 25, null);

		} catch (IOException ie) {
			System.out.println("Erreur :"+ie.getMessage());
		}
	}

	public void GameOver(Graphics g2d) { //non utilisé pour l'instant on se contente de fermer la fenêtre
		String adressedufichier = System.getProperty("user.dir") + "/" + "Ressources" + "/";
		try {

			File input1 = new File(adressedufichier + "gameover.png");
			g2d.drawImage(ImageIO.read(input1), 15*25/2, 15*25/2, 400, 420, null);

		} catch (IOException ie) {
			System.out.println("Erreur :"+ie.getMessage());
		}
	}

	public void deplacementMonstre(Graphics g2d) {
		Random random = new Random();
		ArrayList<String> choix = new ArrayList();
		if (m.x % 25 == 0 && m.y % 25 ==0) {
			//System.out.println("kakakaka");
			int position = m.x/25 + 15 * (int)(m.y/25);
			if ((lab[position] & 1) ==0 && m.dx != 1) {
				choix.add("Left");
			}
			if ((lab[position] & 2) ==0 && m.dy != 1) {
				choix.add("Up");
			}
			if ((lab[position] & 4) ==0 && m.dx != -1) {
				choix.add("Right");
			}
			if ((lab[position] & 8) ==0 && m.dy != -1) {
				choix.add("Down");
			}
			if (choix.size()!=0) {
				int nombreAleatoire = random.nextInt(choix.size());
				String direction = choix.get(nombreAleatoire);
				switch (direction) {
				case "Left":
					m.moveLeft();
					break;
				case "Right":
					m.moveRight();
					break;
				case "Up":
					m.moveUp();
					break;
				case "Down":
					m.moveDown();
					break;
				}
			}
			else {
				m.dx=-m.dx;
				m.dy=-m.dy;
			}
		}
		m.setX(m.getX()+(m.dx*3));
		m.setY(m.getY()+(m.dy*3));
		chargerImageMonstre(g2d, m.getX(), m.getY());
	}

	public void paintComponent(Graphics g) {
		setBackground(new Color(0,0,0));
		super.paintComponent(g);
		timer = new Timer(5, (ActionListener) this);
		timer.start();


		Graphics2D g2d = (Graphics2D) g;
		chargerImage(g2d,h.getX(),h.getY());
		genererLabyrinthe(g2d,Principale.level);
		deplacementMonstre(g2d);
		afficherVie(g2d);
		repaint();
		g.dispose();
		if (h.x == 350 && h.y == 350) {
			System.out.println("PARTIE GAGNEE");
			repaint();
			setVisible(false);

		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int pos = h.x/25+15*(int)h.y/25;
		int pos_ = lab[pos];
		if (h.dx==-1 && h.dy==0 && (pos_ & 1) != 0 || h.dx==1 && h.dy==0 && (pos_ & 4) != 0 ||
				h.dx==0 && h.dy==-1 && (pos_ & 8) != 0 || h.dx==0 && h.dy==1 && (pos_ & 2) != 0 ) {
			//System.out.println(h.dy);
			h.dx=0;
			h.dy=0;
		}
		else {
			h.move();
			h.dx=0;
			h.dy=0;
		}

		repaint();
	}


}

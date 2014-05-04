package Bilde_Redigering;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import Client.BildeBibliotek;
import Client.Handlere.mainFrame;
import Client.Paneler.BildeOversikt;

public class BildeRotasjon extends JPanel {
	BildeBibliotek Bibliotek;
	
	private BufferedImage img;
	int x;
	int y;
	int dx; 
	int dy;

	private float angle;

	public BildeRotasjon(BufferedImage bilde){

	          setImg(bilde);
	         // JLabel label = new JLabel(new ImageIcon(getImg()));
              //label.setMinimumSize(new Dimension(200,200));
              	x = getWidth(); 
          		y = getHeight();
          	
          		setPreferredSize(new Dimension(x,y));
              //this.add(label);
	          this.repaint();

	}
	/*
	public BufferedImage rightRotation(){
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage returnImage = new BufferedImage( height, width , img.getType());
		for( int x = 0; x < width; x++ ) {
			for( int y = 0; y < height; y++ ) {
				returnImage.setRGB(height-y-1, x, img.getRGB(x,y));
			}
		}
		return returnImage;
	}
  */ 
	public void Rotasjon(double degrees){
		/*
		 *  1 = Horizontal (normal) 
			2 = Mirror horizontal 
			3 = Rotate 180 
			4 = Mirror vertical 
			5 = Mirror horizontal and rotate 270 CW 
			6 = Rotate 90 CW 
			7 = Mirror horizontal and rotate 90 CW 
			8 = Rotate 270 CW
		 */
		int OrientationNumber = 1;
		switch ((int)degrees){
		case 0:
			OrientationNumber = 1;
			break;
		case 90:
			OrientationNumber = 6;
			break;
		case -90:
			OrientationNumber = 8;
			break;
		case 180:
			OrientationNumber = 3;
			break;
		
		}
		
		mainFrame.Server.writeMetadata(Bibliotek.serverPanel.bildeIDVector.get(Bibliotek.serverPanel.bildeNummer), "Orientation", Integer.toString(OrientationNumber));
		
    	angle+=Math.toRadians(degrees);
    	this.setImg(img);

	}


	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform turn = new AffineTransform();
		double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	    int w = img.getWidth(), h = img.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	    g2D.translate(this.getWidth() / 2, this.getHeight() / 2);
	    g2D.rotate(angle);
	    g2D.translate(-img.getWidth()/2, -img.getHeight()/2);
        g2D.drawRenderedImage(getImg(), turn);  
        g2D.dispose();
    }


	public Dimension getPreferredSize() {
        if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }
    }

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}
}
package Bilde_Redigering;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import Client.BildeBibliotek;
public class BildeTegning extends JPanel implements MouseListener, MouseMotionListener {
	public boolean aktiverTegning = false;
	public int tykkelse = 2;
	public Color color = Color.BLACK;
	private BufferedImage img;
	int x1;
	int y1; 
	int x2; 
	int y2;
	int x;
	int y;
	
	public BildeTegning(BufferedImage bilde){
			
		setImg(bilde);
         	x = getWidth(); 
     		y = getHeight();
     	
     		setPreferredSize(new Dimension(x,y));
         this.repaint();
         
		addMouseListener(this);
        addMouseMotionListener(this);
	}
	
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform turn = new AffineTransform();
		g2D.translate(this.getWidth() / 2, this.getHeight() / 2);
		g2D.translate(-img.getWidth()/2, -img.getHeight()/2);
		g2D.drawRenderedImage(getImg(), turn); 
		g2D.dispose();
    }
	
	public Dimension getPreferredSize() {
        if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(), img.getHeight());
       }
    }
	public BufferedImage getImg() {
		return img;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}	
	
	@Override public void mousePressed(MouseEvent e) {
		x1 = e.getX() - (this.getWidth()/2) + (img.getWidth()/2);  
		y1 = e.getY() - (this.getHeight()/2) + (img.getHeight()/2); 
		
    }
    @Override public void mouseDragged(MouseEvent e) {
    	
    	Graphics2D g2 = img.createGraphics();
    	if(SwingUtilities.isLeftMouseButton(e)){
    		if(aktiverTegning){
    			x2 = e.getX() - (this.getWidth()/2) + (img.getWidth()/2);  
    			y2 = e.getY() - (this.getHeight()/2) + (img.getHeight()/2); 
    			g2.setColor(color);
    			BasicStroke stroke = new BasicStroke(tykkelse);
    			g2.setStroke(stroke);
    			g2.drawLine(x1, y1, x2, y2);  
        		x1 = x2; y1 = y2;
        		repaint();
        		g2.dispose();
        	
    	}	
    	}	
    	
    	
        	
    }
    @Override public void mouseReleased(MouseEvent e) {
    	
    }
    
    @Override public void mouseMoved(MouseEvent e) {
    	x1 = e.getX() - (this.getWidth()/2) + (img.getWidth()/2); 
    	y1 = e.getY() - (this.getHeight()/2) + (img.getHeight()/2); 
    }
    
    @Override public void mouseClicked(MouseEvent e) {
    }
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
package Bilde_Redigering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class BildeCropping extends JPanel {
	
	private BufferedImage img, img0;
	int x;
	int y;
	int dx; 
	int dy;
	int type;
	private float angle;
    int x1,x2,y1,y2;
    boolean croppingActive;
    
    public BildeCropping(BufferedImage imgScr){
		setImg(imgScr);
		x = getWidth(); 
		y = getHeight();
		this.repaint();

}
	
	 public BufferedImage cropImage(int x1, int y1, int x2, int y2, BufferedImage src){
		   BufferedImage dest = null;
		   if(x1>x2){
			   int tmp = x1;
			   x1 = x2;
			   x = tmp;
		   }
		   if(y1>y2){
			   int tmp = y1;
			   y1 = y2;
			   y2 = tmp;
		   }
			dest = src.getSubimage(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
			img = dest;
			this.repaint();


		return dest;


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

			img0 = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);

			for(int i = 0; i < img.getWidth(); i++){
				for(int j = 0; j < img.getHeight(); j++){
					img0.setRGB(i, j, img.getRGB(i, j));
				}
			}



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
		public void reloadPic(){

			if(img == null || img0 == null){
				return;
			}

			for(int i = 0; i < img.getWidth(); i++){
				for(int j = 0; j < img.getHeight(); j++){
					img.setRGB(i, j, img0.getRGB(i, j));
				}
			}
		}


}

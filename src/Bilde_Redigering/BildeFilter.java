package Bilde_Redigering;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BildeFilter extends JPanel {

	private BufferedImage img, img0;
	int x;
	int y;
	int dx; 
	int dy;
	int type;
	private float angle;

	public BildeFilter(BufferedImage imgScr){
			setImg(imgScr);
			x = getWidth(); 
			y = getHeight();
			this.repaint();

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
	
	public int filterRGB(int x, int y, int rgb) {
		return ((rgb & 0xff00ff00)
				| ((rgb & 0xff0000) >> 16)
				| ((rgb & 0xff) << 16));
	}
	public void RBFilter(){
		int width_i = img.getWidth(); 
		int height_i = img.getHeight();
		for(int i=0; i<width_i; i++){
			for(int j=0;j<height_i;j++){
				type = img.getRGB(i,j);
				type = filterRGB(i,j,type);
				img.setRGB(i, j, type);	
			}
		}

	}
	public void BWFilter(){
		int width_i = img.getWidth(); 
		int height_i = img.getHeight();

		for(int i=0; i<width_i; i++){
			for(int j=0;j<height_i;j++){
				type = img.getRGB(i,j);
				type = filterRGB(i,j,type);
				img.setRGB(i, j, getGreyscale(type));	
			}
		}

	}




	public void EdgeFilter(){
		int width_i = img.getWidth(); 
		int height_i = img.getHeight();

		for(int i=0; i<width_i-1; i++){
			for(int j=0;j<height_i-1;j++){
				
				int t0 = getRgbScalar(img.getRGB(i, j));
				int t1 = getRgbScalar(img.getRGB(i+1, j));
				int t2 = getRgbScalar(img.getRGB(i, j+1));

				int dt = Math.max(Math.abs(t0-t1), Math.abs(t0-t2));

				if(dt > 15){
					img.setRGB(i, j, 0xffffffff);		
				}else{		
					img.setRGB(i, j, 0x0);				
				}
			}
		}

	}

	public void AvgFilter(){
		int width_i = img.getWidth(); 
		int height_i = img.getHeight();

		for(int i=0; i<width_i-1; i++){
			for(int j=0;j<height_i-1;j++){
				
				int t0 = getRgbAvg(img.getRGB(i, j),img.getRGB(i+1, j));
				int t1 = getRgbAvg(img.getRGB(i, j+1),t0);

					img.setRGB(i, j, t1);		

			}
		}

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

	
	private int getRgbAvg(int argb0, int argb1){
		
		int red0 = argb0 & 0xff;
		int green0 = (argb0 >> 8) & 0xff;
		int blue0 = (argb0 >> 16) & 0xff;
		
		int red1 = argb1 & 0xff;
		int green1 = (argb1 >> 8) & 0xff;
		int blue1 = (argb1 >> 16) & 0xff;
	
		int red = (red0 + red1) /2;
		int green = (green0 + green1) /2;
		int blue = (blue0 + blue1) /2;
		
		
		int avg = (0xff << 24) + (red << 16) + (green << 8) + blue;

		return avg;
	}

	private int getRgbScalar(int argb){
		int red = argb & 0xff;
		int green = (argb >> 8) & 0xff;
		int blue = (argb >> 16) & 0xff;
		int sc = (int)Math.sqrt(red*red + green*green + blue*blue);

		return sc;
	}


	private int getGreyscale(int argb){
		int red = argb & 0xff;
		int green = (argb >> 8) & 0xff;
		int blue = (argb >> 16) & 0xff;
		int avg = ((red + green + blue) / 3) & 0xff;
		int grey = (0xff << 24) + (avg << 16) + (avg << 8) + avg;
		return grey;
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
}

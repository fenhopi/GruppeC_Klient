package Bilde_Redigering;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;



public class BildeZooming {

	private static TransformingCanvas canvas;
	 private static BufferedImage etBilde;

		
	
 
	
	public  BildeZooming(BufferedImage img){
		etBilde = img;
		JFrame frame = new JFrame();
		canvas = new TransformingCanvas();
		TranslateHandler translater = new TranslateHandler();
		canvas.addMouseListener(translater);
		canvas.addMouseMotionListener(translater);
		canvas.addMouseWheelListener(new ScaleHandler());
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.setSize(etBilde.getWidth(), etBilde.getHeight());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	
	}
	
	
	public static class TransformingCanvas extends JComponent {
		private double translateX;
		private double translateY;
		private double scale;
 
		public TransformingCanvas() {
			translateX = 0;
			translateY = 0;
			scale = 1;
			setOpaque(true);
			setDoubleBuffered(true);
		}
 
		@Override public void paint(Graphics g) {
 
			AffineTransform tx = new AffineTransform();
			tx.translate(translateX, translateY);
			tx.scale(scale, scale);
			Graphics2D TegneGraps = (Graphics2D) g;
		
			
			TegneGraps.setColor(Color.WHITE);
			TegneGraps.fillRect(0, 0, getWidth(), getHeight());
		
			
			TegneGraps.setTransform(tx);
			TegneGraps.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			TegneGraps.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);			

			TegneGraps.drawImage(etBilde, null, 0, 0);
			
			 super.paint(g);
		}
	}
 
	public static class TranslateHandler implements MouseListener,
			MouseMotionListener {
		private int lastOffsetX;
		private int lastOffsetY;
 
		public void mousePressed(MouseEvent e) {
			// capture starting point
			lastOffsetX = e.getX();
			lastOffsetY = e.getY();
		}
 
		public void mouseDragged(MouseEvent e) {
			
			// new x and y are defined by current mouse location subtracted
			// by previously processed mouse location
			int newX = e.getX() - lastOffsetX;
			int newY = e.getY() - lastOffsetY;
 
			// increment last offset to last processed by drag event.
			lastOffsetX += newX;
			lastOffsetY += newY;
 
			// update the canvas locations
			canvas.translateX += newX;
			canvas.translateY += newY;
			
			// schedule a repaint.
			canvas.repaint();
		}
 
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
 
	public static class ScaleHandler implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				
				
				canvas.scale += (.1 * e.getWheelRotation());
				
				canvas.scale = Math.max(0.00001, canvas.scale); 
				canvas.repaint();
			}
		}
	}
	
}

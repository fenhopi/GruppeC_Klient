package Other_Resources;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import Server.ServerConnector;
import Server.Util;
import Client.BildeBibliotek;
import Client.Handlere.mainFrame;
import Client.Paneler.BildeOversikt;
import Client.Paneler.BildeVisning;

public class Rating extends JPanel {
	
	JButton starOne = new JButton("");
	JButton starTwo = new JButton("");
	JButton starThree = new JButton("");
	JButton starFour = new JButton("");
	JButton starFive = new JButton("");

	Image fyll = null;
	Image tom = null;
	
	public Rating(){
		setLayout(new FlowLayout());
		
		try {
			fyll = ImageIO.read(getClass().getResource("/Resurser//fyll_Stjerne.png"));
			tom = ImageIO.read(getClass().getResource("/Resurser//tom_Stjerne.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iconMaker(getRating(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer)));
	}
	
	public short getRating(String bildeID){
		
		String metadata = mainFrame.Server.getMetadata(bildeID);
		
		String[][] table = Util.toTable(metadata);
		
		HashMap<String,String> meta = new HashMap<String,String>();
		
		for(int i = 0; i < table.length; i++){		
			meta.put(table[i][0], table[i][1]);		
		}
		
		
		short rating = 0;
		
		if(meta.containsKey("Rating")){
			rating = Short.parseShort(meta.get("Rating"));
		}
		return rating;
	}
	
	public void iconMaker(short rating){
		if (rating == 1){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
	
		}else if (rating ==2){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating ==3){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating ==4){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating == 5){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (fyll.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			
		}else {
			starOne.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH)));
		}
		//Fjern knappe utseende
		starOne.setBorderPainted(false);
		starOne.setOpaque(false);
		starOne.setContentAreaFilled(false);
		starOne.setMargin(new Insets(0,0,0,0));
		
		starTwo.setBorderPainted(false);
		starTwo.setOpaque(false);
		starTwo.setContentAreaFilled(false);
		starTwo.setMargin(new Insets(0,0,0,0));
		
		starThree.setBorderPainted(false);
		starThree.setOpaque(false);
		starThree.setContentAreaFilled(false);
		starThree.setMargin(new Insets(0,0,0,0));
		
		starFour.setBorderPainted(false);
		starFour.setOpaque(false);
		starFour.setContentAreaFilled(false);
		starFour.setMargin(new Insets(0,0,0,0));
		
		starFive.setBorderPainted(false);
		starFive.setOpaque(false);
		starFive.setContentAreaFilled(false);
		starFive.setMargin(new Insets(0,0,0,0));
		
		
		this.add(starOne);
		this.add(starTwo);
		this.add(starThree);
		this.add(starFour);
		this.add(starFive);
		
		ActionListener ratingListener =  new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource()== starOne){
					mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "Rating", "1");
				
				}else if (e.getSource()==starTwo){
					mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "Rating", "2");
				
				}else if (e.getSource()==starThree){
					mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "Rating", "3");
				
				}else if (e.getSource()==starFour){
					mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "Rating", "4");
				
				}else if (e.getSource()==starFive){
					mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "Rating", "5");
				}
				mainFrame.Server.getMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer));
				iconMaker(getRating(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer)));
				
			}
			
		};
		
		starOne.addActionListener(ratingListener);
		starTwo.addActionListener(ratingListener);
		starThree.addActionListener(ratingListener);
		starFour.addActionListener(ratingListener);
		starFive.addActionListener(ratingListener);
					
		
	}
	

}

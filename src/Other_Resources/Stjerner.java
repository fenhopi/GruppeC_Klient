package Other_Resources;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Server.ServerConnector;
import Server.Util;


public class Stjerner extends JPanel implements ActionListener{
	
	
	JButton starOne = new JButton("");
	JButton starTwo = new JButton("");
	JButton starThree = new JButton("");
	JButton starFour = new JButton("");
	JButton starFive = new JButton("");

Image fyll = null;
Image tom = null;
	public Stjerner() throws Exception{
		
		this.setLayout(new FlowLayout());
		String pathFyll = "C://Users//Fotis//Pictures//fyll_Stjerne.png";
		String pathTom ="C://Users//Fotis//Pictures//tom_Stjerne.png";
//		String path = "C://Users//Fotis//Downloads//pic.jpg";
		try{
		fyll = ImageIO.read(new File(pathFyll));
		}catch(IOException io){
			io.printStackTrace();
		}
		try{
		tom = ImageIO.read(new File(pathTom));
		}catch(IOException io){
			io.printStackTrace();
		}
		
		ServerConnector Server = new ServerConnector(false);
		Server.setAddress("http://127.0.0.1:8080");

		iconMaker(getRating(Server));
	}
	public short getRating(ServerConnector Server){
		
		String metadata = Server.getMetadata("7");
		
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
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
	
		}else if (rating ==2){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating ==3){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating ==4){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
		
		}else if (rating == 5){
			starOne.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (fyll.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			
		}else {
			starOne.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starTwo.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starThree.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFour.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
			starFive.setIcon(new ImageIcon (tom.getScaledInstance(120, 50,  java.awt.Image.SCALE_SMOOTH)));
		}
		this.add(starOne);
		this.add(starTwo);
		this.add(starThree);
		this.add(starFour);
		this.add(starFive);
		
		starOne.addActionListener(this);
		starTwo.addActionListener(this);
		starThree.addActionListener(this);
		starFour.addActionListener(this);
		starFive.addActionListener(this);
					
		
	}
	
	
		
	

	
@Override
public void actionPerformed(ActionEvent e) {
	ServerConnector Server = new ServerConnector(false);
	Server.setAddress("http://127.0.0.1:8080");
	if (e.getSource()== starOne){
		Server.writeMetadata("7", "Rating", "1");
	
	}else if (e.getSource()==starTwo){
		Server.writeMetadata("7", "Rating", "2");
	
	}else if (e.getSource()==starThree){
		Server.writeMetadata("7", "Rating", "3");
	
	}else if (e.getSource()==starFour){
		Server.writeMetadata("7", "Rating", "4");
	
	}else if (e.getSource()==starFive){
		Server.writeMetadata("7", "Rating", "5");
	}
	Server.getMetadata("7");
	iconMaker(getRating(Server));
}


	public static void main(String[] args) throws Exception {
		JFrame framme = new JFrame();
		framme.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Stjerner test = new Stjerner();
		framme.add(test);
		framme.setMinimumSize(new Dimension(640,400));
		framme.setExtendedState(framme.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		framme.setVisible(true);
	}

	

}

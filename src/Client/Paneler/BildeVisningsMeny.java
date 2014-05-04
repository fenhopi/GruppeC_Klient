package Client.Paneler;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import Bilde_Redigering.BildeCropping;
import Bilde_Redigering.BildeFilter;
import Bilde_Redigering.BildeRotasjon;
import Bilde_Redigering.BildeTegning;
import Client.BildeBibliotek;
import Client.Handlere.MetaDataHandler;
import Client.Handlere.mainFrame;

public class BildeVisningsMeny extends JTabbedPane {
	private static JPanel redigeringsPanel, metaDataPanel, kommentarPanel; 
	private int PanelHeight, PanelWidth;
	private ActionListener lukkBildeListener;
	private BildeBibliotek Bibliotek;
	private boolean croppingActive;
	int x1,x2,y1,y2;
	
	/*
	 * Konstruktøren får høyden og bredden fra BildeVisningsPanelet slik at
	 * menyen blir rett størrelse
	 */
	public BildeVisningsMeny(int Height, int Width){
		PanelHeight = Height;
		PanelWidth = Width;
		lagGUI();
	}
	
	public void lagGUI(){
		setPreferredSize(new Dimension(PanelWidth,PanelHeight/7));
		
		lagRedigeringsTab();
		lagMetaDataTab();
	}
	
	public void lagRedigeringsTab(){
		//LAG PANEL FOR BILDE REDIGERING
		redigeringsPanel = new JPanel();
		redigeringsPanel.setPreferredSize(new Dimension(PanelWidth,PanelHeight/7));
		redigeringsPanel.setLayout(new GridLayout(0,6));
		add(redigeringsPanel, "Rediger");
		
		Image info;
		try{
			info = ImageIO.read(getClass().getResource("/Resurser//configure-icon.png"));
			info = info.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
			
			ImageIcon redigerIcon = new ImageIcon(info);
			
			JButton redigerBtn = new JButton(redigerIcon);
			redigerBtn.setBorderPainted(false);
			redigerBtn.setOpaque(false);
			redigerBtn.setContentAreaFilled(false);
			add(redigeringsPanel, redigerIcon);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lagRoteringsPanel();
		lagFilterPanel();
		lagTegnePanel();
		lagCroppePanel();
		lagZoomPanel();
		
		gaaTilbakeTilBildeOversikt();
		lagKnappePanel();
	}
	public void lagMetaDataTab(){
		//LAG PANEL FOR VISNING AV METADATA
		metaDataPanel = new JPanel();
		metaDataPanel.setPreferredSize(new Dimension(PanelWidth,PanelHeight/7));
		metaDataPanel.setLayout(new GridLayout(0,1));
		add(metaDataPanel, "Bilde Informasjon");
		
		Image info;
		try{
			info = ImageIO.read(getClass().getResource("/Resurser//info.png"));
			info = info.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
			
			ImageIcon infoIcon = new ImageIcon(info);
			
			JButton infoBtn = new JButton(infoIcon);
			infoBtn.setBorderPainted(false);
			infoBtn.setOpaque(false);
			infoBtn.setContentAreaFilled(false);
			add(metaDataPanel, infoIcon);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lagMetaPanel();
		
	}

	
	public void lagRoteringsPanel(){
		/*
		 * GUI FOR ROTERING AV BILDE
		 */

		JPanel roterPanel = new JPanel();
		roterPanel.setLayout(new GridBagLayout());
		roterPanel.setBorder(BorderFactory.createTitledBorder("Rotering"));
		redigeringsPanel.add(roterPanel);

		Image roterHoegre;
		Image roterVenstre;
		try {     
			//Knapp med pil som roterer bildet mot venstre
			roterVenstre = ImageIO.read(getClass().getResource("/Resurser//rotate-left.png"));
			roterVenstre = roterVenstre.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);

			ImageIcon roterVenstreIcon = new ImageIcon(roterVenstre);

			JButton roterVenstreBtn = new JButton(roterVenstreIcon);
			roterVenstreBtn.setBorderPainted(false);
			roterVenstreBtn.setOpaque(false);
			roterVenstreBtn.setContentAreaFilled(false);
			roterVenstreBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					LayoutManager minLayout = BildeVisning.bildePanel.getLayout();
					BildeVisning.bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					BildeRotasjon RoterBilde = new BildeRotasjon(BildeVisning.AktivtBilde);
					RoterBilde.setBildeBibliotek(Bibliotek);

					BildeVisning.bildeOrientering -= 90;
					RoterBilde.Rotasjon(BildeVisning.bildeOrientering);


					BildeVisning.bildePanel.add(RoterBilde, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
			});

			roterPanel.add(roterVenstreBtn, new GridBagConstraints());

			//Knapp med pil som roterer bilde mot høgre
			roterHoegre = ImageIO.read(getClass().getResource("/Resurser//rotate-right.png"));
			roterHoegre = roterHoegre.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);

			ImageIcon roterHoegreIcon = new ImageIcon(roterHoegre);

			JButton roterHoegreBtn = new JButton(roterHoegreIcon);
			roterHoegreBtn.setBorderPainted(false);
			roterHoegreBtn.setOpaque(false);
			roterHoegreBtn.setContentAreaFilled(false);
			roterHoegreBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					LayoutManager minLayout = BildeVisning.bildePanel.getLayout();
					BildeVisning.bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					BildeRotasjon RoterBilde = new BildeRotasjon(BildeVisning.AktivtBilde);
					RoterBilde.setBildeBibliotek(Bibliotek);

					BildeVisning.bildeOrientering += 90;
					RoterBilde.Rotasjon(BildeVisning.bildeOrientering);


					BildeVisning.bildePanel.add(RoterBilde, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
			});

			roterPanel.add(roterHoegreBtn, new GridBagConstraints());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void lagFilterPanel(){
		/*
		 * LEGG PÅ BILDEFILTER
		 */

		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridBagLayout());
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
		redigeringsPanel.add(filterPanel);

		final String filterTypes[] = {"Normal", "RB Filter", "BW Filter", "Edge Filter", "Avg Filter"};

		final JComboBox filterCombo = new JComboBox(filterTypes);
		filterCombo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BildeFilter bf = null;
				if(filterTypes[filterCombo.getSelectedIndex()].equals("Normal")){
					BildeVisning.bildePanel.removeAll();

					JLabel stortBilde = new JLabel(new ImageIcon(BildeVisning.AktivtBilde));

					BildeVisning.bildePanel.add(stortBilde, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
				if(filterTypes[filterCombo.getSelectedIndex()].equals("RB Filter")){
					bf = null;
					BildeVisning.bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(BildeVisning.AktivtBilde.getWidth(), BildeVisning.AktivtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(BildeVisning.AktivtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.RBFilter();
					bf.repaint();

					BildeVisning.bildePanel.add(bf, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("BW Filter")){
					bf = null;
					BildeVisning.bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(BildeVisning.AktivtBilde.getWidth(), BildeVisning.AktivtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(BildeVisning.AktivtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.BWFilter();
					bf.repaint();

					BildeVisning.bildePanel.add(bf, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("Edge Filter")){
					bf = null;
					BildeVisning.bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(BildeVisning.AktivtBilde.getWidth(), BildeVisning.AktivtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(BildeVisning.AktivtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.EdgeFilter();
					bf.repaint();

					BildeVisning.bildePanel.add(bf, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("Avg Filter")){
					bf = null;
					BildeVisning.bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(BildeVisning.AktivtBilde.getWidth(), BildeVisning.AktivtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(BildeVisning.AktivtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.AvgFilter();
					bf.repaint();

					BildeVisning.bildePanel.add(bf, BorderLayout.CENTER);
					BildeVisning.bildePanel.revalidate();
					BildeVisning.bildePanel.validate();

					validate();
				}

			}

		});
		filterPanel.add(filterCombo, new GridBagConstraints());
	}

	public void lagTegnePanel(){
		//KODE FOR TEGNING PÅ BILDET
		
		final BildeTegning bt = new BildeTegning(BildeVisning.AktivtBilde);
		
		JPanel tegnePanel = new JPanel();
		tegnePanel.setLayout(new GridBagLayout());
		tegnePanel.setBorder(BorderFactory.createTitledBorder("Tegne Verktøy"));
		redigeringsPanel.add(tegnePanel);
		
		Image fargeIcon = null;
		
		try {
			fargeIcon = ImageIO.read(getClass().getResource("/Resurser//color-chooser.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fargeIcon = fargeIcon.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		ImageIcon skiftFargeIcon = new ImageIcon(fargeIcon);
		
		JButton fargeBildeBtn = new JButton(skiftFargeIcon);
		fargeBildeBtn.setBorderPainted(false);
		fargeBildeBtn.setOpaque(false);
		fargeBildeBtn.setContentAreaFilled(false);
		fargeBildeBtn.setPreferredSize(new Dimension(100,50));
		fargeBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bt.aktiverTegning = true;
				bt.color = JColorChooser.showDialog(null, "Velg en farge", bt.color);
		        if(bt.color==null){
		        	bt.color = (Color.BLACK);
		        }
				BildeVisning.bildePanel.add(bt, BorderLayout.CENTER);
				BildeVisning.bildePanel.revalidate();
				BildeVisning.bildePanel.validate();
				validate();
			
			}
		});
		tegnePanel.add(fargeBildeBtn, new GridBagConstraints());
	}
	
	public void lagCroppePanel(){
		final BildeCropping Crop = new BildeCropping(BildeVisning.AktivtBilde);
		
		JPanel croppePanel = new JPanel();
		croppePanel.setLayout(new GridBagLayout());
		croppePanel.setBorder(BorderFactory.createTitledBorder("Crop"));
		redigeringsPanel.add(croppePanel);
		
		
		
		 JButton mnCrop = new JButton("Crop");
	        croppePanel.add(mnCrop, new GridBagConstraints());
	        
	        mnCrop.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent e) {
			        croppingActive = true;
			        BildeVisning.bildePanel.removeAll();
			        BildeVisning.bildePanel.add(Crop, BorderLayout.CENTER);

				}
			});
	        Crop.addMouseListener(new MouseListener() {

	        	@Override
	        	public void mousePressed(MouseEvent e) {
	        		
	        		if (croppingActive==true) {
						x1 = e.getX()-(Crop.getWidth()/2)+(Crop.getImg().getWidth()/2);
						y1 = e.getY()-(Crop.getHeight()/2)+(Crop.getImg().getHeight()/2);
						System.out.println("x1: " + x1 + " y1: " + y1);
					}
	        	}

	        	@Override
	        	public void mouseReleased(MouseEvent e) {
	        		x2=e.getX()-(Crop.getWidth()/2)+(Crop.getImg().getWidth()/2);
	        		y2=e.getY()-(Crop.getHeight()/2)+(Crop.getImg().getHeight()/2);
	        		System.out.println("x2: " + x2 + " y2: " + y2);

	        		if(croppingActive == true){
	        			
	        			System.out.println("Cropedde: "+Crop.getImg().getWidth() + " høyde: " + Crop.getImg().getHeight());
	        			
						System.out.println("x1: " + x1 + " y1: " + y1);
	        			System.out.println("x2: " + x2 + " y2: " + y2);
						Crop.cropImage(x1, y1, x2, y2, Crop.getImg());
						croppingActive = false;

	        		}
	        	}
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}
			});
			JButton mnReload = new JButton("Reload");
			mnReload.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent e) {

					Crop.reloadPic();
					Crop.repaint();
				}
			});
			croppePanel.add(mnReload, new GridBagConstraints());
			//croppePanel.add(Crop, new GridBagConstraints());
	}
	
	public void lagZoomPanel(){
		//KODE FOR zooming PÅ BILDET

		
		JPanel zoomPanel = new JPanel();
		zoomPanel.setLayout(new GridBagLayout());
		zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
		redigeringsPanel.add(zoomPanel);
		
		Image zoomIcon = null;
		
		try {
			zoomIcon = ImageIO.read(getClass().getResource("/Resurser//zoom_inn.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zoomIcon = zoomIcon.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		ImageIcon zoomInnIcon = new ImageIcon(zoomIcon);
		
		JButton zoomInnPaaBildeBtn = new JButton(zoomInnIcon);
		zoomInnPaaBildeBtn.setBorderPainted(false);
		zoomInnPaaBildeBtn.setOpaque(false);
		zoomInnPaaBildeBtn.setContentAreaFilled(false);
		zoomInnPaaBildeBtn.setPreferredSize(new Dimension(100,50));
		
		zoomInnPaaBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				final Bilde_Redigering.BildeZooming bz = new Bilde_Redigering.BildeZooming(BildeVisning.AktivtBilde);	
			}
		});
		zoomPanel.add(zoomInnPaaBildeBtn, new GridBagConstraints());
	}

	public void lagKnappePanel(){
		/*
		 * KNAPPER SOM SLETTER, LUKKER OG LAGRER BILDET
		 */

		JPanel knappPanel = new JPanel();
		knappPanel.setLayout(new GridLayout(0,1));
		knappPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		redigeringsPanel.add(knappPanel);

		Image slett;
		
		JButton slettBildeBtn = new JButton("Slett Bilde");
		try{
			slett = ImageIO.read(getClass().getResource("/Resurser//slett.png"));
			slett = slett.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
			slettBildeBtn.setIcon(new ImageIcon(slett));		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		slettBildeBtn.setPreferredSize(new Dimension(100,50));
		slettBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String bildeID = Bibliotek.serverPanel.bildeIDVector.get(Bibliotek.visningPanel.bildeNummer);
				mainFrame.Server.deleteImage(bildeID);
				Bibliotek.serverPanel.hentBildeNavn(null, null, null, null);
				Bibliotek.changePanel("One");
			}
			
		});
		knappPanel.add(slettBildeBtn);
		
		Image lagre;
		
		JButton lagreBildeBtn = new JButton("Lagre endringer");
		try{
			lagre = ImageIO.read(getClass().getResource("/Resurser//lagre.png"));
			lagre = lagre.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
			lagreBildeBtn.setIcon(new ImageIcon(lagre));		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lagreBildeBtn.setPreferredSize(new Dimension(100,50));
		lagreBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Lagre bilde endringer
			}
		});
		//knappPanel.add(lagreBildeBtn);

		Image lukk;
		
		JButton lukkBildeVisningBtn = new JButton("Lukk Bilde");
		try{
		lukk = ImageIO.read(getClass().getResource("/Resurser//lukk.png"));
		lukk = lukk.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
		lukkBildeVisningBtn.setIcon(new ImageIcon(lukk));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		lukkBildeVisningBtn.setPreferredSize(new Dimension(100,50));
		lukkBildeVisningBtn.addActionListener(lukkBildeListener);
		knappPanel.add(lukkBildeVisningBtn);
	}

	public void lagMetaPanel(){
		MetaDataHandler MetaData = new MetaDataHandler();
		MetaData.lastInnMetaData();
		MetaData.basicMetaDataCheck();
		
		MetaDataPanel MetaPanel = new MetaDataPanel();
		MetaPanel.setBildeBibliotek(Bibliotek);
		metaDataPanel.add(MetaPanel);
	}
	
	public void gaaTilbakeTilBildeOversikt(){
		lukkBildeListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Bibliotek.visningPanel.removeAll();
				Bibliotek.changePanel("One");
			}
		};
	}
	
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}
}

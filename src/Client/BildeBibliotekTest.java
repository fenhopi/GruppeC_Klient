package Client;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;

import javax.swing.JProgressBar;

import Bilde_Redigering.BildeFilter;
import Bilde_Redigering.BildeRotasjon;
import Bilde_Redigering.BildeTegning;
import Client.Handlere.BildeHandler;
import Client.Handlere.mainFrame;
import Client.Paneler.opplastingsPanel;
import Other_Resources.Rating;
import Server.Util;

public class BildeBibliotekTest extends JFrame {

	ActionListener listener;
	private BufferedImage valgtBilde;
	public JPanel contentPane, lokalPanel, serverPanel, menuPanel, visningPanel;
	static JPanel mainPanel;
	public JPanel bildePanel;
	opplastingsPanel opplastingsPanel;
	public JProgressBar LasteBar = new JProgressBar(0,100);
	private JToolBar buttonBar = new JToolBar();
	private JLabel stortBilde;
	private Vector<String> bildeNavnServerThumb = new Vector<String>();
	private Vector<String> bildeNavnServerStort = new Vector<String>();
	private Vector<String> bildeNavnReal = new Vector<String>();
	public static Vector<String> bildeIDVector = new Vector<String>();
	private int x = 0, y = 0, i = 0, Fremgang = 0, bildeOrientering;
	public static int bildeNummer;
	public static CardLayout panelKontroll = new CardLayout();
	private CardLayout MetaDataKontroll = new CardLayout();
	BildeHandler BildeHandle;
	private JButton images[];
	JScrollPane RullBilder;
	JTabbedPane mainPanels, bildeInfoPanels;
	public BildeFilter bf;
	public JComboBox kategoriCombo = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BildeBibliotekTest frame = new BildeBibliotekTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BildeBibliotekTest() {
		mainFrame lastInnstillinger = new mainFrame();
		buildGUI();
	}

	public void buildGUI(){
		///////////////////////////////////////////////////////////////
		//					   JFRAME INNSTILLINGER					 //
		//////////////////////////////////////////////////////////////

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 

		///////////////////////////////////////////////////////////////
		//					   PANELER OG LAYOUTER					 //
		//////////////////////////////////////////////////////////////
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 5));

		GridLayout mainLayout = new GridLayout(0,1);
		GridLayout menuLayout = new GridLayout(0,6);


		//lokalPanel = new JPanel();
		//lokalPanel.setLayout(mainLayout);

		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		getContentPane().add(menuPanel, BorderLayout.NORTH);
		
		JPanel sortByCatPanel = new JPanel();
		sortByCatPanel.setLayout(new FlowLayout());
		menuPanel.add(sortByCatPanel, BorderLayout.EAST);
		
		JLabel MKategoriLbl = new JLabel("Sorter etter kategori:", JLabel.LEFT);
		sortByCatPanel.add(MKategoriLbl);
		
		String Kategorier[][] = Util.toTable(mainFrame.Server.getAllCategories());
		String tempKat[] = new String[Kategorier.length];
		for(int i = 0; i < Kategorier.length; i++){
			String Category = Kategorier[i][1];
			tempKat[i] = Category;
		}
		kategoriCombo = new JComboBox(tempKat);
		sortByCatPanel.add(kategoriCombo);

		mainPanel = new JPanel(panelKontroll);


		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayout(0,6));

		visningPanel = new JPanel();
		visningPanel.setLayout(new BorderLayout());

		RullBilder = new JScrollPane(serverPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		mainPanel.add(RullBilder, "One");
		mainPanel.add(visningPanel, "Two");

		panelKontroll.show(mainPanel, "One");

		opplastingsPanel = new opplastingsPanel();



		mainPanels = new JTabbedPane();
		//mainPanels.add("Lokalt Bibliotek",RullBilder);
		mainPanels.add("Server Bibliotek",mainPanel);
		mainPanels.add("Last opp Bilde",opplastingsPanel);
		mainPanels.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mainFrame.nyeBilder){
					resetAll(true);
					lastInnBilderServer(false);

					mainFrame.nyeBilder = false;
				}
			}

		});

		getContentPane().add(mainPanels);

		///////////////////////////////////////////////////////////////
		//				   ANDRE GUI KOMPONENTER					 //
		//////////////////////////////////////////////////////////////


		buttonBar.add(Box.createGlue());


		// this centers the frame on the screen
		setLocationRelativeTo(null);

		//Listener som bestemmer hva som skal skje når en bruker klikker på et bildeikon.
		bildeKlikketListener();
		resetAll(true);
		lastInnBilderServer(false); 


	}




	/*
	 * LASTING OG VISNING AV BILDER
	 * Henter filsti til store bilder og thumbnails.
	 * Laster inn thumbnails fra server, og viser dem i JPanel.
	 * Actionlistener som laster inn bildet i full størrelse og setter opp redigerings valg, når thumbnails blir klikker på.
	 */
	public void lastInnBilderServer(final boolean baserPaaKategori){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BildeHandle = new BildeHandler();
				if(baserPaaKategori){
					bildeNavnServerThumb = BildeHandle.HentBildeNavnFraServer("ThumbnailNavn", kategoriCombo.getSelectedItem().toString());
					bildeNavnServerStort = BildeHandle.HentBildeNavnFraServer("ImageNavn",kategoriCombo.getSelectedItem().toString());
					bildeNavnReal = BildeHandle.HentBildeNavnFraServer("RealNavn",kategoriCombo.getSelectedItem().toString());
					bildeIDVector = BildeHandle.HentBildeNavnFraServer("bildeID",kategoriCombo.getSelectedItem().toString());
				}
				else{
					bildeNavnServerThumb = BildeHandle.HentBildeNavnFraServer("ThumbnailNavn", "");
					bildeNavnServerStort = BildeHandle.HentBildeNavnFraServer("ImageNavn", "");
					bildeNavnReal = BildeHandle.HentBildeNavnFraServer("RealNavn", "");
					bildeIDVector = BildeHandle.HentBildeNavnFraServer("bildeID", "");
				}
				images = new JButton[bildeNavnServerThumb.size()];
				for (int j = 0; j < bildeNavnServerThumb.size(); j++) {
					Fremgang += 1;
					BufferedImage TmpImage;
					ImageIcon icon;
					TmpImage = BildeHandle.lagBildeIkonServer(bildeNavnServerThumb.get(j));

					if(TmpImage != null){

						icon = new ImageIcon(TmpImage);

						ThumbnailAction thumbAction;
						//ImageIcon thumbnailIcon = new ImageIcon(BildeHandle.hentBilde(icon.getImage(), 120, 120));
						ImageIcon thumbnailIcon = icon;
						thumbAction = new ThumbnailAction(thumbnailIcon, bildeNavnServerThumb.get(j));

						JPanel imageHolder = new JPanel();
						imageHolder.setLayout(new BoxLayout(imageHolder, BoxLayout.PAGE_AXIS));
						imageHolder.setBorder(new EmptyBorder(10, 0, 10, 0) );

						JLabel bildeNavnLbl;

						if(bildeNavnReal.get(j).length() > 14){
							bildeNavnLbl = new JLabel(bildeNavnReal.get(j).substring(0, 14) + "...", SwingConstants.RIGHT);
						}
						else{
							bildeNavnLbl = new JLabel(bildeNavnReal.get(j), SwingConstants.RIGHT);
						}
						bildeNavnLbl.setBorder(new EmptyBorder(0, 30, 0, 0) );


						images[i] = new JButton(thumbAction);
						images[i].setBorderPainted(false);
						images[i].setOpaque(false);
						images[i].setContentAreaFilled(false);
						images[i].addActionListener(listener);
						images[i].setActionCommand(Integer.toString(j));

						final JPopupMenu popup = new JPopupMenu();

						final int tmpJ = j;
						popup.add(new JMenuItem(new AbstractAction("Slett Bilde") {
							public void actionPerformed(ActionEvent e) {
								String bildeID = bildeNavnServerThumb.get(tmpJ);
								bildeID = bildeID.substring(bildeID.indexOf("/")+1, bildeID.indexOf("."));
								mainFrame.Server.deleteImage(bildeID);
								resetAll(true);
								lastInnBilderServer(false);
							}
						}));

						images[i].addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent e) {
								if(SwingUtilities.isRightMouseButton(e)){
									popup.show(e.getComponent(), e.getX(), e.getY());
								}
							}
						});

						// add the new button BEFORE the last glue
						// this centers the buttons in the toolbar
						imageHolder.add(images[i]);
						imageHolder.add(bildeNavnLbl);
						serverPanel.add(imageHolder);
						serverPanel.repaint();
						serverPanel.revalidate();
						i++;

					}
				}
				serverPanel.repaint();
				serverPanel.revalidate();
			}
		});
	}


	public void resetAll(Boolean isServer){
		x = 0;
		y = 0;
		i = 0;
		if(isServer){
			serverPanel.removeAll();
			bildeNavnServerThumb = null;
			bildeNavnServerStort = null;
			images = null;
		}
		else{
			lokalPanel.removeAll();	
		}
		LasteBar = null;
		visningPanel.removeAll();

	}

	public void LagBildeFremvisning(){
		stortBilde = new JLabel(new ImageIcon(valgtBilde));

		bildePanel = new JPanel();
		bildePanel.setLayout(new BorderLayout());
		bildePanel.setPreferredSize(new Dimension(visningPanel.getWidth(),(visningPanel.getHeight()/8)*7));
		visningPanel.add(bildePanel);

		bildePanel.add(stortBilde, BorderLayout.CENTER);

		//LAST INN METADATA
		String MetaData = mainFrame.Server.getMetadata(bildeIDVector.get(bildeNummer));

		String MetaDataTable[][] = Server.Util.toTable(MetaData);

		//SJEKK OM BILDE HAR ROTERING
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
		if(MetaData.contains("Orientation")){
			String MetaRotation = MetaDataTable[3][1];
			if(!MetaRotation.equals("1")){
				LayoutManager minLayout = bildePanel.getLayout();
				BildeRotasjon RoterBilde = new BildeRotasjon(valgtBilde);
				switch(Integer.parseInt(MetaRotation)){
				case 3:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));
					
					

					RoterBilde.Rotasjon(180);
					
					bildeOrientering = 180;

					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
					break;
				case 6:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					RoterBilde.Rotasjon(90);
					
					bildeOrientering = 90;

					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
					break;
				case 8:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					RoterBilde.Rotasjon(-90);

					bildeOrientering = -90;
					
					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
					break;

				}

			}
		}

		//LAG PANEL FOR BILDE REDIGERING
		JPanel redigeringsPanel = new JPanel();
		redigeringsPanel.setPreferredSize(new Dimension(visningPanel.getWidth(),visningPanel.getHeight()/7));
		redigeringsPanel.setLayout(new GridLayout(0,5));

		//LAG PANEL FOR VISNING AV METADATA
		JPanel metaDataPanel = new JPanel();
		metaDataPanel.setPreferredSize(new Dimension(visningPanel.getWidth(),visningPanel.getHeight()/7));
		metaDataPanel.setLayout(new GridLayout(0,1));

		//LAG PANEL FOR VISNING OG SKRIVING AV KOMMENTARER
		JPanel kommentarPanel = new JPanel();
		kommentarPanel.setPreferredSize(new Dimension(visningPanel.getWidth(),visningPanel.getHeight()/7));
		kommentarPanel.setLayout(new GridLayout(0,3));

		//LEGG TIL PANEL I TABBED PANES
		bildeInfoPanels = new JTabbedPane();
		bildeInfoPanels.setPreferredSize(new Dimension(visningPanel.getWidth(),visningPanel.getHeight()/7));
		bildeInfoPanels.add(redigeringsPanel, "Rediger");
		bildeInfoPanels.add(metaDataPanel, "Bilde Informasjon");
		bildeInfoPanels.add(kommentarPanel, "Kommentarer");
		visningPanel.add(bildeInfoPanels, BorderLayout.SOUTH);


		/*
		 * GUI FOR ROTERING AV BILDE
		 */

		JPanel roterPanel = new JPanel();
		roterPanel.setLayout(new FlowLayout());
		roterPanel.setBorder(new EmptyBorder(visningPanel.getHeight()/50, 0, 0, 0) );
		redigeringsPanel.add(roterPanel);

		JLabel roterLbl = new JLabel("Roter Bilde:");
		roterPanel.add(roterLbl);

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

					LayoutManager minLayout = bildePanel.getLayout();
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					BildeRotasjon RoterBilde = new BildeRotasjon(valgtBilde);

					bildeOrientering -= 90;
					RoterBilde.Rotasjon(bildeOrientering);


					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
			});

			roterPanel.add(roterVenstreBtn);

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

					LayoutManager minLayout = bildePanel.getLayout();
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					BildeRotasjon RoterBilde = new BildeRotasjon(valgtBilde);

					bildeOrientering += 90;
					RoterBilde.Rotasjon(bildeOrientering);


					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
			});

			roterPanel.add(roterHoegreBtn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * LEGG PÅ BILDEFILTER
		 */

		JPanel filterPanel = new JPanel();
		filterPanel.setBorder(new EmptyBorder(visningPanel.getHeight()/35, 0, 0, 0) );
		redigeringsPanel.add(filterPanel);

		final String filterTypes[] = {"Normal", "RB Filter", "BW Filter", "Edge Filter", "Avg Filter"};

		final JComboBox filterCombo = new JComboBox(filterTypes);
		filterCombo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(filterTypes[filterCombo.getSelectedIndex()].equals("Normal")){
					bildePanel.removeAll();

					stortBilde = new JLabel(new ImageIcon(valgtBilde));

					bildePanel.add(stortBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
				if(filterTypes[filterCombo.getSelectedIndex()].equals("RB Filter")){
					bf = null;
					bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(valgtBilde.getWidth(), valgtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(valgtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.RBFilter();
					bf.repaint();

					bildePanel.add(bf, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("BW Filter")){
					bf = null;
					bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(valgtBilde.getWidth(), valgtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(valgtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.BWFilter();
					bf.repaint();

					bildePanel.add(bf, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("Edge Filter")){
					bf = null;
					bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(valgtBilde.getWidth(), valgtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(valgtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.EdgeFilter();
					bf.repaint();

					bildePanel.add(bf, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}
				else if(filterTypes[filterCombo.getSelectedIndex()].equals("Avg Filter")){
					bf = null;
					bildePanel.removeAll();

					BufferedImage tmp = 
						new BufferedImage(valgtBilde.getWidth(), valgtBilde.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(valgtBilde, 0, 0, null);

					bf = new BildeFilter(tmp);

					bf.AvgFilter();
					bf.repaint();

					bildePanel.add(bf, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					getContentPane().validate();
				}

			}

		});
		filterPanel.add(filterCombo);


		//KODE FOR TEGNING PÅ BILDET
		
		final BildeTegning bt = new BildeTegning(valgtBilde);
		
		JPanel tegnePanel = new JPanel();
		tegnePanel.setLayout(new GridLayout(0,1));
		tegnePanel.setBorder(new EmptyBorder(visningPanel.getHeight()/50, 0, 0, 0) );
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
				bildePanel.add(bt, BorderLayout.CENTER);
				bildePanel.revalidate();
				bildePanel.validate();
				getContentPane().validate();
			
			}
		});
		tegnePanel.add(fargeBildeBtn);

		/*
		 * KNAPPER SOM SLETTER, LUKKER OG LAGRER BILDET
		 */

		JPanel knappPanel = new JPanel();
		knappPanel.setLayout(new GridLayout(0,1));
		knappPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		redigeringsPanel.add(knappPanel);

		JButton slettBildeBtn = new JButton("Slett Bilde");
		slettBildeBtn.setPreferredSize(new Dimension(100,50));
		slettBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String bildeID = bildeNavnServerStort.get(bildeNummer);
				bildeID = bildeID.substring(bildeID.indexOf("/")+1, bildeID.indexOf("."));
				mainFrame.Server.deleteImage(bildeID);
				resetAll(true);
				lastInnBilderServer(false);
				panelKontroll.show(mainPanel, "One");
			}
		});
		knappPanel.add(slettBildeBtn);

		JButton lagreBildeBtn = new JButton("Lagre endringer");
		lagreBildeBtn.setPreferredSize(new Dimension(100,50));
		lagreBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Lagre bilde endringer
			}
		});
		knappPanel.add(lagreBildeBtn);

		JButton lukkBildeVisningBtn = new JButton("Lukk Bilde");
		lukkBildeVisningBtn.setPreferredSize(new Dimension(100,50));
		lukkBildeVisningBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				visningPanel.removeAll();
				panelKontroll.show(mainPanel, "One");
			}
		});
		knappPanel.add(lukkBildeVisningBtn);

		/*
		 * GUI FOR VISNING AV META DATA INFOMRASJON
		 */

		//Check metadata information on picture, if it doesn't exist write it

		if(!MetaData.contains("DocumentName")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "DocumentName", bildeNavnReal.get(bildeNummer));
		}
		if(!MetaData.contains("ImageHeight")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "ImageHeight", Integer.toString(valgtBilde.getHeight()));
		}
		if(!MetaData.contains("ImageWidth")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "ImageWidth", Integer.toString(valgtBilde.getWidth()));
		}
		if(!MetaData.contains("ImageDescription")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "ImageDescription", "Ingen beskrivelse");
		}


		//LAST INN METADATA PÅ NYTT
		MetaData = mainFrame.Server.getMetadata(bildeIDVector.get(bildeNummer));
		MetaDataTable = Server.Util.toTable(MetaData);

		String MetaNavn = MetaDataTable[1][1];

		String MetaHeight = MetaDataTable[3][1];

		String MetaWidth = MetaDataTable[0][1];

		String MetaDesc = MetaDataTable[2][1];

		//TO PANELER SOM BLIR STYRT I EN CARD LAYOUT
		//DET ENE PANELET VISER META DATA DETALJER
		//DET ANDRE LAR DEG ENDRE META DATA DETALJER
		final JPanel metaPanelKontroll = new JPanel(MetaDataKontroll);
		metaDataPanel.add(metaPanelKontroll);

		JPanel metaPanel = new JPanel();
		metaPanel.setBorder(BorderFactory.createTitledBorder("Bilde Informasjon"));
		metaPanel.setLayout(new GridLayout(1,3));

		JPanel editMetaPanel = new JPanel();
		editMetaPanel.setBorder(BorderFactory.createTitledBorder("Endre metadata detaljer"));
		editMetaPanel.setLayout(new GridLayout(1,0));

		metaPanelKontroll.add(metaPanel, "One");
		metaPanelKontroll.add(editMetaPanel, "Two");

		MetaDataKontroll.show(metaPanelKontroll, "One");

		//PANELET SOM VISER METADATA

		JLabel bildeDimLbl = new JLabel("Navn: " + MetaNavn );
		metaPanel.add(bildeDimLbl);

		JLabel bildeDimLbl2 = new JLabel("Størrelse: " + MetaWidth + "x" + MetaHeight);
		metaPanel.add(bildeDimLbl2);

		//RATING

		JPanel holdRatingPanel = new JPanel();
		holdRatingPanel.setLayout(new FlowLayout());

		Rating ratingPanel = new Rating();

		metaPanel.add(holdRatingPanel);
		JLabel bildeDimLbl3 = new JLabel("Rating:");
		holdRatingPanel.add(bildeDimLbl3);
		holdRatingPanel.add(ratingPanel);

		JLabel bildeDimLbl4 = new JLabel("Beskrivelse: " + MetaDesc);
		metaPanel.add(bildeDimLbl4);

		JButton endreMetaDataBtn = new JButton("Endre detaljer");
		endreMetaDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MetaDataKontroll.show(metaPanelKontroll, "Two");
			}
		});		
		metaPanel.add(endreMetaDataBtn);

		//PANELET SOM LAR DEG ENDRE METADATA
		JLabel metaNameLbl = new JLabel("Navn: ");
		editMetaPanel.add(metaNameLbl);
		final JTextField metaNameField = new JTextField(MetaNavn);
		editMetaPanel.add(metaNameField);

		JLabel metaDescLbl = new JLabel("Beskrivelse: ");
		editMetaPanel.add(metaDescLbl);
		final JTextField metaDescField = new JTextField(MetaDesc);
		editMetaPanel.add(metaDescField);

		JButton lagreMetaDataBtn = new JButton("Lagre endringer");
		lagreMetaDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "DocumentName", metaNameField.getText());
				mainFrame.Server.writeMetadata(bildeIDVector.get(bildeNummer), "ImageDescription", metaDescField.getText());
				visningPanel.removeAll();
				panelKontroll.show(mainPanel, "One");
			}
		});		
		editMetaPanel.add(lagreMetaDataBtn);
	}

	public void bildeKlikketListener(){
		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					bildeNummer = Integer.parseInt(((JButton) e.getSource()).getActionCommand());
					valgtBilde = BildeHandle.lagBildeIkonServer(bildeNavnServerStort.get(bildeNummer));

					//Metode som tilpasser bilde størrelsen til panel størrelsen
					if(valgtBilde.getWidth() > visningPanel.getWidth() || valgtBilde.getHeight() > visningPanel.getHeight()-(visningPanel.getHeight()/8)){
						valgtBilde = tilpassBilde(valgtBilde);
					}

					//((JButton) e.getSource()).setBackground(Color.black);

					bildeOrientering = 0;
					
					LagBildeFremvisning();

					panelKontroll.show(mainPanel, "Two");



				}
			}
		};
		setFocusable(true);
		requestFocusInWindow();

		//Key listener for å bla gjennom bildene
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				myKeyEvt(e, "keyReleased");
			}

			private void myKeyEvt(KeyEvent e, String text) {
				int key = e.getKeyCode();

				if ((key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_LEFT) && bildeNummer > 0)
				{
					visningPanel.removeAll();
					bildeNummer -= 1;
					valgtBilde = BildeHandle.lagBildeIkonServer(bildeNavnServerStort.get(bildeNummer));

					//Metode som tilpasser bilde størrelsen til panel størrelsen
					if(valgtBilde.getWidth() > visningPanel.getWidth() || valgtBilde.getHeight() > visningPanel.getHeight()-(visningPanel.getHeight()/7)){
						valgtBilde = tilpassBilde(valgtBilde);
					}

					LagBildeFremvisning();

					visningPanel.revalidate();
					visningPanel.validate();
				}
				else if ((key == KeyEvent.VK_KP_RIGHT || key == KeyEvent.VK_RIGHT) && bildeNavnServerStort.size() > (bildeNummer+1))
				{
					visningPanel.removeAll();
					bildeNummer += 1;
					valgtBilde = BildeHandle.lagBildeIkonServer(bildeNavnServerStort.get(bildeNummer));

					//Metode som tilpasser bilde størrelsen til panel størrelsen
					if(valgtBilde.getWidth() > visningPanel.getWidth() || valgtBilde.getHeight() > visningPanel.getHeight()-(visningPanel.getHeight()/7)){
						valgtBilde = tilpassBilde(valgtBilde);
					}

					LagBildeFremvisning();

					visningPanel.revalidate();
					visningPanel.validate();
				}
			}


		});

	}

	//Funksjon som endrer bildet slik at det passer i JPanel
	public BufferedImage tilpassBilde(BufferedImage bilde){
		BufferedImage Bilde = bilde;

		Image tmp = (Image)Bilde;

		int deleRate = 2;

		while(Bilde.getWidth() > visningPanel.getWidth() || Bilde.getHeight() > visningPanel.getHeight()-(visningPanel.getHeight()/7)){
			if(Bilde.getWidth() > visningPanel.getWidth()){
				double tilpassingsRate = (double)Bilde.getWidth()/(double)visningPanel.getWidth();
				double nyBildeBredde = (double)Bilde.getWidth()/tilpassingsRate;
				double nyBildeHoegde = (double)Bilde.getHeight()/tilpassingsRate;

				tmp = tmp.getScaledInstance((int)nyBildeBredde, (int)nyBildeHoegde, java.awt.Image.SCALE_SMOOTH);
				Bilde = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);

				Graphics2D bGr = Bilde.createGraphics();
				bGr.drawImage(tmp, 0, 0, null);
				bGr.dispose();
			}
			else{
				double tilpassingsRate = (double)Bilde.getHeight()/(double)(visningPanel.getHeight()-(visningPanel.getHeight()/7));
				double nyBildeBredde = Math.floor((double)Bilde.getWidth()/tilpassingsRate);
				double nyBildeHoegde = Math.floor(((double)Bilde.getHeight()/tilpassingsRate));

				tmp = tmp.getScaledInstance((int)nyBildeBredde, (int)nyBildeHoegde, java.awt.Image.SCALE_SMOOTH);
				Bilde = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);

				Graphics2D bGr = Bilde.createGraphics();
				bGr.drawImage(tmp, 0, 0, null);
				bGr.dispose();
			}
		}
		return Bilde;
	}

	/**
	 * Action class that shows the image specified in it's constructor.
	 */
	private class ThumbnailAction extends AbstractAction{

		/**
		 *The icon if the full image we want to display.
		 */
		private Icon displayPhoto;

		/**
		 * @param Icon - The full size photo to show in the button.
		 * @param Icon - The thumbnail to show in the button.
		 * @param String - The descriptioon of the icon.
		 */
		public ThumbnailAction(Icon thumb, String desc){
			//displayPhoto = photo;

			// The short description becomes the tooltip of a button.
			putValue(SHORT_DESCRIPTION, desc);

			// The LARGE_ICON_KEY is the key for setting the
			// icon when an Action is applied to a button.
			putValue(LARGE_ICON_KEY, thumb);
		}

		/**
		 * Shows the full image in the main area and sets the application title.
		 */
		public void actionPerformed(ActionEvent e) {
			//photographLabel.setIcon(displayPhoto);
			setTitle("Bilde: " + getValue(SHORT_DESCRIPTION).toString());
		}
	}




}
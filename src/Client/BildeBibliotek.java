package Client;


import javax.imageio.ImageIO;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import javax.swing.*;

import Client.Handlere.mainFrame;
import Client.Paneler.BildeOversikt;
import Client.Paneler.BildeVisning;
import Client.Paneler.HovedMenyPanel;
import Client.Paneler.opplastingsPanel;
import Server.Util;

public class BildeBibliotek extends JFrame {
	public JPanel contentPane;
	private JPanel mainPanel;
	private CardLayout panelKontroll = new CardLayout();
	opplastingsPanel opplastingsPanel;
	private JScrollPane RullBilder;
	JTabbedPane mainPanels;
	public static BildeOversikt serverPanel;
	public static BildeVisning visningPanel;
	public static HovedMenyPanel menuPanel;
	public JComboBox kategoriCombo = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BildeBibliotek frame = new BildeBibliotek();
					frame.setTitle("IMBD");
					//Sett logo
					Image hovedLogo = ImageIO.read(getClass().getResource("/Resurser//logo.png"));
					frame.setIconImage(hovedLogo);
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
	public BildeBibliotek() {
		//Last inn mainframe som lager en instans of Server
		mainFrame lastInnstillinger = new mainFrame();
		buildGUI();
	}

	public void buildGUI(){
		//Standard innstillinger for JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);

		//Panelet som holder absolutt alt i klienten
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 5));
		
		lagToppMeny();
		lagHovedPaneler();
		lagTabbedView();
	}

	/*
	 * Kaller HovedMenyPanel.java som setter opp alle komponentene som trengs i menyen
	 */
	public void lagToppMeny(){
		menuPanel = new HovedMenyPanel();
		menuPanel.setBildeBibliotek(this);
		contentPane.add(menuPanel, BorderLayout.NORTH);
	}
	
	/*
	 * Lager en instans av BildeVisning.java og BildeOversikt.java og legger de i en cardlayout,
	 * slik at vi lett kan veksle mellom dem.
	 */
	public void lagHovedPaneler(){
		mainPanel = new JPanel(panelKontroll);
		serverPanel = new BildeOversikt();
		serverPanel.setBildeBibliotek(this);
		RullBilder = new JScrollPane(serverPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		visningPanel = new BildeVisning();
		visningPanel.setBildeBibliotek(this);


		mainPanel.add(RullBilder, "One");
		mainPanel.add(visningPanel, "Two");

		changePanel("One");
		
		opplastingsPanel = new opplastingsPanel();
		opplastingsPanel.setBildeBibliotek(this);
	}
	
	/*
	 * Legger opplastingsPanel.java og BildeOversikt.java+BildeVisning.java i tabs
	 */
	public void lagTabbedView(){
		mainPanels = new JTabbedPane();
		//mainPanels.add("Lokalt Bibliotek",RullBilder);
		mainPanels.add("Server Bibliotek",mainPanel);
		mainPanels.add("Last opp Bilde / Ny Kategori",opplastingsPanel);
		mainPanels.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mainFrame.nyeBilder){
					serverPanel.renskBildeOversikt();
					serverPanel.hentBildeNavn(null, null, null, null);
					serverPanel.repaint();

					mainFrame.nyeBilder = false;
				}
			}

		});

		getContentPane().add(mainPanels);
	}
	
	/*
	 * Enkel metode der vi kan veksle mellom BildeOversikt og BildeVisning i CardLayouten.
	 */
	public void changePanel(String toPanel){
		panelKontroll.show(mainPanel, toPanel);
	}

	




}
package Client.Paneler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import Client.BildeBibliotek;
import Client.Handlere.BildeHandler;
import Client.Handlere.mainFrame;
import Other_Resources.SpringUtilities;
import Server.Util;

public class opplastingsPanel extends JPanel{
	private static BildeBibliotek Bibliotek;

	public opplastingsPanel(){
		lagGUI();
	}

	public void lagGUI(){
		setLayout(new GridLayout(4,0));
		setBorder(new EmptyBorder(20, 0, 0, 0) );

		lagOpplastingAvBildeMappe();

		lagOpplastingAvEnkeltBilde();

		lagNyKategoriPanel();
	}

	/*
	 * NORTHLAYER GUI
	 * GUI kode for å laste opp en hel mappe med bilder.
	 */
	public void lagOpplastingAvBildeMappe(){
		final JPanel northLayer = new JPanel();
		northLayer.setBorder(BorderFactory.createTitledBorder("Last opp bilde mappe"));

		northLayer.setLayout(new SpringLayout());
		add(northLayer);

		//OPPLASTINGS LABELS
		JLabel MbildeNavnLbl = new JLabel("Mappe Navn:", JLabel.LEFT);
		northLayer.add(MbildeNavnLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField MbildeNavnFelt = new JTextField(30);
		northLayer.add(MbildeNavnFelt);

		//OPPLASTINGS LABELS
		JLabel MKategoriLbl = new JLabel("Kategori:", JLabel.LEFT);
		northLayer.add(MKategoriLbl);

		//OPPLASTINGS TEKSTFELT
		String getCategories = mainFrame.Server.getAllCategories();
		String Kategorier[][] = Util.toTable(getCategories);
		String tempKat[] = new String[Kategorier.length];
		if(!getCategories.equals("null")){
			for(int i = 0; i < Kategorier.length; i++){
				String Category = Kategorier[i][1];
				tempKat[i] = Category;
			}
		}
		final JComboBox kategoriCombo = new JComboBox(tempKat);
		northLayer.add(kategoriCombo);


		//OPPLASTINGS LABELS
		JLabel MfilLbl = new JLabel("Fil sti:", JLabel.LEFT);
		northLayer.add(MfilLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField MfilStiFelt = new JTextField(30);
		northLayer.add(MfilStiFelt);

		JButton MVelgBildeBtn = new JButton("Velg Mappe");
		MVelgBildeBtn.setPreferredSize(new Dimension(100,50));
		MVelgBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Velg bilde mappe
				JFileChooser bildeVelger = new JFileChooser();
				bildeVelger.setCurrentDirectory(new java.io.File("."));
				bildeVelger.setDialogTitle("Velg bildemappe å laste opp");
				bildeVelger.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				bildeVelger.setAcceptAllFileFilterUsed(false);

				if (bildeVelger.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File filSti =  bildeVelger.getSelectedFile();
					MfilStiFelt.setText(filSti.toString());
					MbildeNavnFelt.setText(filSti.getName());
				} 
			}
		});
		northLayer.add(MVelgBildeBtn);

		JButton MLastOppBildeBtn = new JButton("Last Opp Mappe");
		MLastOppBildeBtn.setPreferredSize(new Dimension(100,50));
		MLastOppBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(MbildeNavnFelt.getText() != "" && MfilStiFelt.getText() != ""){
					BildeHandler BH = new BildeHandler(new File(MfilStiFelt.getText()));
					final Vector<String> bildeNavn = BH.lastBilderFraMappe();


					Runnable runner = new Runnable()
					{
						public void run() {

							//Hent kategori ID'en
							String getCategories = mainFrame.Server.getAllCategories();
							String Kategorier[][] = Util.toTable(getCategories);
							String KatID = "";
							if(!getCategories.equals("null")){
								for(int i = 0; i < Kategorier.length; i++){
									if(Kategorier[i][1].contains(kategoriCombo.getSelectedItem().toString())){
										KatID = Kategorier[i][0];
									}
								}
							}

							for(int i = 0; i < bildeNavn.size(); i++){
								String realPath = MfilStiFelt.getText() + "\\" + bildeNavn.get(i);
								mainFrame.Server.uploadImage(realPath, bildeNavn.get(i), KatID, MbildeNavnFelt.getText());
								mainFrame.nyeBilder = true;
							}
							MfilStiFelt.setText("");
							MbildeNavnFelt.setText("");
							mainFrame.nyeBilder = true;
						}
					};
					Thread t = new Thread(runner, "Code Executer");
					t.start();
				}
			}
		});
		northLayer.add(MLastOppBildeBtn);

		SpringUtilities.makeCompactGrid(northLayer,
				4, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad
	}

	/*
	 * MAINLAYER GUI
	 * GUI Kode for opplasting av enkelt filer
	 */
	public void lagOpplastingAvEnkeltBilde(){
		JPanel mainLayer = new JPanel();
		mainLayer.setBorder(BorderFactory.createTitledBorder("Last opp enkelt bilder"));
		mainLayer.setLayout(new SpringLayout());
		add(mainLayer);

		//OPPLASTINGS LABELS
		JLabel bildeNavnLbl = new JLabel("Bilde Navn:", JLabel.LEFT);
		mainLayer.add(bildeNavnLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField bildeNavnFelt = new JTextField(30);
		mainLayer.add(bildeNavnFelt);

		//OPPLASTINGS LABELS
		JLabel KategoriLbl = new JLabel("Kategori:", JLabel.LEFT);
		mainLayer.add(KategoriLbl);

		//OPPLASTINGS TEKSTFELT
		String getCategories = mainFrame.Server.getAllCategories();
		String Kategorier[][] = Util.toTable(getCategories);
		String tempKat[] = new String[Kategorier.length];
		if(!getCategories.equals("null")){
			for(int i = 0; i < Kategorier.length; i++){
				String Category = Kategorier[i][1];
				tempKat[i] = Category;
			}
		}

		final JComboBox kategoriCombo = new JComboBox(tempKat);
		mainLayer.add(kategoriCombo);

		//OPPLASTINGS LABELS
		JLabel filLbl = new JLabel("Fil sti:", JLabel.LEFT);
		mainLayer.add(filLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField filStiFelt = new JTextField(30);
		mainLayer.add(filStiFelt);

		JButton VelgBildeBtn = new JButton("Velg bilde");
		VelgBildeBtn.setPreferredSize(new Dimension(100,50));
		VelgBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Velg bilde mappe
				JFileChooser bildeVelger = new JFileChooser();
				bildeVelger.setCurrentDirectory(new java.io.File("."));
				bildeVelger.setDialogTitle("Velg bilde å laste opp");
				bildeVelger.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				bildeVelger.setAcceptAllFileFilterUsed(false);

				if (bildeVelger.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File filSti =  bildeVelger.getSelectedFile();
					filStiFelt.setText(filSti.toString());
					bildeNavnFelt.setText(filSti.getName());
				} 
			}
		});
		mainLayer.add(VelgBildeBtn);

		JButton LastOppBildeBtn = new JButton("Last Opp Bilde");
		LastOppBildeBtn.setPreferredSize(new Dimension(100,50));
		LastOppBildeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(bildeNavnFelt.getText() != "" && filStiFelt.getText() != ""){
					//Hent kategori ID'en
					String getCategories = mainFrame.Server.getAllCategories();
					String Kategorier[][] = Util.toTable(getCategories);
					String KatID = "";
					if(!getCategories.equals("null")){
						for(int i = 0; i < Kategorier.length; i++){
							if(Kategorier[i][1].contains(kategoriCombo.getSelectedItem().toString())){
								KatID = Kategorier[i][0];
							}
						}
					}

					mainFrame.Server.uploadImage(filStiFelt.getText(), bildeNavnFelt.getText(), KatID, "");
					mainFrame.nyeBilder = true;
				}
			}
		});
		mainLayer.add(LastOppBildeBtn);

		SpringUtilities.makeCompactGrid(mainLayer,
				4, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6); 
	}

	/*
	 * LEGG TIL NY KATEGORI
	 */
	public void lagNyKategoriPanel(){
		JPanel kategoriLayer = new JPanel();
		kategoriLayer.setBorder(BorderFactory.createTitledBorder("Legg til ny kategori"));
		kategoriLayer.setLayout(new FlowLayout());
		add(kategoriLayer);

		//OPPLASTINGS LABELS
		JLabel kategoriNavnLbl = new JLabel("Kategori navn: ", JLabel.LEFT);
		kategoriLayer.add(kategoriNavnLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField kategoriNavnFelt = new JTextField(30);
		kategoriLayer.add(kategoriNavnFelt);

		//OPPLASTINGS LABELS
		JLabel kategoriDescLbl = new JLabel("Kategori beskrivelse: ", JLabel.LEFT);
		kategoriLayer.add(kategoriDescLbl);

		//OPPLASTINGS TEKSTFELT
		final JTextField kategoriDescFelt = new JTextField(30);
		kategoriLayer.add(kategoriDescFelt);

		JButton leggTilKategoriBtn = new JButton("Legg til");
		leggTilKategoriBtn.setPreferredSize(new Dimension(100,50));
		leggTilKategoriBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Legg til kategori
				mainFrame.Server.createCategory(kategoriNavnFelt.getText(), kategoriDescFelt.getText());
				
				//Oppdater GUI i opplastingsPanel
				removeAll();
				lagGUI();
				revalidate();
				repaint();
				
				//Oppdater GUI i HoverMenyPanel
				Bibliotek.menuPanel.removeAll();
				Bibliotek.menuPanel.lagGUI();
				Bibliotek.menuPanel.revalidate();
				Bibliotek.menuPanel.repaint();
			}
		});
		kategoriLayer.add(leggTilKategoriBtn);
	}

	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}
}

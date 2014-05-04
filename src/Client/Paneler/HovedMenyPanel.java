package Client.Paneler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.BildeBibliotek;
import Client.Handlere.BildeHandler;
import Client.Handlere.MetaDataHandler;
import Client.Handlere.mainFrame;
import Server.Util;

public class HovedMenyPanel extends JPanel{
	private ActionListener sokEtterBilder;
	private JTextField sokField;
	private BildeBibliotek Bibliotek;
	private ActionListener VelgKatListener;
	private JComboBox kategoriCombo;

	public HovedMenyPanel(){
		lagGUI();
	}

	public void lagGUI(){
		setLayout(new BorderLayout());

		lagSokPanel();
		lagKategoriPanel();
	}

	/*
	 * Panelet der du kan sortere etter kategori
	 */
	public void lagKategoriPanel(){
		KatComboKlikkListener();

		JPanel sortByCatPanel = new JPanel();
		sortByCatPanel.setLayout(new FlowLayout());
		add(sortByCatPanel, BorderLayout.EAST);

		JLabel MKategoriLbl = new JLabel("Sorter etter kategori:", JLabel.LEFT);
		sortByCatPanel.add(MKategoriLbl);

		String getCategories = mainFrame.Server.getAllCategories();
		String Kategorier[][] = Util.toTable(getCategories);
		String tempKat[] = new String[Kategorier.length];
		if(!getCategories.equals("null")){
			for(int i = 0; i < Kategorier.length; i++){
				String Category = Kategorier[i][1];
				tempKat[i] = Category;
			}
		}
		kategoriCombo = new JComboBox(tempKat);
		kategoriCombo.addActionListener(VelgKatListener);
		sortByCatPanel.add(kategoriCombo);
	}

	/*
	 * Klikk listener som kontrollerer hva som sjer når en kategori er valgt
	 */
	public void KatComboKlikkListener(){
		VelgKatListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Hent all kategoriene
				String Kategorier[][] = Util.toTable(mainFrame.Server.getAllCategories());
				String KatID = "";
				String bilderFraKat = "";

				Vector<String> bildeNavnStort = new Vector<String>();
				Vector<String> bildeNavnThumb = new Vector<String>();
				Vector<String> bildeNavnReal = new Vector<String>();
				Vector<String> bildeNavnID = new Vector<String>();

				//Loop gjennom kategoriene for å få tak i rett kategori ID
				for(int i = 0; i < Kategorier.length; i++){
					//Dersom kategorien matcher det elementet som er valgt i kombo boksen
					if(Kategorier[i][1].contains(kategoriCombo.getSelectedItem().toString())){
						KatID = Kategorier[i][0];
						//Hent alle bildene som er i den kategorien
						bilderFraKat = mainFrame.Server.getAllImages(Integer.parseInt(KatID));
						String bildeNavnArray[][] = mainFrame.Utility.toTable(bilderFraKat);

						//hent alt vi trenger for å lage en bildeoversikt
						for(int k = 0; k < bildeNavnArray.length; k++){
							bildeNavnStort.add(bildeNavnArray[k][2]);
							bildeNavnThumb.add(bildeNavnArray[k][3]);
							bildeNavnReal.add(bildeNavnArray[k][1]);
							bildeNavnID.add(bildeNavnArray[k][0]);
						}
					}
				}
				//Lag bildeoversikten med de nye bildene
				Bibliotek.serverPanel.hentBildeNavn(bildeNavnThumb, bildeNavnStort, bildeNavnReal, bildeNavnID);

			}

		};
	}

	/*
	 * Søk panelet
	 */
	public void lagSokPanel(){
		bildeSok();

		JPanel sokPanel = new JPanel();
		add(sokPanel, BorderLayout.WEST);

		JLabel sokLbl = new JLabel("Søk: ");
		sokPanel.add(sokLbl);

		sokField = new JTextField();
		sokField.setPreferredSize(new Dimension(150,25));
		sokPanel.add(sokField);
		
		Image sok;
		JButton sokBtn = new JButton("Søk i bilder");
		try{
			sok = ImageIO.read(getClass().getResource("/Resurser//sok.png"));
			sok = sok.getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
			sokBtn.setIcon(new ImageIcon(sok));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sokBtn.addActionListener(sokEtterBilder);
		sokPanel.add(sokBtn);
	}

	/*
	 * Metode som kontrollerer hva som skjer når en bruker søker
	 */
	public void bildeSok(){
		sokEtterBilder = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Bibliotek.visningPanel.removeAll();
				if(!sokField.getText().equals("")){
					BildeHandler BildeHandle = new BildeHandler();

					Vector<String> bildeNavnStort = new Vector<String>();
					Vector<String> bildeNavnThumb = new Vector<String>();
					Vector<String> bildeNavnReal = new Vector<String>();
					Vector<String> bildeNavnID = new Vector<String>();

					Vector<String> alleBildeNavnStort = BildeHandle.HentBildeNavnFraServer("ImageNavn", "");
					Vector<String> alleBildeNavnThumb = BildeHandle.HentBildeNavnFraServer("ThumbnailNavn", "");
					Vector<String> alleBildeNavnReal = BildeHandle.HentBildeNavnFraServer("RealNavn", "");
					Vector<String> alleBildeNavnID = BildeHandle.HentBildeNavnFraServer("bildeID", "");

					//Loop som sammenligner all metadata med søkeparameteret.
					MetaDataHandler MetaData = new MetaDataHandler();
					for(int k = 0; k < alleBildeNavnThumb.size(); k++){
						MetaData.setBildeNummer(k);
						MetaData.lastInnMetaData();
						String[][] MetaTable = MetaData.returnerMetaTable();
						for(int i = 0; i < MetaTable.length; i++){
							for(int j = 0; j < MetaTable[i].length; j++){
								if(MetaTable[i][j].toLowerCase().contains(sokField.getText().toLowerCase()) && !bildeNavnStort.contains(alleBildeNavnStort.get(k))){
									bildeNavnStort.add(alleBildeNavnStort.get(k));
									bildeNavnThumb.add(alleBildeNavnThumb.get(k));
									bildeNavnReal.add(alleBildeNavnReal.get(k));
									bildeNavnID.add(alleBildeNavnID.get(k));
								}
							}
						}
					}
					//Alle bildene som matcher blir lagt inn i egne vektorer og sendt til BildeOversikt
					Bibliotek.serverPanel.hentBildeNavn(bildeNavnThumb, bildeNavnStort, bildeNavnReal, bildeNavnID);
				}
				//Dersom ingen søkeparametere, vis alle bilder
				else{
					Bibliotek.serverPanel.hentBildeNavn(null, null, null, null);
				}


				Bibliotek.changePanel("One");
			}
		};
	}



	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}

}

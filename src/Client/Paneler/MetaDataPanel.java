package Client.Paneler;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.BildeBibliotek;
import Client.Handlere.MetaDataHandler;
import Client.Handlere.mainFrame;
import Other_Resources.Rating;

public class MetaDataPanel extends JPanel {
	private static CardLayout MetaDataKontroll = new CardLayout();
	private MetaDataHandler MetaData;
	private BildeBibliotek Bibliotek;
	private JTextField tagText;
	private String[][] tag_table;
	
	public MetaDataPanel(){
		lagGUI();
	}

	public void lagGUI(){
		setLayout(MetaDataKontroll);
		setPreferredSize(new Dimension(getWidth(),getHeight()/10));
		
		MetaData = new MetaDataHandler();
		MetaData.lastInnMetaData();
		
		lagMetaPanel();
		lagEndreMetaPanel();
		changePanel("One");
	}
	
	/*
	 * Panelet som viser meta data i HovedMenyPanel(.java)
	 */
	public void lagMetaPanel(){
		JPanel metaPanel = new JPanel();
		metaPanel.setBorder(BorderFactory.createTitledBorder("Bilde Informasjon"));
		metaPanel.setLayout(new GridLayout(1,3));
		add(metaPanel, "One");
		
		//PANELET SOM VISER METADATA

		JLabel bildeDimLbl = new JLabel("Navn: " + MetaData.hentBildeNavn() );
		metaPanel.add(bildeDimLbl);

		JLabel bildeDimLbl2 = new JLabel("Størrelse: " + MetaData.hentBildeWidth() + "x" + MetaData.hentBildeHeight());
		metaPanel.add(bildeDimLbl2);

		//RATING

		JPanel holdRatingPanel = new JPanel();
		holdRatingPanel.setLayout(new FlowLayout());

		Rating ratingPanel = new Rating();

		metaPanel.add(holdRatingPanel);
		JLabel bildeDimLbl3 = new JLabel("Rating:");
		holdRatingPanel.add(bildeDimLbl3);
		holdRatingPanel.add(ratingPanel);

		JPanel tagPanel = new JPanel();
		tagPanel.setLayout(new FlowLayout());


		JLabel tagLbl = new JLabel("Tags: ");
		tagText = new JTextField(10);
		tagText.setEditable(false);

//		tagText = new JLabel();
//		tagText.setMaximumSize(10);

		tag_table = mainFrame.Utility.toTable(mainFrame.Server.getTags(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer)));



		if(tag_table.length != 0 && tag_table[0].length == 2){

			StringBuilder str = new StringBuilder();

			for(int i = 0; i < tag_table.length; i++){
				str.append(tag_table[i][1]+" ");
			}
			String strToString = str.toString();

			tagText.setText(strToString);
		}

		tagPanel.add(tagLbl);

		JButton newtag = new JButton("+");
		newtag.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewTag();
			}

		});

		tagPanel.add(tagText);
		tagPanel.add(newtag);
		metaPanel.add(tagPanel);

		
		JButton endreMetaDataBtn = new JButton("Endre navn");
		endreMetaDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				changePanel("Two");
			}
		});		
		metaPanel.add(endreMetaDataBtn);

	}
	
	/*
	 * Panelet som lar deg endre meta data i HovedMenyPanel(.java)
	 */
	public void lagEndreMetaPanel(){
		JPanel editMetaPanel = new JPanel();
		editMetaPanel.setBorder(BorderFactory.createTitledBorder("Endre metadata detaljer"));
		editMetaPanel.setLayout(new FlowLayout());
		add(editMetaPanel, "Two");
		
		//PANELET SOM LAR DEG ENDRE METADATA
		JLabel metaNameLbl = new JLabel("Navn: ");
		editMetaPanel.add(metaNameLbl);
		final JTextField metaNameField = new JTextField(MetaData.hentBildeNavn());
		editMetaPanel.add(metaNameField);

		/*
		JLabel metaDescLbl = new JLabel("Beskrivelse: ");
		editMetaPanel.add(metaDescLbl);
		final JTextField metaDescField = new JTextField(MetaData.hentBildeBeskrivelse());
		editMetaPanel.add(metaDescField);
		*/
		
		JButton lagreMetaDataBtn = new JButton("Lagre endringer");
		lagreMetaDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "DocumentName", metaNameField.getText());
				//mainFrame.Server.writeMetadata(BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer), "ImageDescription", metaDescField.getText());
				removeAll();
				lagGUI();
			}
		});		
		editMetaPanel.add(lagreMetaDataBtn);
	}
	
	private void createNewTag(){

		String tag = JOptionPane.showInputDialog(this,"Create a new tag!",tagText.getText());

		if(tag == null || tag.isEmpty()){
			return;
		}

		if(tag_table.length != 0 && tag_table[0].length == 2){

			for(int i = 0; i < tag_table.length; i++){
				mainFrame.Server.deleteTag(tag_table[i][0]);
			}
		}

		String bildeid = BildeOversikt.bildeIDVector.get(BildeVisning.bildeNummer);

		tagText.setText(tag);
		mainFrame.Server.createTag(bildeid, tag);				

	}
	
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}
	
	public void changePanel(String ChangeTo){
		MetaDataKontroll.show(this, ChangeTo);
	}
}

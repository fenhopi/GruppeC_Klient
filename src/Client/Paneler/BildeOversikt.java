package Client.Paneler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Client.BildeBibliotek;
import Client.Handlere.BildeHandler;
import Client.Handlere.mainFrame;

public class BildeOversikt extends JPanel {
	private int bildeTeller;
	public static int bildeNummer;
	ActionListener listener;
	private BufferedImage valgtBilde;
	public Vector<String> bildeNavnServerThumb = new Vector<String>();
	static Vector<String> bildeNavnServerStort = new Vector<String>();
	public static Vector<String> bildeNavnReal = new Vector<String>();
	public static Vector<String> bildeIDVector = new Vector<String>();
	public JPanel imageHolder;
	JButton images[];
	BildeHandler BildeHandle;
	BildeBibliotek Bibliotek;

	public BildeOversikt(){
		makeGUI();	
		
		//Null parameterene lar metoden vite at den skal hente alle bildene.
		hentBildeNavn(null, null, null, null);
	}

	public void makeGUI(){
		setLayout(new GridLayout(0,6));

	}

	/*
	 * LASTING OG VISNING AV BILDER
	 * Henter filsti til store bilder og thumbnails.
	 * Laster inn thumbnails fra server, og viser dem i JPanel.
	 */	
	public void lastInnBilder(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				images = new JButton[bildeNavnServerThumb.size()];
				bildeTeller = 0;
				for (int j = 0; j < bildeNavnServerThumb.size(); j++) {
					BufferedImage TmpImage;
					ImageIcon icon;
					TmpImage = BildeHandle.lagBildeIkonServer(bildeNavnServerThumb.get(j));

					//Dersom bildet eksisterer, lag en ny instans av BildeThumbPanel(.java)
					if(TmpImage != null){

						BildeThumbPanel BildeThumb = new BildeThumbPanel(TmpImage, j);
						BildeThumb.setBildeBibliotek(Bibliotek);
						
						add(BildeThumb);
						
						repaint();
						revalidate();
						bildeTeller++;

					}
				}
				repaint();
				revalidate();
			}
		});
	}
	
	/*
	 * Metode som henter de rette bildene basert på hva du spør etter.
	 * Default parameterene null, null, null, null gjør at den henter alle bildene.
	 * Metoden kjører deretter lastInnBilder() metoden som lager bilde thumbnailene i oversikten
	 */
	public void hentBildeNavn(Vector<String> Thumb, Vector<String> Stort, Vector<String> RealNavn, Vector<String> BildeID){
		BildeHandle = new BildeHandler();
		renskBildeOversikt();
		
		//Dersom Default parameter, hent alle bildene
		if(Thumb == null && Stort == null && RealNavn == null && BildeID == null){
			bildeNavnServerThumb = BildeHandle.HentBildeNavnFraServer("ThumbnailNavn", "");
			bildeNavnServerStort = BildeHandle.HentBildeNavnFraServer("ImageNavn", "");
			bildeNavnReal = BildeHandle.HentBildeNavnFraServer("RealNavn", "");
			bildeIDVector = BildeHandle.HentBildeNavnFraServer("bildeID", "");
			lastInnBilder();
		}
		//Dersom annet enn default parameter; bruk de nye parameterene.
		else{
			bildeNavnServerThumb = Thumb;
			bildeNavnServerStort = Stort;
			bildeNavnReal = RealNavn;
			bildeIDVector = BildeID;
			lastInnBilder();
		}
		bildeKlikkListener();
	}

	/*
	 * Trigger Listener som styrer hva som skjer når brukeren trykker på en av "bilde thumbene" i oversikten.
	 */
	public void bildeKlikkListener(){
		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					//Finn ut hvilken knapp som ble trykker
					bildeNummer = Integer.parseInt(((JButton) e.getSource()).getActionCommand());
					
					//Last inn stor versjon av valg bilde
					valgtBilde = BildeHandle.lagBildeIkonServer(bildeNavnServerStort.get(bildeNummer));
					
					//Send bildet og bildenummeret til VisningPanel(.java)
					BildeBibliotek.visningPanel.setBildeNummer(bildeNummer);
					BildeBibliotek.visningPanel.setImg(valgtBilde);
					
					//Skift til BildeVisning panelet
					Bibliotek.changePanel("Two");
					
					Bibliotek.visningPanel.setFocusable(true);
					Bibliotek.visningPanel.requestFocusInWindow();			
				}
			}
		};
	}

	/*
	 * Fjern alle komponenter fra panelet
	 */
	public void renskBildeOversikt(){
		removeAll();
	}

	/*
	 * Få rett instans av BildeBibliotek for å holde kontroll på cardlayouten
	 */
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}


}

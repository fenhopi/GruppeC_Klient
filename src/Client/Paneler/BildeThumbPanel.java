package Client.Paneler;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
import Client.Handlere.mainFrame;

public class BildeThumbPanel extends JPanel{
	private ThumbnailAction thumbAction;
	private BildeBibliotek Bibliotek;
	private int j;
	
	/*
	 * Få bildet og bildeNummer (for i for loopen den er) i fra BildeOversikt.java
	 * Bygger panelet.
	 */
	public BildeThumbPanel(BufferedImage TmpImage, int bildeNummer){
		setBildeNummer(bildeNummer);
		lagThumbAction(TmpImage);
		lagGUI();
	}
		
	
	public void setBildeNummer(int bildeNummer){
		this.j = bildeNummer;
	}
	
	/*
	 *Thumnailaction gjør at vi får en tooltip som viser bildenavn når du hovrer over bildet.
	 *Den endrer også frame tittelen til bildenavnet når brukeren trykker på bildet.
	 */
	public void lagThumbAction(BufferedImage TmpImage){
		ImageIcon icon = new ImageIcon(TmpImage);
		ImageIcon thumbnailIcon = icon;
		thumbAction = new ThumbnailAction(thumbnailIcon, Bibliotek.serverPanel.bildeNavnReal.get(j));
	}
	
	public void lagGUI(){
		//Layout og border som plasserer bilde og Lbl på rett vis
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 0, 10, 0) );

		lagBildeKnapp();
		
		lagNavneLbl();
		
		lagPopupMeny();

	}
	/*
	 *Legg bildenavnet i en JLabel.
	 *Forkort navnet dersom det er lenger enn 14 karakterer
	 */
	public void lagNavneLbl(){
		JLabel bildeNavnLbl;
		if(Bibliotek.serverPanel.bildeNavnReal.get(j).length() > 14){
			bildeNavnLbl = new JLabel(Bibliotek.serverPanel.bildeNavnReal.get(j).substring(0, 14) + "...", SwingConstants.RIGHT);
		}
		else{
			bildeNavnLbl = new JLabel(Bibliotek.serverPanel.bildeNavnReal.get(j), SwingConstants.RIGHT);
		}
		bildeNavnLbl.setBorder(new EmptyBorder(0, 30, 0, 0) );
		
		add(bildeNavnLbl);		
	}
	
	/*
	 *Lag en knapp med bildet som icon
	 *Fjern alle attributtene som gjør at knappen ser ut som en knapp.
	 *Listener kontrollerer hva som skjer når brukeren trykker på knappen.
	 */
	public void lagBildeKnapp(){
		Bibliotek.serverPanel.images[j] = new JButton(thumbAction);
		Bibliotek.serverPanel.images[j].setBorderPainted(false);
		Bibliotek.serverPanel.images[j].setOpaque(false);
		Bibliotek.serverPanel.images[j].setContentAreaFilled(false);
		Bibliotek.serverPanel.images[j].addActionListener(Bibliotek.serverPanel.listener);
		Bibliotek.serverPanel.images[j].setActionCommand(Integer.toString(j));
		
		add(Bibliotek.serverPanel.images[j]);
	}
	
	/*
	 *Popup som kommer når du høyreklikker på bildet
	 *Valg å slette bildet dersom du vil.
	 */
	public void lagPopupMeny(){
		final JPopupMenu popup = new JPopupMenu();
		final int tmpJ = j;
		popup.add(new JMenuItem(new AbstractAction("Slett Bilde") {
			public void actionPerformed(ActionEvent e) {
				String bildeID = Bibliotek.serverPanel.bildeIDVector.get(tmpJ);
				mainFrame.Server.deleteImage(bildeID);
				Bibliotek.serverPanel.hentBildeNavn(null, null, null, null);
			}
		}));

		//Si at popupen skal vises der brukeren klikket på skjermen
		Bibliotek.serverPanel.images[j].addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)){
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	/*
	 * Klasse som lager tooltip med bilde navn når du hovrer over
	 */
	private class ThumbnailAction extends AbstractAction{

		public ThumbnailAction(Icon thumb, String desc){
			//Tooltip som viser bildenavn
			putValue(SHORT_DESCRIPTION, desc);

			//Thumbnail iconet
			putValue(LARGE_ICON_KEY, thumb);
		}

		//Setter tittel til bilde navn
		public void actionPerformed(ActionEvent e) {
			//photographLabel.setIcon(displayPhoto);
			Bibliotek.setTitle("Bilde: " + getValue(SHORT_DESCRIPTION).toString());
		}
	}
	
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}

}

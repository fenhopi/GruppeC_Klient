package Client.Paneler;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Bilde_Redigering.BildeFilter;
import Bilde_Redigering.BildeRotasjon;
import Bilde_Redigering.BildeTegning;
import Client.BildeBibliotek;
import Client.Handlere.BildeHandler;
import Client.Handlere.MetaDataHandler;
import Client.Handlere.mainFrame;
import Other_Resources.Rating;

public class BildeVisning extends JPanel{
	public static BufferedImage AktivtBilde;
	public static int bildeNummer;
	public static int bildeOrientering;
	public static JPanel bildePanel = null;
	private BildeBibliotek Bibliotek;
	
	public BildeVisning(){
		setLayout(new BorderLayout());
		
		//Start listener som sjekker om du prover å navigere bildene med pile tastene
		pilNavigeringListener();
	}
	
	public BildeVisning(BufferedImage Bilde){
		setImg(Bilde);
	}
	
	/*
	 * Metode som setter nytt bilde, kaller tilpassings metoden dersom det er for stort,
	 * deretter sender det videre til LagBildeFramvisningsMetoden
	 */
	public void setImg(BufferedImage Bilde){
		AktivtBilde = Bilde;
		bildeOrientering = 0;
		if(AktivtBilde.getWidth() > getWidth() || AktivtBilde.getHeight() > getHeight()-(getHeight()/8)){
			AktivtBilde = tilpassBilde(AktivtBilde);
		}
		LagBildeFremvisning();
		repaint();
	}
	
	//Sett bildenummer slik at vi vet hvor vi er med hensyn til pilnavigering
	public void setBildeNummer(int bildeNr){
		bildeNummer = bildeNr;
	}
	
	/*
	 * Lage bildePanel som er der bildet blir vist.
	 * Lager en instans av BildeVisningsMeny(.java) for redigering/metadata menyen
	 */
	public void LagBildeFremvisning(){
		final JLabel stortBilde = new JLabel(new ImageIcon(AktivtBilde));

		bildePanel = new JPanel();
		bildePanel.setLayout(new BorderLayout());
		bildePanel.setPreferredSize(new Dimension(getWidth(),(getHeight()/8)*7));
		add(bildePanel);

		bildePanel.add(stortBilde, BorderLayout.CENTER);

		sjekkOmRotert();

		BildeVisningsMeny Meny = new BildeVisningsMeny(getHeight(), getWidth());
		Meny.setBildeBibliotek(Bibliotek);
		add(Meny, BorderLayout.SOUTH);
	}
	
	/*
	 * Metode som sjekker metadataen for om bildet er blitt rotert.
	 * Dersom det er rotert så åpner den en rotert versjon i bildePanel
	 */
	public void sjekkOmRotert(){
		//LAST INN METADATA
		MetaDataHandler MetaHandler = new MetaDataHandler(bildeNummer);
		
		String MetaData = MetaHandler.returnerMetaData();

		String MetaDataTable[][] = MetaHandler.returnerMetaTable();

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
				BildeRotasjon RoterBilde = new BildeRotasjon(AktivtBilde);
				switch(Integer.parseInt(MetaRotation)){
				case 3:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));
					RoterBilde.Rotasjon(180);
					
					bildeOrientering = 180;

					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					validate();
					break;
				case 6:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					RoterBilde.Rotasjon(90);
					
					bildeOrientering = 90;

					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();

					validate();
					break;
				case 8:
					bildePanel.remove(((BorderLayout) minLayout).getLayoutComponent(BorderLayout.CENTER));

					RoterBilde.Rotasjon(-90);

					bildeOrientering = -90;
					
					bildePanel.add(RoterBilde, BorderLayout.CENTER);
					bildePanel.revalidate();
					bildePanel.validate();
					validate();
					break;

				}

			}
		}
	}
	
	//Funksjon som endrer bildet slik at det passer i JPanel
	public BufferedImage tilpassBilde(BufferedImage bilde){
		BufferedImage Bilde = bilde;

		Image tmp = (Image)Bilde;

		int deleRate = 2;

		//Loop så lenge høgden eller bredden av bildet er større enn panelet
		while(Bilde.getWidth() > getWidth() || Bilde.getHeight() > getHeight()-(getHeight()/7)){
			//Dersom bilder er for bredt
			if(Bilde.getWidth() > getWidth()){
				//Regn ut noen faktorer som gjør at vi kan stille høyden og bredden til bildet
				//men fortsatt beholde proporsjonene
				double tilpassingsRate = (double)Bilde.getWidth()/(double)getWidth();
				double nyBildeBredde = (double)Bilde.getWidth()/tilpassingsRate;
				double nyBildeHoegde = (double)Bilde.getHeight()/tilpassingsRate;

				tmp = tmp.getScaledInstance((int)nyBildeBredde, (int)nyBildeHoegde, java.awt.Image.SCALE_SMOOTH);
				Bilde = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);

				Graphics2D bGr = Bilde.createGraphics();
				bGr.drawImage(tmp, 0, 0, null);
				bGr.dispose();
			}
			//Dersom bildet er for høyt
			else{
				//Regn ut noen faktorer som gjør at vi kan stille høyden og bredden til bildet
				//men fortsatt beholde proporsjonene
				double tilpassingsRate = (double)Bilde.getHeight()/(double)(getHeight()-(getHeight()/7));
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
	
	/*
	 * Listener som gjør at vi kan navigere mellom bildene med piletastene
	 */
	public void pilNavigeringListener(){
		//Sørg for at dette panelet har fokus
		setFocusable(true);
		requestFocusInWindow();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				myKeyEvt(e, "keyReleased");
			}

			private void myKeyEvt(KeyEvent e, String text) {
				int key = e.getKeyCode();
				BildeHandler BildeHandle = new BildeHandler();

				//Dersom venstre pil blir klikket og bildet er ikke det første
				if ((key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_LEFT) && bildeNummer > 0)
				{
					//Fjern alle panel i BildeVisning
					removeAll();
					//La bildenummer vite at vi har beveget oss mot venstre
					bildeNummer -= 1;
					//Hent det nye bildet
					AktivtBilde = BildeHandle.lagBildeIkonServer(BildeOversikt.bildeNavnServerStort.get(bildeNummer));
					//Metode som tilpasser bilde størrelsen til panel størrelsen
					if(AktivtBilde.getWidth() > getWidth() || AktivtBilde.getHeight() > getHeight()-(getHeight()/7)){
						AktivtBilde = tilpassBilde(AktivtBilde);
					}
					//Set det nye bildet som aktivt og bygg opp visnings panelet på nytt
					setImg(AktivtBilde);

					revalidate();
					validate();
				}
				//Dersom høgre pil blir klikket og bildet er ikke det siste
				else if ((key == KeyEvent.VK_KP_RIGHT || key == KeyEvent.VK_RIGHT) && BildeOversikt.bildeNavnServerStort.size() > (bildeNummer+1))
				{
					//Fjern alle panel i BildeVisning
					removeAll();
					//La bildenummer vite at vi har beveget oss mot venstre
					bildeNummer += 1;
					//Hent det nye bildet
					AktivtBilde = BildeHandle.lagBildeIkonServer(BildeOversikt.bildeNavnServerStort.get(bildeNummer));
					//Metode som tilpasser bilde størrelsen til panel størrelsen
					if(AktivtBilde.getWidth() > getWidth() || AktivtBilde.getHeight() > getHeight()-(getHeight()/7)){
						//valgtBilde = tilpassBilde(valgtBilde);
					}
					//Set det nye bildet som aktivt og bygg opp visnings panelet på nytt
					setImg(AktivtBilde);

					revalidate();
					validate();
				}
			}


		});
	}
	
	public void setBildeBibliotek(BildeBibliotek Bibliotek){
		this.Bibliotek = Bibliotek;
	}

}

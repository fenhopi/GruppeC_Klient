package Client.Handlere;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Server.ServerConnector;
import Server.Util;

public class BildeHandler {
	private static File root;
	private static String root2;
	static final String[] filType = new String[]{
		"png", "jpeg", "JPG", "jpg", "bmp", "gif"
	};
	public static Vector<String> bildeNavn = new Vector<String>();

	public BildeHandler(File path){
		root = path;
	}

	public BildeHandler(){
		root2 = "http:/" + mainFrame.Server.getAddress() + "/images/";
	}

	//Filter som kun henter filer med extensions definert i filType array.
	static final FilenameFilter Filter = new FilenameFilter(){
		public boolean accept(final File dir, final String name) {
			for (final String ext : filType) {
				if (name.endsWith("." + ext)) {
					return (true);
				}
			}
			return (false);
		}
	};

	/*
	 * Metode som brukes til å få tak i alle bildenavnene i en mappe.
	 * Brukes under opplasting av en hel mappe
	 */
	public static Vector<String> lastBilderFraMappe(){
		bildeNavn = new Vector<String>();
		if (root.isDirectory()) { // make sure it's a directory
			for (final File Bilde : root.listFiles(Filter)) {
				BufferedImage BildeBuff = null;

				try {
					BildeBuff = ImageIO.read(Bilde);
					String BildeString = Bilde.getName();

					//Legg til bilde navn i Vector
					bildeNavn.add(BildeString);
				} catch (final IOException e) {

				}
			}
		}
		return bildeNavn;
	}

	/*
	 * Metode som henter bilde verdier (Navn, ID, osv) og legger det inn i en vector
	 */
	public Vector<String> HentBildeNavnFraServer(String returVerdi, String Kategori){
		String bilderCSV= mainFrame.Server.getAllImages();
		Vector<String> bilder = new Vector<String>();
		String bildeNavnArray[][] = Util.toTable(bilderCSV);
		String tmpName;
		
		if(Kategori.equals("") && !bilderCSV.equals("null")){
			if(returVerdi.equals("ThumbnailNavn")){
				for(int i=0; i < bildeNavnArray.length; i++){
					tmpName = bildeNavnArray[i][3];
					bilder.add(tmpName);
				}	
			}
			else if(returVerdi.equals("ImageNavn")){
				for(int i=0; i < bildeNavnArray.length; i++){
					tmpName = bildeNavnArray[i][2];
					bilder.add(tmpName);

				}
			}
			else if(returVerdi.equals("RealNavn")){
				for(int i=0; i < bildeNavnArray.length; i++){
					tmpName = bildeNavnArray[i][1];
					bilder.add(tmpName);

				}	
			}
			else if(returVerdi.equals("bildeID")){
				for(int i=0; i < bildeNavnArray.length; i++){
					tmpName = bildeNavnArray[i][0];
					bilder.add(tmpName);

				}	
			}
			else if(returVerdi.equals("KategoriID")){
				for(int i=0; i < bildeNavnArray.length; i++){
					tmpName = bildeNavnArray[i][4];
					bilder.add(tmpName);

				}	
			}
		}
		else{
			
		}

		return bilder;
	}

	/*
	 * Metode som returnerer selve bildet når den får inn navnet som parameter
	 */
	public BufferedImage lagBildeIkonServer(String sti) {
		String bildeSti = sti;
		//System.out.println(bildeSti);
		BufferedImage BildeFraServer =  null;
		BildeFraServer = mainFrame.Server.downloadBuffImage(bildeSti);
		if (BildeFraServer != null) {
			return BildeFraServer;
		}
		return null;
	}
}

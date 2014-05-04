package Client.Handlere;

import java.util.Vector;

import Client.Paneler.BildeOversikt;
import Client.Paneler.BildeVisning;


public class MetaDataHandler {
	String MetaDataTable[][];
	Vector<String> bildeIDVector;
	String MetaData;
	int bildeNummer;
	
	public MetaDataHandler(){
		setBildeNummer(BildeVisning.bildeNummer);
	}
	
	public MetaDataHandler(int bildeNummer){
		setBildeNummer(bildeNummer);
		lastInnMetaData();
	}
	
	/*
	 * Hent inn den seneste metadataen, og legg den i en table
	 */
	public void lastInnMetaData(){
		//LAST INN METADATA
		BildeHandler BildeHandle = new BildeHandler();
		bildeIDVector = BildeHandle.HentBildeNavnFraServer("bildeID", "");
		MetaData = mainFrame.Server.getMetadata(bildeIDVector.get(bildeNummer));

		MetaDataTable = Server.Util.toTable(MetaData);
	}
	
	/*
	 * Sjekker at bildet har de basic meta verdiene som er:
	 * Navn, størrelse, beskrivelse
	 */
	public void basicMetaDataCheck(){
		if(!MetaData.contains("DocumentName")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(BildeVisning.bildeNummer), "DocumentName", BildeOversikt.bildeNavnReal.get(BildeVisning.bildeNummer));
		}
		if(!MetaData.contains("ImageHeight")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(BildeVisning.bildeNummer), "ImageHeight", Integer.toString(BildeVisning.AktivtBilde.getHeight()));
		}
		if(!MetaData.contains("ImageWidth")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(BildeVisning.bildeNummer), "ImageWidth", Integer.toString(BildeVisning.AktivtBilde.getWidth()));
		}
		if(!MetaData.contains("ImageDescription")){
			mainFrame.Server.writeMetadata(bildeIDVector.get(BildeVisning.bildeNummer), "ImageDescription", "Ingen beskrivelse");
		}
	}
	
	public String returnerMetaData(){
		return MetaData;
	}
	
	public String[][] returnerMetaTable(){
		return MetaDataTable;
	}
	
	public String hentBildeNavn(){
		return MetaDataTable[1][1];
	}
	
	public String hentBildeHeight(){
		return MetaDataTable[3][1];
	}
	
	public String hentBildeWidth(){
		return MetaDataTable[0][1];
	}
	
	public String hentBildeBeskrivelse(){
		return MetaDataTable[2][1];
	}
	
	public void setBildeNummer(int bildeNummer){
		this.bildeNummer = bildeNummer;
	}

}

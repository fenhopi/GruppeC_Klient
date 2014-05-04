package Client.Handlere;

import Server.ServerConnector;
import Server.Util;

public class mainFrame {
	public static ServerConnector Server;
	public static Util Utility;
	public static boolean nyeBilder; 
	
	public mainFrame(){
		Server = new ServerConnector(true);
		Utility = new Util();
		nyeBilder = false;
	}

}

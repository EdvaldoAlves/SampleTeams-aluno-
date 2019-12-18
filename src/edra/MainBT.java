package edra;

import java.net.UnknownHostException;

public class MainBT {

	public static void main(String[] args) throws UnknownHostException {
		BTreeTeam team1 = new BTreeTeam("EDRA");
		BTreeTeam team2 = new BTreeTeam("B");
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
	
}


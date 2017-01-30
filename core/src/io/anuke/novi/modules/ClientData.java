package io.anuke.novi.modules;

import io.anuke.novi.Novi;
import io.anuke.novi.entities.Player;
import io.anuke.ucore.modules.Module;

public class ClientData extends Module<Novi>{
	public Player player;

	public ClientData(){
		player = new Player();
		player.client = true;
		player.addSelf();
	}
}
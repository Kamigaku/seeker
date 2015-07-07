package com.ledouxaurelien.seeker.singleton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.ledouxaurelien.seeker.entity.Player;
import com.ledouxaurelien.seeker.entity.impl.Bullet;
import com.ledouxaurelien.seeker.level.impl.GameLevel;
import com.ledouxaurelien.seeker.message.IMessage;
import com.ledouxaurelien.seeker.screens.GameScreen;
import com.ledouxaurelien.seeker.screens.LobbyScreen;

public class ServerManager {

	private static ServerManager serverManager;
	public Client client;
	public static boolean isLaunched = false;
	public String pseudo;
	public String[] pseudos;
	
	// Clients
	public static HashMap<Integer, Player> players;
	
	public ServerManager() {
		this.pseudo = "Kamigaku";
	}
		
	public static ServerManager getServerManager() {
		if(serverManager == null)
			ServerManager.serverManager = new ServerManager();
		return serverManager;
	}
	
	public void connectToServer(String ipAdress, int port) {
		this.client = new Client();
		this.registerClasses(this.client);
		client.start();
		client.addListener(new Listener() {
						
			public void connected(Connection connection) {
				client.sendTCP("connection|" + pseudo);
			}
			
			public void received (Connection connection, Object object) {
				if(object instanceof String) {
					final String[] msgDatas = ((String)object).split("\\|");
					switch(msgDatas[0]) {
						case "serverMessage":
							switch(msgDatas[1]) {
								case "position":
									Player p = LevelManager.getLevelManager().getPlayer(Integer.parseInt(msgDatas[2]));
									p.updatePosition(Float.parseFloat(msgDatas[3]), Float.parseFloat(msgDatas[4]), 
													 Float.parseFloat(msgDatas[5]), Float.parseFloat(msgDatas[6]));
								break;
								case "projectile":
									Bullet b = new Bullet(); // pb somewhere
									LevelManager.getLevelManager().getLevel().addBody(b.createBody(Float.parseFloat(msgDatas[2]), 
																								   Float.parseFloat(msgDatas[3]), 
																								   Float.parseFloat(msgDatas[4])));
									b.defineVector(Float.parseFloat(msgDatas[5]), Float.parseFloat(msgDatas[6]));
									break;
								case "start":
									Gdx.app.postRunnable(new Runnable() {
								         @Override
								         public void run() {
								        	 ScreenManager.getScreenManger().changeScreen(new GameScreen(new GameLevel(msgDatas[2])));
								         }
									});
									break;
								case "disconnect":
									LevelManager.getLevelManager().removePlayer(new Integer(msgDatas[2]));
									break;
								case "player":
									Gdx.app.postRunnable(new Runnable() {
								         @Override
								         public void run() {
							        	 	LevelManager.getLevelManager().addPlayer(
							        	 			Integer.parseInt(msgDatas[2]), msgDatas[3], msgDatas[4], 
							        	 			Float.parseFloat(msgDatas[5]), Float.parseFloat(msgDatas[6]));
								         }
									});
									break;
							}
						break;
						case "listjoueur":
							pseudos = new String[msgDatas.length - 1];
							for(int i = 1; i < msgDatas.length; i++) {
								pseudos[i - 1] = msgDatas[i];
							}
							break;
						case "chat":
							if(ScreenManager.getScreenManger().getCurrentScreen() instanceof LobbyScreen) {
								((LobbyScreen)ScreenManager.getScreenManger().getCurrentScreen()).addChat(msgDatas[1]);
							}
							break;
					}
					System.out.println("Message du server : " + object);
				}
			}
		});
		try {
			client.connect(5000, InetAddress.getByName(ipAdress), port, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void registerClasses(EndPoint entity) {
		Kryo kryo = entity.getKryo();
		kryo.register(IMessage.class);
	}

	public void sendMessage(Object message) {
		if(this.client != null) {
			client.sendTCP(message);
		}
	}
	
}

package com.ledouxaurelien.seeker.screens;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ledouxaurelien.seeker.constants.Constants;
import com.ledouxaurelien.seeker.singleton.ScreenManager;

public class MultiplayerScreen implements Screen {
	
	
	private Stage stage;
	private Skin uiSkin;
	private TextField joinIpServerField;
	private TextField joinPortServerField;
	public static String messageErreur = "";

	public MultiplayerScreen() {
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		this.uiSkin = new Skin(Gdx.files.internal("hud/uiskin.json"));
		
		Table table = new Table(this.uiSkin);
		table.setFillParent(true);
		stage.addActor(table);
		
		Label lb_servIpLabel = new Label("Adresse IP : ", this.uiSkin);
		Label lb_portServ = new Label("Port du serveur : ", this.uiSkin);
		Label lb_joinServ = new Label("Rejoindre un serveur", this.uiSkin);
		
		Label erreurServ = new Label(MultiplayerScreen.messageErreur, this.uiSkin);

		this.joinIpServerField = new TextField("", this.uiSkin);
		this.joinIpServerField.setText("127.0.0.1");
		this.joinPortServerField = new TextField("", this.uiSkin);
		
		TextButton joinServer = new TextButton("Rejoindre le serveur", this.uiSkin);
		joinServer.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				String ipAdresse = "";
				int port;
				if(!joinIpServerField.getText().equals(""))
					ipAdresse = joinIpServerField.getText();
				if(!joinPortServerField.getText().equals(""))
					port = Integer.parseInt(joinPortServerField.getText());
				else 
					port = 2302;
				// Instancie un nouveau joueur et rejoint le serveur
				LobbyScreen ls = new LobbyScreen(ipAdresse, port, false);
				if(ls.isConnected())
					ScreenManager.getScreenManger().changeScreen(ls);
				else
					ScreenManager.getScreenManger().changeScreen(new MultiplayerScreen());
                return true;
			}
		});
		
		TextButton refreshServer = new TextButton("Recuperer les serveurs", this.uiSkin);
		refreshServer.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				requestServer();
				return false;
			}
		});
		
		table.add(lb_joinServ);
		table.row();
		table.add(lb_servIpLabel);
		table.add(joinIpServerField);
		table.row();
		table.add(lb_portServ);
		table.add(joinPortServerField);
		table.row();
		table.add(joinServer);
		table.row();
		table.add(refreshServer);
		table.row();
		table.add(erreurServ);
	}
	
	public void requestServer() {
		System.out.println("connection");
		final Client masterServer = new Client();
		masterServer.start();
		masterServer.addListener(new Listener() {
			
			public void connected (Connection connection) {
				String message = "request|seeker";
				masterServer.sendTCP(message);
			}
			
			public void received (Connection connection, Object object) {
				System.out.println("j'ai reçu quelque chose " + object.getClass().toString());
				if(object instanceof String) {
					System.out.println(((String)object));
					masterServer.close();
				}
			}
		});
		try {
			System.out.println("Tentative de connexion au master server...");
			masterServer.connect(5000, InetAddress.getByName(Constants.MASTERSERVER_IP), Constants.MASTERSERVER_PORT, Constants.MASTERSERVER_PORT);
			System.out.println("Je suis connecté au master server.");
		} catch (IOException ex) {
			System.out.println("Le serveur n'existe pas");
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		this.stage.dispose();
		this.uiSkin.dispose();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		dispose();		
	}

}

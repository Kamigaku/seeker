package com.ledouxaurelien.seeker.screens;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ledouxaurelien.seeker.entity.Player;
import com.ledouxaurelien.seeker.singleton.ServerManager;

public class LobbyScreen implements Screen {
	
	//private Player player;
	
	private Stage stage;
	private Skin uiSkin;
	private Table allChat;
	private Table chat;
	private List<String> listPseudo;
	private Window parametresWindow;
	private boolean parametreOpen = false;
	
	private HashMap<String, Integer> roles = new HashMap<>();
	
	// Client constructeur
	public LobbyScreen(String ipServer, int port, boolean isHost) {

		ServerManager.getServerManager().connectToServer(ipServer, port);
			
		// UI Stuff
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		this.uiSkin = new Skin(Gdx.files.internal("hud/uiskin.json"));
		
		Table table = new Table(this.uiSkin);
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(table);
		
		this.chat = new Table(this.uiSkin);
		table.addActor(this.chat);
		this.chat.setSize(Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() * 0.9f);
		this.chat.setPosition(0, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.9f));
		
		// Menu haut gauche - Affichage du test
		this.allChat = new Table(this.uiSkin);
		this.allChat.align(Align.bottomLeft);
		this.allChat.setFillParent(true);
		ScrollPane scrollChat = new ScrollPane(this.allChat, this.uiSkin);
		scrollChat.setScrollingDisabled(true, false);
		scrollChat.setFillParent(true);
		scrollChat.setFlickScroll(false);
		this.chat.addActor(scrollChat);
		
		// Menu haut droite - Pseudo
		Table pseudo = new Table(this.uiSkin);
		stage.addActor(pseudo);
		pseudo.setSize(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.8f);
		pseudo.setPosition(Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.8f));
		this.listPseudo = new List<String>(this.uiSkin);
		this.listPseudo.setFillParent(true);
		this.listPseudo.setItems(ServerManager.getServerManager().pseudo);
		ScrollPane scrollPseudo = new ScrollPane(this.listPseudo, this.uiSkin);
		scrollPseudo.setScrollingDisabled(true, false);
		scrollPseudo.setFillParent(true);
		scrollPseudo.setFlickScroll(false);
		pseudo.addActor(scrollPseudo);
		
		// Menu bas gauche - Chat
		Table msgTable = new Table(this.uiSkin);
		msgTable.setSize(Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() * 0.1f);
		msgTable.setPosition(0, 0);
		this.stage.addActor(msgTable);
		final TextField msgText = new TextField("", this.uiSkin);
		msgText.setSize((int)(Gdx.graphics.getWidth() * 0.7f), (int)(Gdx.graphics.getHeight() * 0.1f));
		msgText.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(TextField textField, char c) {
				if(c == '\n' && msgText.getText() != "") {
					String msg = msgText.getText();
					ServerManager.getServerManager().sendMessage("chat|" + msg);
					msgText.setText("");
					addChat(ServerManager.getServerManager().pseudo + " : " + msg);
				}
			}
		});
		TextButton msgSend = new TextButton("Envoyer", this.uiSkin);
		msgSend.setSize((int)(Gdx.graphics.getWidth() * 0.1f), (int)(Gdx.graphics.getHeight() * 0.1f));
		msgSend.setPosition(Gdx.graphics.getWidth() * 0.7f, 0);
		msgSend.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				String msg = msgText.getText();
				ServerManager.getServerManager().sendMessage("chat|" + msg);
				msgText.setText("");
				addChat(ServerManager.getServerManager().pseudo + " : " + msg);
                return true;
			}
		});
		msgTable.addActor(msgText);
		msgTable.addActor(msgSend);
		
		
		// Menu bas droite - Controles
		Table controle = new Table(this.uiSkin);
		controle.setPosition(Gdx.graphics.getWidth() * 0.8f, 0);
		controle.setSize(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.2f);
		controle.align(Align.topLeft);

		// Police
		Label label_police = new Label("Policiers : ", this.uiSkin);
		final Slider slider_police = new Slider(0, 2, 1, false, this.uiSkin);
		final Label label_nbrPolice = new Label("0", this.uiSkin);
		slider_police.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrPolice.setText("" + (int)slider_police.getValue());
				roles.put("Policier", new Integer((int)slider_police.getValue()));
			}
		});
		
		// Detective
		Label label_detective = new Label("Detectives : ", this.uiSkin);
		final Slider slider_detective = new Slider(0, 2, 1, false, this.uiSkin);
		final Label label_nbrDetective = new Label("0", this.uiSkin);
		slider_detective.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrDetective.setText("" + (int)slider_detective.getValue());
				roles.put("Detective", new Integer((int)slider_detective.getValue()));
			}
		});
		
		// Leader
		Label label_leader = new Label("Leader : ", this.uiSkin);
		final Slider slider_leader = new Slider(0, 1, 1, false, this.uiSkin);
		final Label label_nbrLeader = new Label("0", this.uiSkin);
		slider_leader.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrLeader.setText("" + (int)slider_leader.getValue());
				roles.put("Leader", new Integer((int)slider_leader.getValue()));
			}
		});
		
		// Gang
		Label label_gang = new Label("Membres de gang : ", this.uiSkin);
		final Slider slider_gang = new Slider(0, 4, 2, false, this.uiSkin);
		final Label label_nbrGang = new Label("0", this.uiSkin);
		slider_gang.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrGang.setText("" + (int)slider_gang.getValue());
				roles.put("Gang", new Integer((int)slider_gang.getValue()));
			}
		});
		
		// Voyant
		Label label_voyant = new Label("Voyants : ", this.uiSkin);
		final Slider slider_voyant = new Slider(0, 1, 1, false, this.uiSkin);
		final Label label_nbrVoyant = new Label("0", this.uiSkin);
		slider_voyant.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrVoyant.setText("" + (int)slider_voyant.getValue());
				roles.put("Voyant", new Integer((int)slider_voyant.getValue()));
			}
		});
		
		
		// Murderer
		Label label_murderer = new Label("Tueurs : ", this.uiSkin);
		final Slider slider_murderer = new Slider(1, 3, 1, false, this.uiSkin);
		roles.put("Murderer", new Integer((int)slider_murderer.getValue()));
		final Label label_nbrMurderer = new Label("1", this.uiSkin);
		slider_murderer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label_nbrMurderer.setText("" + (int)slider_murderer.getValue());
				roles.put("Murderer", new Integer((int)slider_murderer.getValue()));
			}
		});
		
		this.parametresWindow = new Window("Parametres", this.uiSkin);
		TextButton closeWindow = new TextButton("X", uiSkin);
		closeWindow.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				parametresWindow.remove();
				parametreOpen = false;
				return true;
			}
		});
		parametresWindow.getTitleTable().add(closeWindow).height(parametresWindow.getPadTop() - 4);
		parametresWindow.setMovable(false);
		parametresWindow.add(label_police, slider_police, label_nbrPolice);
		parametresWindow.row();
		parametresWindow.add(label_detective, slider_detective, label_nbrDetective);
		parametresWindow.row();
		parametresWindow.add(label_leader, slider_leader, label_nbrLeader);
		parametresWindow.row();
		parametresWindow.add(label_gang, slider_gang, label_nbrGang);
		parametresWindow.row();
		parametresWindow.add(label_murderer, slider_murderer, label_nbrMurderer);
		parametresWindow.row();
		parametresWindow.add(label_voyant, slider_voyant, label_nbrVoyant);
		parametresWindow.row();
		parametresWindow.pack();
		
		TextButton parametres = new TextButton("Parametres", this.uiSkin);
		parametres.setSize(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.1f);
		parametres.setPosition(0, Gdx.graphics.getHeight() * 0.1f);
		parametres.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!parametreOpen) {
					stage.addActor(parametresWindow);
					parametreOpen = true;
				}
				else {
					parametresWindow.remove();
					parametreOpen = false;
				}
				return true;
			}
		});
		
		TextButton launch = new TextButton("Demarrer", this.uiSkin);
		launch.addListener(new InputListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				String message = "startGame";
				for(Entry<String, Integer> role : roles.entrySet()) {
					message += "|" + role.getKey() + "|" + role.getValue();
				}
				ServerManager.getServerManager().sendMessage(message);
				return true;
			}
			
		});
		launch.setSize(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.1f);
		controle.addActor(parametres);
		controle.row();
		controle.addActor(launch);
		this.stage.addActor(controle);
		
	}
	
	public void addChat(String msgChat) {
		Label msg = new Label(msgChat, this.uiSkin);
		msg.setWrap(true);
		this.allChat.add(msg);
		this.allChat.row();
	}
	
	public boolean isConnected() {
		return ServerManager.getServerManager().client.isConnected();
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
		this.listPseudo.clearItems();
		this.listPseudo.setItems(ServerManager.getServerManager().pseudos);
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		this.stage.dispose();
		this.uiSkin.dispose();
	}

}

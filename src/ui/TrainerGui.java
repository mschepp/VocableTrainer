package ui;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import trainer.JapanischTrainer;
import trainer.JapanischTrainer.japaneseWriting;
import trainer.Vokabeltrainer;

public class TrainerGui extends Application {

	static String UMLAUT_OE = "\u00f6";
	static String UMLAUT_UE = "\u00fc";
	static String UMLAUT_AE = "\u00e4";

	private Label actVoc, solution;
	private Button showSol, check, next;
	private TextField input = new TextField();
	private String dbPath = "C:/Users/Melanie_local/workspaceJava-Photon/VokabelTrainer/Database/vokabeln.sql";
	private String solutionTextDefault = "Solution";

	Vokabeltrainer vocTrainer;

	public TrainerGui() {
		// TODO Auto-generated constructor stub
		this.vocTrainer = new JapanischTrainer(this.dbPath);
		Font font = new Font(20);
		Font font2 = new Font(15);
		Font font3 = new Font(24);
		this.actVoc = new Label(this.vocTrainer.getActVocable());
		this.actVoc.setFont(font3);
		this.solution = new Label(solutionTextDefault);
		this.solution.setFont(font);
		this.showSol = new Button("Zeige L" + UMLAUT_OE + "sung");
		this.showSol.setFont(font2);
		this.check = new Button("Check/L" + UMLAUT_OE + "sung");
		this.check.setFont(font2);
		this.next = new Button("N" + UMLAUT_AE + "chste Vokabel");
		this.next.setFont(font2);
		this.input = new TextField();
		this.input.setFont(font);
		this.input.setAlignment(Pos.CENTER);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		primaryStage.setTitle("Vokabeltrainer");
		BorderPane bPane = new BorderPane();
		Scene scene = new Scene(bPane, 715, 475);
		primaryStage.setMinHeight(475);
		primaryStage.setMinWidth(715);
		this.input.setMinHeight(425 / 3.);

		// menu
		MenuBar menuBar;
		Menu menu, modus, answerMenu, questMenu;
		MenuItem exit, dbAction, reverseItem, kanjiQuest, kanjiAnswer, kanaQuest, kanaAnswer, romajiQuest, romajiAnswer;

		// init menu
		menuBar = new MenuBar();
		menu = new Menu("Men" + UMLAUT_UE);
		modus = new Menu("Modus");

		answerMenu = new Menu("answerModus");
		questMenu = new Menu("askModus");

		// menuitems
		exit = new MenuItem("exit");
		dbAction = new MenuItem("Database");
		reverseItem = new MenuItem("reverse");

		kanaAnswer = new MenuItem("Kana");
		kanaQuest = new MenuItem("Kana");

		kanjiAnswer = new MenuItem("Kanji");
		kanjiQuest = new MenuItem("Kanji");

		romajiAnswer = new MenuItem("Romaji");
		romajiQuest = new MenuItem("Romaji");

		// add menu,items and submenu
		menuBar.getMenus().add(menu);
		menuBar.getMenus().add(modus);

		menu.getItems().add(dbAction);
		menu.getItems().add(exit);

		modus.getItems().add(reverseItem);
		modus.getItems().add(answerMenu);
		modus.getItems().add(questMenu);

		answerMenu.getItems().add(kanaAnswer);
		answerMenu.getItems().add(kanjiAnswer);
		answerMenu.getItems().add(romajiAnswer);

		questMenu.getItems().add(kanaQuest);
		questMenu.getItems().add(kanjiQuest);
		questMenu.getItems().add(romajiQuest);

		bPane.setTop(menuBar);

		// center bPane textfield,buttons label
		GridPane gPane = new GridPane();

		ColumnConstraints colConstrain = new ColumnConstraints();
		colConstrain.setPercentWidth(33.3);
		RowConstraints rowConstrain = new RowConstraints(425 / 3.);
		rowConstrain.setPercentHeight(33);
		gPane.getColumnConstraints().addAll(colConstrain, colConstrain, colConstrain);
		gPane.getRowConstraints().addAll(rowConstrain, rowConstrain, rowConstrain);

		gPane.add(this.actVoc, 1, 0);
		gPane.add(this.showSol, 0, 1);
		gPane.add(this.input, 1, 1);
		gPane.add(this.check, 0, 2);
		gPane.add(this.solution, 1, 2);
		gPane.add(this.next, 2, 2);

		gPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(this.check, HPos.CENTER);
		GridPane.setHalignment(this.solution, HPos.CENTER);
		GridPane.setHalignment(this.actVoc, HPos.CENTER);
		GridPane.setHalignment(this.next, HPos.CENTER);
		GridPane.setHalignment(this.showSol, HPos.CENTER);

		bPane.setCenter(gPane);

		// Eventhandler
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		dbAction.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				showDialog();
			}
		});

		kanaAnswer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusAnswer(japaneseWriting.KANA);
			}
		});

		kanaQuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusQuest(japaneseWriting.KANA);
			}
		});

		kanjiAnswer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusAnswer(japaneseWriting.KANJI);
			}
		});

		kanjiQuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusQuest(japaneseWriting.KANJI);
			}
		});

		romajiAnswer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusAnswer(japaneseWriting.ROMAJI);
			}
		});

		romajiQuest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				setModusQuest(japaneseWriting.ROMAJI);
			}
		});

		this.next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getNext();
			}
		});

		this.showSol.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				showSolution("Gesucht wird:\n" + vocTrainer.getSolution());
			}
		});

		this.check.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				check(input.getText());
			}

		});

		reverseItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				reverse();
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});

		primaryStage.show();

	}

	//
	public void setModusQuest(japaneseWriting mod) {
		if (this.vocTrainer.isGermanSearched()) {
			if ((this.vocTrainer.getActVocInfo()[mod.getIdx()] != null
					&& !this.vocTrainer.getActVocInfo()[mod.getIdx()].equals(""))) {
				this.vocTrainer.setAskId(mod.getIdx());
				this.vocTrainer.setLanguageId(mod.getIdx());
			}
			else {
				errorWindow("Kein Eintrag für den gew" + UMLAUT_AE + "hlten Modus in der Datenbank.", 500, 100);
			}
		} else {
			errorWindow("Deutsch wird gefragt. Deutsch hat keine verschiedene Modi", 500, 100);
		}
		refresh();
	}

	public void setModusAnswer(japaneseWriting mod) {
		if (mod.getIdx() == this.vocTrainer.getAskId()) {
			errorWindow("F" + UMLAUT_UE + "r Antwort und Frage wurde der gleiche Modus gew" + UMLAUT_AE + "hlt. "
					+ "Bitte unterschiedliche Modi w" + UMLAUT_AE + "hlen.", 500, 100);
			return;
		}
		if (mod.getIdx() != -1) {
			if ((this.vocTrainer.getActVocInfo()[mod.getIdx()] != null
					&& !this.vocTrainer.getActVocInfo()[mod.getIdx()].equals(""))) {
				this.vocTrainer.setAnswerId(mod.getIdx());
				this.vocTrainer.setLanguageId(mod.getIdx());
				refresh();
			}
		} else {
			errorWindow("Kein Eintrag für den gew" + UMLAUT_AE + "hlten Modus in der Datenbank.", 500, 100);
		}
	}

	public void resetTextField() {
		this.input.clear();
		this.input.setStyle("-fx-inner-background-color: white;");
	}

	public void refresh() {
		this.actVoc.setText(this.vocTrainer.getActVocable());
	}

	private void showDialog() {
		FileChooser chooseDB = new FileChooser();
		chooseDB.setInitialDirectory(new File("./"));
		File chosen = chooseDB.showOpenDialog(new Stage());
		// is null if no file selected
		if (chosen != null) {
			String newPath = chosen.getPath();
			if (newPath.endsWith(".sql")) {
				this.dbPath = newPath;
				this.vocTrainer = new JapanischTrainer(this.dbPath);
				refresh();
				// System.out.println(newPath);
			} else {
				errorWindow("Wrong file extension");

			}
		}
	}

	public void check(String text) {
		WebView wv = new WebView();
		WebEngine engine = wv.getEngine();
		wv.setBlendMode(BlendMode.MULTIPLY);
		wv.setPrefHeight(60);
		wv.setMinWidth(238);
		if (this.vocTrainer.isRight(text)) {
			engine.loadContent("<html> <center> <font size=4pt>" + this.input.getText()
					+ " ist </font><font size=4pt color=green>Richtig</font> </center></html>");
			this.input.setStyle("-fx-background-color: green;");

		} else {
			if (this.vocTrainer.isPossibleSolution(text)) {
				engine.loadContent("<html> <center> <font size=4pt color=orange>" + this.input.getText()
						+ " ist ok<br> aber nicht gesucht</font> </center></html>");
				this.input.setStyle("-fx-background-color: orange;");
			}
			else {
				engine.loadContent("<html> <center> <font size=4pt>" + text
						+ " ist</font> <font size=4pt color=red>falsch</font> <br>" + " <font size=4pt>"
						+ this.vocTrainer.getSolution() + " ist </font>"
						+ "<font size=4pt color=green>Richtig</font></center></html>");
				this.input.setStyle("-fx-background-color: red;");
			}
		}
		showSolution(wv);
	}

	public void errorWindow(String messages, double width, double height) {
		Text errorMess = new Text(messages);
		BorderPane errorPane = new BorderPane();
		errorPane.setCenter(errorMess);
		Scene errorScene = new Scene(errorPane, width, height);
		Stage errorStage = new Stage();
		errorStage.setTitle("Error");
		errorStage.setScene(errorScene);
		errorStage.setResizable(false);
		errorStage.show();
	}

	public void errorWindow(String messages) {
		errorWindow(messages, 200, 100);
	}

	public void getNext() {
		this.actVoc.setText(this.vocTrainer.getNextVocable());
		this.solution.setText(this.solutionTextDefault);
		this.solution.setContentDisplay(ContentDisplay.TEXT_ONLY);
		resetTextField();
	}

	public void showSolution(String text) {
		this.solution.setText(text);
		this.solution.setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	public void showSolution(WebView formText) {
		this.solution.setGraphic(formText);
		this.solution.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}

	/**
	 * @TODO need to be implemented
	 */
	public void reverse() {
		this.vocTrainer.reverse();
		refresh();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}

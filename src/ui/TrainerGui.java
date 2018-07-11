package ui;

import java.io.File;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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
import trainer.LanguageModi;
import trainer.Vokabeltrainer;

public class TrainerGui extends Application {

	static String UMLAUT_OE = "\u00f6";
	static String UMLAUT_UE = "\u00fc";
	static String UMLAUT_AE = "\u00e4";

	private Label actVoc, solution;
	private Button showSol, check, next;
	private TextField input = new TextField();
	private String dbPath = System.getProperty("user.dir") + "/Database/vokabeln.sql";
	private String solutionTextDefault = "Solution";
	private int col2Width = 255;

	private String kanaTxt = "Kana";
	private String romajiTxt = "Romaji";
	private String kanjiTxt = "Kanaji";
	private String germanTxt = "Deutsch";
	private String txtActAnswerBttn = germanTxt;
	private String txtActQuestBttn = kanaTxt;

	private HashMap<String, RadioMenuItem> modiQuestBttn = new HashMap<>();
	private HashMap<String, RadioMenuItem> modiAnswerBttn = new HashMap<>();

	Vokabeltrainer vocTrainer;

	public TrainerGui() {
		// TODO Auto-generated constructor stub
		this.vocTrainer = new JapanischTrainer(this.dbPath);
		Font font = new Font(20);
		Font font2 = new Font(15);
		// Font font3 = new Font(24);
		Font font3 = Font.font("Meiryo", 20);
		Font font4 = Font.font("Meiryo", 16);
		this.actVoc = new Label(this.vocTrainer.getAskedInformation());
		this.actVoc.setFont(font3);
		this.solution = new Label(solutionTextDefault);
		this.solution.setFont(font4);
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
		this.input.setMinWidth(col2Width);

		// menu
		MenuBar menuBar;
		Menu menu, modus, questMenu, answerMenu;
		MenuItem exit, dbAction, reverseItem;
		RadioMenuItem kanaQuest, kanjiQuest, romajiQuest, germanQuest, kanaAnswer, kanjiAnswer, romajiAnswer,
				germanAnswer;
		ToggleGroup questMod, answerMod;

		// init menu
		menuBar = new MenuBar();
		menu = new Menu("Men" + UMLAUT_UE);
		modus = new Menu("Modus");
		questMenu = new Menu("Frage Modi");
		answerMenu = new Menu("Antwort Modi");

		// menuitems
		exit = new MenuItem("exit");
		dbAction = new MenuItem("Database");
		reverseItem = new MenuItem("reverse");

		questMod = new ToggleGroup();
		answerMod = new ToggleGroup();

		kanaQuest = new RadioMenuItem(kanaTxt);
		kanaQuest.setToggleGroup(questMod);
		kanaQuest.setSelected(true);

		kanjiQuest = new RadioMenuItem(kanjiTxt);
		kanjiQuest.setToggleGroup(questMod);

		romajiQuest = new RadioMenuItem(romajiTxt);
		romajiQuest.setToggleGroup(questMod);

		germanQuest = new RadioMenuItem(germanTxt);
		germanQuest.setToggleGroup(questMod);

		kanaAnswer = new RadioMenuItem(kanaTxt);
		kanaAnswer.setToggleGroup(answerMod);

		kanjiAnswer = new RadioMenuItem(kanjiTxt);
		kanjiAnswer.setToggleGroup(answerMod);

		romajiAnswer = new RadioMenuItem(romajiTxt);
		romajiAnswer.setToggleGroup(answerMod);

		germanAnswer = new RadioMenuItem(germanTxt);
		germanAnswer.setToggleGroup(answerMod);
		germanAnswer.setSelected(true);

		modiAnswerBttn.put(kanaTxt, kanaAnswer);
		modiAnswerBttn.put(kanjiTxt, kanjiAnswer);
		modiAnswerBttn.put(romajiTxt, romajiAnswer);
		modiAnswerBttn.put(germanTxt, germanAnswer);

		modiQuestBttn.put(kanaTxt, kanaQuest);
		modiQuestBttn.put(kanjiTxt, kanjiQuest);
		modiQuestBttn.put(romajiTxt, romajiQuest);
		modiQuestBttn.put(germanTxt, germanQuest);

		// add menu,items and submenu
		menuBar.getMenus().add(menu);
		menuBar.getMenus().add(modus);

		menu.getItems().add(dbAction);
		menu.getItems().add(exit);

		modus.getItems().add(reverseItem);
		modus.getItems().add(questMenu);
		modus.getItems().add(answerMenu);

		answerMenu.getItems().add(kanaAnswer);
		answerMenu.getItems().add(kanjiAnswer);
		answerMenu.getItems().add(romajiAnswer);
		answerMenu.getItems().add(germanAnswer);

		questMenu.getItems().add(kanaQuest);
		questMenu.getItems().add(kanjiQuest);
		questMenu.getItems().add(romajiQuest);
		questMenu.getItems().add(germanQuest);

		bPane.setTop(menuBar);

		// center bPane textfield,buttons label
		GridPane gPane = new GridPane();

		ColumnConstraints colConstrain = new ColumnConstraints(col2Width - 50);
		ColumnConstraints col2Constrain = new ColumnConstraints(col2Width);
		RowConstraints rowConstrain = new RowConstraints(425 / 3.);
		rowConstrain.setPercentHeight(33);
		gPane.getColumnConstraints().addAll(colConstrain, col2Constrain, colConstrain);
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

		questMod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				// Has selection.

				if (questMod.getSelectedToggle() != null) {
					RadioMenuItem button = (RadioMenuItem) questMod.getSelectedToggle();
					boolean changeWorked = false;
					if (button.getText().equals(kanaTxt))
						changeWorked = setModusQuest(japaneseWriting.KANA);
					else if (button.getText().equals(kanjiTxt))
						changeWorked = setModusQuest(japaneseWriting.KANJI);
					else if (button.getText().equals(romajiTxt))
						changeWorked = setModusQuest(japaneseWriting.ROMAJI);
					else if (button.getText().equals(germanTxt))
						changeWorked = setModusQuest(japaneseWriting.GERMAN);
					if (!changeWorked) {
						ObservableList<Toggle> ol = questMod.getToggles();
						for (int i = 0; i < ol.size(); i++) {
							if (txtActQuestBttn.equals(((RadioMenuItem) ol.get(i)).getText())) {
								((RadioMenuItem) ol.get(i)).setSelected(true);
								break;
							}
						}
					} else
						txtActQuestBttn = button.getText();
				}
			}
		});

		answerMod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				// Has selection.

				if (answerMod.getSelectedToggle() != null) {
					RadioMenuItem button = (RadioMenuItem) answerMod.getSelectedToggle();
					boolean changeWorked = false;
					if (button.getText().equals(kanaTxt))
						changeWorked = setModusAnswer(japaneseWriting.KANA);
					else if (button.getText().equals(kanjiTxt))
						changeWorked = setModusAnswer(japaneseWriting.KANJI);
					else if (button.getText().equals(romajiTxt))
						changeWorked = setModusAnswer(japaneseWriting.ROMAJI);
					else if (button.getText().equals(germanTxt))
						changeWorked = setModusAnswer(japaneseWriting.GERMAN);
					if (!changeWorked) {
						ObservableList<Toggle> ol = answerMod.getToggles();
						for (int i = 0; i < ol.size(); i++) {
							if (txtActAnswerBttn.equals(((RadioMenuItem) ol.get(i)).getText())) {
								((RadioMenuItem) ol.get(i)).setSelected(true);
								break;
							}
						}
					} else
						txtActAnswerBttn = button.getText();
				}
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
	public boolean setModusQuest(japaneseWriting mod) {
		if (mod.getIdx() == this.vocTrainer.getAnswerId()) {
			errorWindow("F" + UMLAUT_UE + "r Antwort und Frage wurde der gleiche Modus gew" + UMLAUT_AE + "hlt. "
					+ "Bitte unterschiedliche Modi w" + UMLAUT_AE + "hlen.", 500, 100);
			return false;
		}
		if (this.vocTrainer.isGermanSearched() || mod.equals(japaneseWriting.GERMAN)) {
			if ((this.vocTrainer.getActVocInfo()[mod.getIdx()] != null
					&& !this.vocTrainer.getActVocInfo()[mod.getIdx()].equals(""))) {
				this.vocTrainer.setAskId(mod.getIdx());
				refresh();
				return true;
			} else {
				errorWindow("Kein Eintrag für den gew" + UMLAUT_AE + "hlten Modus in der Datenbank.", 500, 100);
			}
		} else {
			errorWindow("Deutsch ist gegeben. Deutsch hat keine verschiedene Modi.", 500, 100);
		}
		return false;

	}

	public boolean setModusAnswer(LanguageModi mod) {
		if (mod.getIdx() == this.vocTrainer.getAskId()) {
			errorWindow("F" + UMLAUT_UE + "r Antwort und Frage wurde der gleiche Modus gew" + UMLAUT_AE + "hlt. "
					+ "Bitte unterschiedliche Modi w" + UMLAUT_AE + "hlen.", 500, 100);
			return false;
		}
		if (mod.getIdx() != -1) {
			if ((this.vocTrainer.getActVocInfo()[mod.getIdx()] != null
					&& !this.vocTrainer.getActVocInfo()[mod.getIdx()].equals(""))) {
				this.vocTrainer.setAnswerId(mod.getIdx());
				refresh();
				return true;
			}
		} else {
			errorWindow("Kein Eintrag für den gew" + UMLAUT_AE + "hlten Modus in der Datenbank.", 500, 100);
		}
		return false;
	}

	public void resetTextField() {
		this.input.clear();
		this.input.setStyle("-fx-inner-background-color: white;");
	}

	public void refresh() {
		this.actVoc.setText(this.vocTrainer.getAskedInformation());
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
		wv.setPrefHeight(70);
		wv.setMinWidth(col2Width);
		String html_center = "<html> <center> ";
		String html_font = "<font style=\"font-family: Meiryo\" size=3em ";
		if (this.vocTrainer.isRight(text)) {
			engine.loadContent(html_center + html_font + this.input.getText() + " ist </font>" + html_font
					+ " color=green>Richtig</font> </center></html>");
			this.input.setStyle("-fx-background-color: green;");

		} else {
			if (this.vocTrainer.isPossibleSolution(text)) {
				engine.loadContent(html_center + html_font + "color=orange>" + this.input.getText()
						+ " ist ok<br> aber nicht gesucht</font> </center></html>");
				this.input.setStyle("-fx-background-color: orange;");
			} else {
				engine.loadContent(html_center + html_font + ">" + text + " ist</font> " + html_font
						+ "color=red>falsch</font> <br>" + html_font + ">" + this.vocTrainer.getSolution()
						+ " ist </font>" + html_font + "color=green>Richtig</font></center></html>");
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
		String helpString = this.txtActAnswerBttn;
		this.modiQuestBttn.get(this.txtActQuestBttn).setSelected(false);
		this.modiAnswerBttn.get(this.txtActAnswerBttn).setSelected(false);
		this.modiAnswerBttn.get(this.txtActQuestBttn).setSelected(true);
		this.modiQuestBttn.get(helpString).setSelected(true);
		refresh();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}

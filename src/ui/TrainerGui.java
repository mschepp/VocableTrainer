package ui;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import trainer.JapanischTrainer.japaneseWriting;

public class TrainerGui extends Application {

	private Label actVoc, solution;
	private Button showSol, check, next;
	private TextField input = new TextField();
	private String dbPath = "C:/Users/Melanie_local/workspaceJava-Photon/VokabelTrainer/Database/vokabeln.sql";

	public TrainerGui() {
		// TODO Auto-generated constructor stub
		this.actVoc = new Label("Vokabel");
		this.solution = new Label("Solution");
		this.showSol = new Button("Zeige Lösung");
		this.check = new Button("Check/Lösung");
		this.next = new Button("Nächste Vokabel");
		this.input = new TextField();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		primaryStage.setTitle("Vokabeltrainer");
		BorderPane bPane = new BorderPane();
		Scene scene = new Scene(bPane, 675, 475);
		this.input.setMinHeight(425 / 3.);

		// menu
		MenuBar menuBar;
		Menu menu, modus, answerMenu, questMenu;
		MenuItem exit, dbAction, reverse, kanjiQuest, kanjiAnswer, kanaQuest, kanaAnswer, romajiQuest, romajiAnswer;

		// init menu
		menuBar = new MenuBar();
		menu = new Menu("Men\u00fc");
		modus = new Menu("Modus");

		answerMenu = new Menu("answerModus");
		questMenu = new Menu("askModus");

		// menuitems
		exit = new MenuItem("exit");
		dbAction = new MenuItem("Database");
		reverse = new MenuItem("reverse");

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

		modus.getItems().add(reverse);
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
		gPane.add(this.check, 0, 2);
		gPane.add(this.next, 2, 2);
		gPane.add(this.input, 1, 1);

		gPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(this.check, HPos.CENTER);
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
			public void handle(ActionEvent e) {
				FileChooser chooseDB = new FileChooser();
				File chosen=chooseDB.showOpenDialog(new Stage());
				// is null if no file selected
				if (chosen != null) {
					dbPath = chosen.getPath();
					System.out.println(chosen.getPath());
				}

			}
		});

		kanaAnswer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				setModusQuest(japaneseWriting.KANA);
			}
		});
		

		primaryStage.setScene(scene);
		primaryStage.show();

	}
	

	/**
	 * @TODO need to be implemented
	 * @param mod
	 */
	public void setModusQuest(japaneseWriting mod) {
		
	}

	/**
	 * @TODO need to be implemented
	 * @param mod
	 */
	public void setModusAnswer(japaneseWriting mod) {

	}

	/**
	 * @TODO need to be changed to functional implementation
	 */
	public void refresh() {
		this.actVoc.setText("calledRefresh");
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}

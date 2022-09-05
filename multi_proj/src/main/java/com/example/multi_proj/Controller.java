package com.example.multi_proj;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    private Label welcomeText;
    @FXML
    private Label Stats;
    @FXML
    private TextField ChosenPos;
    @FXML
    private TextField ChosenLetter;
    @FXML
    private Label DisplayedWord;
    @FXML
    private Label description1;
    @FXML
    private Label description2;
    @FXML
    private Button GoButton;
    @FXML
    private ImageView imageBearer;
    @FXML
    private StackPane PossChars;
    @FXML
    private HBox Hb;

    private static final String filePath = new File("").getAbsolutePath();
    private VBox[] VB = null;

    FileInputStream inputStream0 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image0.png");
    private final Image image0 = new Image(inputStream0);
    FileInputStream inputStream1 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image1.png");
    private final Image image1 = new Image(inputStream1);
    FileInputStream inputStream2 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image2.png");
    private final Image image2 = new Image(inputStream2);
    FileInputStream inputStream3 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image3.png");
    private final Image image3 = new Image(inputStream3);
    FileInputStream inputStream4 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image4.png");
    private final Image image4 = new Image(inputStream4);
    FileInputStream inputStream5 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image5.png");
    private final Image image5 = new Image(inputStream5);
    FileInputStream inputStream6 = new FileInputStream(filePath+"\\src\\main\\java\\com\\example\\multi_proj\\images\\image6.png");
    private final Image image6 = new Image(inputStream6);

    public Controller() throws FileNotFoundException {
    }

    private void reset()
    {
        Stats.setText("");
        DisplayedWord.setText("");
        PossChars.setVisible(false);
        ChosenPos.setVisible(false);
        ChosenLetter.setVisible(false);
        description1.setVisible(false);
        description2.setVisible(false);
        GoButton.setVisible(false);
        imageBearer.setImage(image0);
    }

    private void showGameStatus()
    {
        PossChars.setVisible(true);
        ChosenPos.setVisible(true);
        ChosenLetter.setVisible(true);
        description1.setVisible(true);
        description2.setVisible(true);
        GoButton.setVisible(true);
        Hb.setVisible(true);
        String s1 = "The current Dictionary contains "+Integer.toString(HelloApplication.getWordNo())+ " words";
        String s2 = "Your Score: " + Integer.toString(HelloApplication.getPoints()) + " points";
        String s3 = "Percentage of Correct Guesses: " + String.valueOf((int) (100*HelloApplication.getPercentOfSucSuccess()))+'%';
        Stats.setText(s1+"\r\n"+s2+"\r\n"+s3+"\r\n");
        int size = HelloApplication.getSize();

        /*
        String Display = "";
        for (int i = 0; i < size; i++)
        {
            if (i+1 == 1) Display = "Possible Characters for the 1st letter:\r\n";
            else if (i+1 == 2) Display += "Possible Characters for the 2nd letter:\r\n";
            else if (i+1 == 3) Display += "Possible Characters for the 3rd letter: \r\n";
            else Display += "Possible Characters for the " + (i+1) + "th letter:\r\n";
            //Character[] Letters = (HelloApplication.getCharCount(i)).toArray(new Character[0]);
            List<Pair<Character, Float>> Letters = new ArrayList<>();
            Letters = HelloApplication.AllChars(i);
            for (int j = 0; j < Letters.size(); j++)
            {
                Character c = Letters.get(j).getKey();
                Float prob = Letters.get(j).getValue();
                Display += c;
                Display += "(" + String.format("%.2f", prob*100) + "%)";
                if (j != Letters.size()-1) Display += ",";
            }
            Display += "\r\n";
        }
        */
        VB = new VBox[size];
        for (int i = 0; i < size; i++)
        {
            List<String> Display = new ArrayList<>();
            List<Pair<Character, Float>> Letters = new ArrayList<>();
            Letters = HelloApplication.AllChars(i);
            for (Pair<Character, Float> item : Letters)
            {
                String elem = "";
                elem += item.getKey();
                elem += " (" + String.format("%.2f", item.getValue()*100) + ")";
                Display.add(elem);
            }
            ChoiceBox ChB = new ChoiceBox(FXCollections.observableList(Display));
            ChB.setMaxWidth(1);

            Label index = new Label();
            index.setAlignment(Pos.CENTER);
            index.setText("    " + String.valueOf(i+1) + " ");

            VB[i] = new VBox();
            VB[i].setSpacing(2);
            VB[i].setPadding(new Insets(20, 10, 20, 0));
            VB[i].getChildren().addAll(index, ChB);
        }
        Hb = new HBox();
        Hb.setSpacing(5);
        Hb.setPadding(new Insets(20, 10, 20, 0));
        Hb.getChildren().clear();
        Hb.getChildren().addAll(VB);
        Hb.setAlignment(Pos.CENTER);
        Hb.setTranslateY(20);
        PossChars.getChildren().clear();
        PossChars.getChildren().add(Hb);
        PossChars.setAlignment(Pos.CENTER);

        DisplayedWord.setText(String.valueOf(HelloApplication.getFoundWord()));
        DisplayedWord.setFont(Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 20));
        int mistakes = HelloApplication.getMistakes();
        switch(mistakes)
        {
            case 0:
                imageBearer.setImage(image0);
                break;
            case 1:
                imageBearer.setImage(image1);
                break;
            case 2:
                imageBearer.setImage(image2);
                break;
            case 3:
                imageBearer.setImage(image3);
                break;
            case 4:
                imageBearer.setImage(image4);
                break;
            case 5:
                imageBearer.setImage(image5);
                break;
        }
    }

    @FXML
    protected void onHelloButtonClick()
    {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onLetterChosen()
    {
        String Position;
        Character Lett;
        try{
            Position = ChosenPos.getText();
            Lett = ChosenLetter.getText().charAt(0);
        }
        catch(Exception e)
        {return;}
        int Pos = 0;
        try{ Pos = Integer.parseInt(Position); }
        catch (Exception e) { return; }// if the position is not an integer
        if (Lett.equals("") || Position == "") return; // button pressed without input
        if (Pos > HelloApplication.getSize()) return; // invalid word index
        int res = HelloApplication.checkLetter(Pos-1, Lett);
        if (res == 0)
        {
            //System.out.println("fine");
            showGameStatus();
            if (HelloApplication.PlayerWins())
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("You Win!");
                a.setHeaderText("Congratulations!!");
                a.setContentText("You guessed the hidden word!");
                a.showAndWait();
                reset();
                HelloApplication.reset();
            }
        }
        else if (res == -1)
        {
            if (HelloApplication.getMistakes() > 5) // game over
            {
                //System.out.println("Game Over :-(");
                imageBearer.setImage(image6);
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Game Over");
                a.setHeaderText("You Lose :-(");
                a.setContentText("The Hidden Word was: " + HelloApplication.getWord());
                a.showAndWait();
                reset();
                HelloApplication.reset();
            }
            else showGameStatus();
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("The letter at position "+Position+" has already been guessed correctly");
            a.show();
        }
    }

    @FXML
    protected void onStartClick()
    {
        int ret = HelloApplication.initialize();
        if (ret == -1)
        {
            // if we haven't chosen a dictionary yet, the following code is executed
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please chose a dictionary first");
            a.show();
        }
        else
        {
            showGameStatus();
        }
    }

    @FXML
    protected void onLoadClick()
    {
        //Creating nodes
        TextField textField1 = new TextField();
        //textField1.setTranslateX(90);
        Button button = new Button("Submit");
        button.setTranslateX(160);
        button.setTranslateY(75);
        //Creating labels
        Label label1 = new Label("Dictionary ID: ");
        //label1.setTranslateX(75);
        //Setting the message with read data
        Text text = new Text("");
        //Setting font to the label
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10);
        text.setFont(font);
        //text.setTranslateX(60);
        //text.setTranslateY(125);
        text.setFill(Color.BLUE);
        text.maxWidth(580);
        text.setWrappingWidth(580);
        //Adding labels for nodes
        HBox box = new HBox(5);
        box.setPadding(new Insets(25, 5 , 5, 50));
        box.getChildren().addAll(label1, textField1);
        Group root = new Group(box, button, text);
        //Setting the stage
        Scene scene = new Scene(root, 400, 150, Color.LAVENDER);
        Stage stage = new Stage();
        stage.setTitle("Load");
        stage.setScene(scene);
        stage.show();
        //Displaying the message
        button.setOnAction(e -> {
            //Retrieving data
            String dictID = textField1.getText();
            if (dictID == "") return;
            int ret = HelloApplication.loader(dictID);
            stage.close();

            Alert a = new Alert(Alert.AlertType.NONE);
            if (ret == 0)
            {
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("Success");
                a.setContentText("Dictionary Loaded Successfully");
                a.show();
                reset();
                HelloApplication.reset();
            }
            else
            {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("An Exception Occurred");
                a.show();
            }
        });
    }

    @FXML
    protected void terminate() {
        System.exit(0);
    }

    @FXML
    protected void onCreateClick() {
        //Creating nodes
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        Button button = new Button("Submit");
        button.setTranslateX(250);
        button.setTranslateY(75);
        //Creating labels
        Label label1 = new Label("Dictionary ID: ");
        Label label2 = new Label("Open Library ID: ");
        //Setting the message with read data
        Text text = new Text("");
        //Setting font to the label
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10);
        text.setFont(font);
        text.setTranslateX(15);
        text.setTranslateY(125);
        text.setFill(Color.BROWN);
        text.maxWidth(580);
        text.setWrappingWidth(580);
        //Adding labels for nodes
        HBox box = new HBox(5);
        box.setPadding(new Insets(25, 5 , 5, 50));
        box.getChildren().addAll(label1, textField1, label2, textField2);
        Group root = new Group(box, button, text);
        //Setting the stage
        Scene scene = new Scene(root, 595, 150, Color.LAVENDER);
        Stage stage = new Stage();
        stage.setTitle("Create");
        stage.setScene(scene);
        stage.show();
        //Displaying the message
        button.setOnAction(e -> {
            //Retrieving data
            String dictID = textField1.getText();
            String libID  = textField2.getText();
            //System.out.println("DictID =" + dictID + ", LibID =" + libID);
            String ret = Dict.Creator(dictID, libID);
            stage.close();
            Alert a = new Alert(Alert.AlertType.NONE);
            if(ret == "success")
            {
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("Create");
                a.setHeaderText("The Dictionary was Created Successfully");
                a.setContentText("You Can access it through the Load Menu using " + dictID + " as Dictionary ID");
                a.show();
            }
            else
            {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setTitle("Create");
                a.setHeaderText("Error");
                a.setContentText(ret);
                a.show();
            }
        });
    }

    @FXML
    protected void onDictionaryClick()
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Dictionary");
        a.setHeaderText("Stats about the current dictionary");
        a.contentTextProperty();
        a.setContentText(HelloApplication.getDictionary());
        a.show();
    }

    @FXML
    protected void onRoundsClick()
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Rounds");
        a.setContentText(HelloApplication.getRounds());
        a.setHeaderText("Information on the past Rounds");
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.show();
    }

    @FXML
    protected void onSolutionClick()
    {
        //imageBearer.setImage(image6);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Solution");
        if (HelloApplication.getWord() == "")
        {
            a.setHeaderText("There is no active word");
            a.show();
        }
        else
        {
            a.setHeaderText("This was a hard one...");
            a.setContentText("The Hidden Word was: " + HelloApplication.getWord());
            a.showAndWait();
            reset();
            HelloApplication.reset();
        }
    }
}


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Sample for ListView showing twitter information
 *
 * @author tomo
 */
public class TwitterListViewSample extends Application {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private TwitterSearchTask task;
    private TextField searchText = new TextField();
    private Button startButton = new Button("Start search");
    private Button stopButton = new Button("Stop search");
    private Text searchProgressMessage = new Text();
    private ListView<TwitterInfo> listView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(getHBoxSearch(), getSearchProgressMessage(), getHBoxView());
        setAction();
        Scene scene = new Scene(root, 600, 300);
        primaryStage.setTitle("Twitter ListView Sample");
        primaryStage.setScene(scene);
        primaryStage.show();
        searchText.requestFocus();
    }

    private Node getHBoxSearch(){
        HBox node = new HBox();
        node.setPadding(new Insets(10, 10, 0, 10));
        node.setSpacing(10);
        Label label = new Label("search key:");
        searchText.setPrefWidth(200);
        stopButton.setDisable(true);
        node.getChildren().addAll(label, searchText, startButton, stopButton);
        return node;
    }

    private Node getSearchProgressMessage() {
        HBox node = new HBox();
        node.setPadding(new Insets(5, 10, 5, 10));
        searchProgressMessage.setWrappingWidth(500);
        searchProgressMessage.maxHeight(10);
        node.getChildren().addAll(searchProgressMessage);
        return node;
    }

    private Node getHBoxView(){
        HBox node = new HBox();
        node.setPadding(new Insets(0, 10, 10, 10));
        node.setSpacing(10);
        ObservableList<TwitterInfo> list = FXCollections.observableArrayList();
        listView.setItems(list);
        listView.setPrefWidth(600);
        listView.setCellFactory(p -> new TwitterInfoCell());
        node.getChildren().addAll(listView);
        return node;
    }

    private void setAction() {
        EventHandler<KeyEvent> startFire = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                startButton.fire();
            }
        };
        searchText.setOnKeyPressed(startFire);
        startButton.setOnKeyPressed(startFire);
        startButton.setOnAction(this::handleStart);
        EventHandler<KeyEvent> stopFire = e -> {
            if (e.getCode() == KeyCode.ENTER) {
                stopButton.fire();
            }
        };
        stopButton.setOnKeyPressed(stopFire);
        stopButton.setOnAction(this::handleStop);
    }

    private void handleStart(ActionEvent e) {
        if (searchText.getText().isEmpty()) {
            searchText.requestFocus();
            searchProgressMessage.setText("enter the search key");
            searchProgressMessage.setFill(Color.RED);
            return;
        }
        searchProgressMessage.setFill(Color.BLUE);
        listView.getItems().clear();
        task = new TwitterSearchTask(searchText.getText(), listView.getItems());
        searchProgressMessage.textProperty().bind(task.messageProperty());
        executor.submit(task);
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }
    private void handleStop(ActionEvent e) {
       if (task != null){
           task.shutdownStream();
           task.cancel();
       }
       startButton.setDisable(false);
       stopButton.setDisable(true);
       searchProgressMessage.textProperty().unbind();
       searchProgressMessage.setText(null);
    }
 
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        if (task != null) {
            task.shutdownStream();
            task.cancel();
        }
        if (executor != null){
            executor.shutdownNow();
        }
    }
    
}

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * ListCell for TwitterInfo
 *
 * @author tomo
 */
public class TwitterInfoCell extends ListCell<TwitterInfo>{

    @Override
    protected void updateItem(TwitterInfo t, boolean bln) {
        if (t != null){
            setGraphic(getTwitterInfoCell(t));
        }
    }

    private Node getTwitterInfoCell(TwitterInfo info) {
        HBox node = new HBox();
        node.setSpacing(10);
        Image image = new Image(info.getImageURL(), 48, 48, true, false);
        ImageView imageView = new ImageView(image);
        Text name = new Text(info.getName());
        name.setWrappingWidth(450);
        Text text = new Text(info.getText());
        text.setWrappingWidth(450);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(name, text);
        node.getChildren().addAll(imageView, vbox);
        return node;
    }

}

import twitter4j.Status;

/**
 * Twitter information for ListCell
 *
 * @author tomo
 */
public class TwitterInfo {
    private Status status;

    public TwitterInfo(Status status) {
        this.status = status;
    }

    public String getName() {
        return status.getUser().getName();
    }

    public String getText(){
        return status.getText();
    }

    public String getImageURL(){
        return status.getUser().getProfileImageURL();
    }
}

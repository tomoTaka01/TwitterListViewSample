import java.util.function.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.StatusListener;

/**
 * Task of searching twitter information
 *
 * @author tomo
 */
public class TwitterSearchTask extends Task<Void>{
    private String searchText;
    private ObservableList<TwitterInfo> list;
    private TwitterStream stream;

    public TwitterSearchTask(String searchText, ObservableList<TwitterInfo> list) {
        this.searchText = searchText;
        this.list = list;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("searching tweet ...");
        FilterQuery query = new FilterQuery();
        query.track(new String[]{searchText});
        stream = new TwitterStreamFactory(getTwitterConf()).getInstance();
        addListener(p -> list.add(0, new TwitterInfo(p)));
        stream.filter(query);
        return null;
    }

    private void addListener(Consumer<Status> consumer){
        stream.addListener(
        new StatusAdapter(){
            @Override
            public void onStatus(Status status){
               consumer.accept(status);
            }
        });
    }
    private Configuration getTwitterConf() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setDebugEnabled(false);
        builder.setOAuthConsumerKey("xxxxxxxxxxxxxxxxxxxxxx");
        builder.setOAuthConsumerSecret("xxxxxxxxxxxxxxxxxxx");
        builder.setOAuthAccessToken("xxxxxxxxxxxxxxxxxxxxxx");
        builder.setOAuthAccessTokenSecret("xxxxxxxxxxxxxxxx");
        return builder.build();
    }

    public void shutdownStream(){
        if (stream != null){
            stream.shutdown();
        }
    }
}

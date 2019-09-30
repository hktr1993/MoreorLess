
package hikaru.moreorless.Model.Stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links_ {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("follows")
    @Expose
    private String follows;
    @SerializedName("commercial")
    @Expose
    private String commercial;
    @SerializedName("stream_key")
    @Expose
    private String streamKey;
    @SerializedName("chat")
    @Expose
    private String chat;
    @SerializedName("subscriptions")
    @Expose
    private String subscriptions;
    @SerializedName("editors")
    @Expose
    private String editors;
    @SerializedName("teams")
    @Expose
    private String teams;
    @SerializedName("videos")
    @Expose
    private String videos;

    /**
     * 
     * @return
     *     The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The follows
     */
    public String getFollows() {
        return follows;
    }

    /**
     * 
     * @param follows
     *     The follows
     */
    public void setFollows(String follows) {
        this.follows = follows;
    }

    /**
     * 
     * @return
     *     The commercial
     */
    public String getCommercial() {
        return commercial;
    }

    /**
     * 
     * @param commercial
     *     The commercial
     */
    public void setCommercial(String commercial) {
        this.commercial = commercial;
    }

    /**
     * 
     * @return
     *     The streamKey
     */
    public String getStreamKey() {
        return streamKey;
    }

    /**
     * 
     * @param streamKey
     *     The stream_key
     */
    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }

    /**
     * 
     * @return
     *     The chat
     */
    public String getChat() {
        return chat;
    }

    /**
     * 
     * @param chat
     *     The chat
     */
    public void setChat(String chat) {
        this.chat = chat;
    }

    /**
     * 
     * @return
     *     The subscriptions
     */
    public String getSubscriptions() {
        return subscriptions;
    }

    /**
     * 
     * @param subscriptions
     *     The subscriptions
     */
    public void setSubscriptions(String subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     * 
     * @return
     *     The editors
     */
    public String getEditors() {
        return editors;
    }

    /**
     * 
     * @param editors
     *     The editors
     */
    public void setEditors(String editors) {
        this.editors = editors;
    }

    /**
     * 
     * @return
     *     The teams
     */
    public String getTeams() {
        return teams;
    }

    /**
     * 
     * @param teams
     *     The teams
     */
    public void setTeams(String teams) {
        this.teams = teams;
    }

    /**
     * 
     * @return
     *     The videos
     */
    public String getVideos() {
        return videos;
    }

    /**
     * 
     * @param videos
     *     The videos
     */
    public void setVideos(String videos) {
        this.videos = videos;
    }

}

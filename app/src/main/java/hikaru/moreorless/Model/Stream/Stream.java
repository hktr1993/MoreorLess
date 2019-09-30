
package hikaru.moreorless.Model.Stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stream {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("game")
    @Expose
    public String game;
    @SerializedName("viewers")
    @Expose
    private Integer viewers;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("video_height")
    @Expose
    private Integer videoHeight;
    @SerializedName("average_fps")
    @Expose
    private Double averageFps;
    @SerializedName("delay")
    @Expose
    private Integer delay;
    @SerializedName("is_playlist")
    @Expose
    private Boolean isPlaylist;
    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("channel")
    @Expose
    private Channel channel;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The game
     */
    public String getGame() {
        return game;
    }

    /**
     * 
     * @param game
     *     The game
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * 
     * @return
     *     The viewers
     */
    public Integer getViewers() {
        return viewers;
    }

    /**
     * 
     * @param viewers
     *     The viewers
     */
    public void setViewers(Integer viewers) {
        this.viewers = viewers;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The videoHeight
     */
    public Integer getVideoHeight() {
        return videoHeight;
    }

    /**
     * 
     * @param videoHeight
     *     The video_height
     */
    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    /**
     * 
     * @return
     *     The averageFps
     */
    public Double getAverageFps() {
        return averageFps;
    }

    /**
     * 
     * @param averageFps
     *     The average_fps
     */
    public void setAverageFps(Double averageFps) {
        this.averageFps = averageFps;
    }

    /**
     * 
     * @return
     *     The delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * 
     * @param delay
     *     The delay
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * 
     * @return
     *     The isPlaylist
     */
    public Boolean getIsPlaylist() {
        return isPlaylist;
    }

    /**
     * 
     * @param isPlaylist
     *     The is_playlist
     */
    public void setIsPlaylist(Boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }

    /**
     * 
     * @return
     *     The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The _links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * 
     * @return
     *     The preview
     */
    public Preview getPreview() {
        return preview;
    }

    /**
     * 
     * @param preview
     *     The preview
     */
    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    /**
     * 
     * @return
     *     The channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * 
     * @param channel
     *     The channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}

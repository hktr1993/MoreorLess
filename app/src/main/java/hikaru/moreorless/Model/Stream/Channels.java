package hikaru.moreorless.Model.Stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Channels {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("streams")
    private Stream[] stream;
    @SerializedName("_links")
    @Expose
    private Links_ links;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Stream[] getStreams() {
        return stream;
    }

    public void setStreams(Stream[] stream) {
        this.stream = stream;
    }

    public Links_ getLinks() {
        return links;
    }

    public void setLinks(Links_ links) {
        this.links = links;
    }
}

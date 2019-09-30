
package hikaru.moreorless.Model.Stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StreamDataModel {

    @SerializedName("stream")
    @Expose
    public Stream stream;
    @SerializedName("_links")
    @Expose
    public Links__ links;

    /**
     * 
     * @return
     *     The stream
     */
   /* public Stream getStream() {
        return stream;
    }

    /**
     * 
     * @param stream
     *     The stream
     */
   /* public void setStream(Stream stream) {
        this.stream = stream;
    }

    /**
     * 
     * @return
     *     The links
     */
   /* public Links__ getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The _links
     */
   /* public void setLinks(Links__ links) {
        this.links = links;
    }*/

}

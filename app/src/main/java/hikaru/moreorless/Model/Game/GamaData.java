
package hikaru.moreorless.Model.Game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GamaData {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("games")
    @Expose
    private List<Game> games = new ArrayList<Game>();

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
     *     The games
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * 
     * @param games
     *     The games
     */
    public void setGames(List<Game> games) {
        this.games = games;
    }

}

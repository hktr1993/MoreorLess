package hikaru.moreorless.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Streamer implements Parcelable{

    public String name;
    private int follows;
    private String views;
    public String image;

    public Streamer(){}

    public Streamer(String i) {
        this.image = i;
    }

    public Streamer(String n, int f, String v){
        this.name = n;
        this.follows = f;
        this.views = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(follows);
        dest.writeString(views);
        dest.writeString(image);
    }

    public static final Creator CREATOR = new Creator() {
        public Streamer createFromParcel(Parcel in) {
            return new Streamer(in);
        }

        public Streamer[] newArray(int size) {
            return new Streamer[size];
        }
    };

    public Streamer(Parcel in) {
        name = in.readString();
        follows = in.readInt();
        views = in.readString();
        image = in.readString();
    }
}

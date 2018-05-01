package app.com.youtubeapiv3.models;


public class courses {
    private int Id;
    private String Coursename;
    private String About;
    private byte[] Image;
    private String Playlist;

    public courses(int id, String coursename, String about, byte[] image, String playlist) {
        Id = id;
        Image = image;
        Coursename = coursename;
        About = about;
        Playlist = playlist;


    }



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getAbout() {
        return About;
    }

    public String getCoursename() {
        return Coursename;
    }

    public void setCoursename(String coursename) {
        Coursename = coursename;
    }

    public String getPlaylist() {
        return Playlist;
    }

    public void setPlaylist(String playlist) {
        Playlist = playlist;
    }
}

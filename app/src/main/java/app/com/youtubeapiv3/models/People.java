package app.com.youtubeapiv3.models;

public class People {

    private String name;
    private int ImageResourceId ;

    public People(String name , int imageResourceId){
      this.name = name ;
      this.ImageResourceId = imageResourceId ;
    }


    public String getName() {
        return name;
    }
    public int getmImageResourceId(){
        return ImageResourceId;
    }


}

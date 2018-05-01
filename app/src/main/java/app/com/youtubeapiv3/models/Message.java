package app.com.youtubeapiv3.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserId(){
        return  getString("userId");
    }

    public String getBody() {
        return getString("body");
    }

    public String getBodyRecieved() {
        return getString("body recieved");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }
    public void setBody(String body) {
        put("body", body);
    }
}

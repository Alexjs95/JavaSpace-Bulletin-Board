import java.sql.Date;

import net.jini.core.entry.Entry;

public class MessageObject implements Entry {
	public Integer messageID;
	public Integer topicID;
	public String username;
	public String message;
	public String timeStamp;
	
	public MessageObject() {
		
	}
	
	public MessageObject(Integer msgID, Integer topID, String user, String msg, String date) {
		messageID = msgID;
		topicID = topID;
		username = user;
		message = msg;
		timeStamp = date;
	}

	public MessageObject(Integer id, Integer topID) {
		messageID = id;
		topicID = topID;
	}
	
	

}

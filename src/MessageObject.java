import net.jini.core.entry.Entry;

public class MessageObject implements Entry {
	public String topicTitle;
	public String message;
	public Integer messageID;
	public Integer topicID;
	
public MessageObject() {
		
	}
	
	public MessageObject(Integer msgID, Integer topID, String title, String msg) {
		messageID = msgID;
		topicID = topID;
		topicTitle = title;
		message = msg;
	}

	public MessageObject(Integer id, Integer topID) {
		messageID = id;
		topicID = topID;
	}

}

import net.jini.core.entry.Entry;

public class TopicObject implements Entry {
	
	 public String topicOwner;
	 public String topicTitle;
	 public Integer topicID;
	 public String allowPrivateMsg;
	 
	public TopicObject() {
		
	}
	
	public TopicObject(Integer ID, String owner, String title, String pvtMsg) {
		topicID = ID;
		topicOwner = owner;
		topicTitle = title;
		allowPrivateMsg = pvtMsg;
	}
	
	public TopicObject(Integer ID) {
		topicID = ID;
	}
	
	
	
}

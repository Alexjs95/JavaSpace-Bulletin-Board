import net.jini.core.entry.Entry;

public class UserObject implements Entry {
	 public String username;
	 public String password;
	
	 public UserObject() {
		
	 }
	
	 public UserObject(String user, String pass) {
		username = user;
		password = pass;
	 }
	 
	 
	 public UserObject(String user) {
		 username = user;
	 }
	 
}

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import net.jini.core.event.RemoteEventListener;
import net.jini.space.JavaSpace;

public class CreateTopic {
	public static JavaSpace space;
	String user;
	Integer topicID;
	
	JFrame frmCreateTopic;

	JLabel lblTopicName;
	JButton btnCreateTopic;
	JTextField txtTopicName;
	JTextArea txtMessage;
	JCheckBox chkPvtOpts;
	 
	CreateTopic() {
		frmCreateTopic = new JFrame("Create New Topic");
		lblTopicName = new JLabel("Please enter a topic name:");
		txtTopicName = new JTextField(25);
		txtMessage = new JTextArea(15,30);
		chkPvtOpts = new JCheckBox("Check box to disable private messages");
		btnCreateTopic = new JButton("Confirm and Create Topic");
		
		frmCreateTopic.add(lblTopicName);
		frmCreateTopic.add(txtTopicName);
		frmCreateTopic.add(txtMessage);
		frmCreateTopic.add(chkPvtOpts);
		frmCreateTopic.add(btnCreateTopic);
		
		LoggedInUser get = new LoggedInUser();
	 	user = get.getUser();
	 	System.out.println(user + " Logged into CreateTopic");
		
	 	space = SpaceUtils.getSpace(); 
	    if (space == null){ 
	    	System.err.println("Failed to find the javaspace"); 
	        System.exit(1); 
	    } 
	 	
		btnCreateTopic.addActionListener(new ActionListener() { 
		private RemoteEventListener theStub;

		public void actionPerformed(ActionEvent e) { 
			String allowPvtMsg = "false";
			String topicName = txtTopicName.getText();
			String message = txtMessage.getText();
			if (!chkPvtOpts.isSelected()) {
				allowPvtMsg = "true";
			}
			 				  
			System.out.println(user);
			System.out.println(topicName);
			System.out.println(message);
			System.out.println(allowPvtMsg);
			
			
			for (int id = 0; id < 100;id ++) {		// need to get max id in space....?
				try {
					TopicObject tObj = new TopicObject(id);
					TopicObject value = (TopicObject)space.read(tObj, null, 2 * 1000);
					 
					if (value != null) {
						topicID = value.topicID;
						System.out.println("TopicID loop: " + topicID + value.topicTitle);
					} else {
						System.out.println("ID loop: " + id);
						break;
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
			if (topicID != null) {		
				topicID++;
			} else {
				topicID = 0;	
			}
			
			
			
			try {
				TopicObject tObj = new TopicObject(topicID, user, topicName, allowPvtMsg);
				System.out.println ("topicID" + topicID);
	        	space.write(tObj, null, 1000000 * 60 * 3);		
			} catch (Exception e1) {
	            e1.printStackTrace();
	            System.out.println("not writing");
			}
		  
			DateFormat dateFormat = new SimpleDateFormat("HH:mm  dd/MM/yy");
			Date date = new Date();
			
			
			
			
			
			try {
	        	MessageObject mObj = new MessageObject(0, topicID, user, message, dateFormat.format(date));
	        	space.write(mObj, null, 1000000 * 60 * 3);		
			} catch (Exception e2) {
	            e2.printStackTrace();
	            System.out.println("not writing");
	        }	  
			
			 
			
			
		} 
		});
		
		
		frmCreateTopic.setLayout(new FlowLayout());
		frmCreateTopic.setSize(400, 400);
		frmCreateTopic.setVisible(true);
	}
	
	 
	
	
	public static void main(String[] args) throws RemoteException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
            
		new CreateTopic();
	}

	

}

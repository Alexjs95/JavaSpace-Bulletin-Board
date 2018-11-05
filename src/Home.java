import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.*;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class Home {
	public static JavaSpace space;
	private String user;
	
	JComboBox<String>  cbbTopics;
	JLabel lbltopicDetails;
	JButton btnCreateTopic, btnAddMsg, btnAddPvtMsg, btnDelete;
	JTextField txtTopic, txtOwner;
	JTextArea txtTopicMsgs;
	JScrollPane scrollPane;
	
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<Integer> topicIDs = new ArrayList<Integer>();
	
	
	Home() {
		JFrame frmHome = new JFrame("Home Page");
		cbbTopics = new JComboBox<String>();
		btnCreateTopic = new JButton("Create new Topic");
		lbltopicDetails = new JLabel();
		txtTopicMsgs =  new JTextArea(15,30);
		scrollPane = new JScrollPane(txtTopicMsgs);
		btnAddMsg = new JButton("Add Message to Topic");
		btnAddPvtMsg = new JButton("Message Topic Owner");
		btnDelete = new JButton("Delete Topic");

		frmHome.add(cbbTopics);
		frmHome.add(btnCreateTopic);
		frmHome.add(lbltopicDetails);
		frmHome.add(scrollPane);
		frmHome.add(btnAddMsg);
		frmHome.add(btnAddPvtMsg);
		frmHome.add(btnDelete);
		
		lbltopicDetails.setVisible(false);
		
	 	LoggedInUser get = new LoggedInUser();
	 	user = get.getUser();
	 	System.out.println(user + " Logged into home");
	 	
	 	space = SpaceUtils.getSpace(); 
	    if (space == null){ 
	    	System.err.println("Failed to find the javaspace"); 
	    	System.exit(1); 
	    }
	    GetTopics();
	    
	    
	    cbbTopics.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  String selectedTopic = cbbTopics.getSelectedItem().toString();
				  Integer topicID = 0;
				  for (int i = 0; i < titles.size(); i++) {
					  if (titles.get(i) == selectedTopic) {
						  topicID = topicIDs.get(i);
					  }
				  }
				  getMessages(selectedTopic, topicID);
				  lbltopicDetails.setText("Topic: " + selectedTopic + "   Author: "  );
				  lbltopicDetails.setVisible(true);
			  }

			
		});
		
	 	
		btnCreateTopic.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  Createtopic();
			  } 
		});
		
		
		btnAddMsg.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  AddMsg();
			  } 
		});
		
		btnAddPvtMsg.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  AddPvtMsg();
			  } 
		});
		
		btnDelete.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  DeleteTopic();
			  } 
		});
		
		frmHome.setLayout(new FlowLayout());
		frmHome.setSize(400, 400);
		frmHome.setVisible(true);
	}
	
	 public static void main(String[] args) {	
		 new Home();	   
	 }
	 
	 public void GetTopics() {
		System.out.println("GETTOPICS");
		cbbTopics.addItem("");
		int id = 0;
		 while(id < 5) {		// need to get max id in space....?
			 try {
				 TopicObject tObj = new TopicObject(id);
				 TopicObject value = (TopicObject)space.read(tObj, null, 2 * 1000);
				 
				 if (value != null) {
					 String title = value.topicTitle;
					 System.out.println(title);
					 titles.add(value.topicTitle);
					 topicIDs.add(value.topicID);
					 cbbTopics.addItem(title);
					 
					 
				 } else {
					 System.out.println(id);
					 break;
				 }
				 
				 
			 } catch (Exception e3) {
				 e3.printStackTrace();
			 }
			 id ++;
		 } 
	 }
				
	 public void getMessages(String topicTitle, Integer topicID) {
		 System.out.println("Selected topic ID :  " + topicID);
		 for (Integer id = 0; id < 5; id++) {
			try {
				 MessageObject mObj = new MessageObject(id, topicID);
				 MessageObject value =  (MessageObject)space.read(mObj, null, 2*1000);
				 
				 if (value != null) {
					 txtTopicMsgs.append(value.message + "\n");
				 } else {
					 break;
				 }
				
			} catch (Exception e) {
				e.printStackTrace();
			}	 
		 }
		 
		 
		 
	 }
	 
	 public void Createtopic() {
		 new CreateTopic();
	 }
	 
	 public void AddMsg() {
		 
	 }
	 
	 public void AddPvtMsg() {
		 
	 }
	 
	 public void DeleteTopic() {
	 
	 }
	 
	 
	 
	 
}

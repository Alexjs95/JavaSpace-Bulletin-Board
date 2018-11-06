import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace;

public class Home implements RemoteEventListener{
	public static JavaSpace space;
	private String user;
	JFrame frmHome;
	JComboBox<String>  cbbTopics;
	JLabel lbltopicDetails;
	JButton btnCreateTopic, btnAddMsg, btnAddPvtMsg, btnDelete, btnLogout;
	JTextField txtTopic, txtOwner;
	JTable tblTopicMsgs;
	DefaultTableModel model;
	JTextArea txtTopicMsgs;
	JScrollPane scrollPane;
	
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<Integer> topicIDs = new ArrayList<Integer>();
	private RemoteEventListener theStub;

	Home() {
		frmHome = new JFrame("Home Page");
		cbbTopics = new JComboBox<String>();
		btnCreateTopic = new JButton("Create new Topic");
		lbltopicDetails = new JLabel();
		
		model = new DefaultTableModel();
		tblTopicMsgs = new JTable(model);
		//txtTopicMsgs =  new JTextArea(15,30);
		scrollPane = new JScrollPane(tblTopicMsgs);
		btnAddMsg = new JButton("Add Message to Topic");
		btnAddPvtMsg = new JButton("Message Topic Owner");
		btnDelete = new JButton("Delete Topic");
		btnLogout = new JButton("Logout");
		
		scrollPane.setPreferredSize(new Dimension(700,200));
		
		frmHome.add(cbbTopics);
		frmHome.add(btnCreateTopic);
		frmHome.add(lbltopicDetails);
		frmHome.add(scrollPane);
		frmHome.add(btnAddMsg);
		frmHome.add(btnAddPvtMsg);
		frmHome.add(btnDelete);
		frmHome.add(btnLogout);
		
		lbltopicDetails.setVisible(false);
		btnDelete.setEnabled(false);
		
	 	LoggedInUser get = new LoggedInUser();
	 	user = get.getUser();
	 	System.out.println(user + " Logged into home");
	 	
	 	space = SpaceUtils.getSpace(); 
	    if (space == null){ 
	    	System.err.println("Failed to find the javaspace"); 
	    	System.exit(1); 
	    }
	    
	    model.addColumn("Username");
	    model.addColumn("Message");
	    model.addColumn("Time added");
	    
	    GetTopics();
	    
	    cbbTopics.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  String selectedTopic = cbbTopics.getSelectedItem().toString();
				  String author = null;
				  Integer topicID = 0;
				  for (int i = 0; i < titles.size(); i++) {
					  if (titles.get(i) == selectedTopic) {
						  topicID = topicIDs.get(i);
					  }
				  }
				  
				  getMessages(selectedTopic, topicID);
				  
				  try {
					  TopicObject tObj = new TopicObject(topicID);
					  TopicObject value =  (TopicObject)space.read(tObj, null, 2*1000);
						 
						 if (value != null) {
							  author = value.topicOwner;
							 if (value.allowPrivateMsg.equals("false")) {
								 btnAddPvtMsg.setEnabled(false);
							 }
						 } 
						
					} catch (Exception e2) {
						e2.printStackTrace();
					}	 
				  
				  
				  lbltopicDetails.setText("Topic: " + selectedTopic + "   Author: " + author);
				  lbltopicDetails.setVisible(true);
				  if (user.equals(author)) {
					  btnDelete.setEnabled(true);
					  // Later addition: add right click menu to jtable to delete specific messages. Add similar for replies
					  // https://stackoverflow.com/questions/16743427/jtable-right-click-popup-menu
					  
				  }
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
		
		btnLogout.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  
				  LoggedInUser log = new LoggedInUser();
				  log.logout();
				  new login();
				  frmHome.dispose();
			  } 
		});
		
		frmHome.setLayout(new FlowLayout());
		frmHome.setSize(800, 400);
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
						 model.addRow(new Object[]{value.username, value.message, value.timeStamp}); 
						 System.out.println(value.username);
					 } else {
						 break;
					 }
					
				} catch (Exception e) {
					e.printStackTrace();
				}	 
			 }
		 
		 
		 Exporter myDefaultExporter =
		            new BasicJeriExporter(TcpServerEndpoint.getInstance(0),
		                  new BasicILFactory(), false, true);
		
		 
		 
		 try {
	            // register this as a remote object and get a reference to the 'stub'
	            theStub = (RemoteEventListener) myDefaultExporter.export(this);

	            // add the listenermObj
	            MessageObject template = new MessageObject();
	            space.notify(template, null, this.theStub, Lease.FOREVER, null);

	        } catch (Exception e3) {
	            e3.printStackTrace();
	        }
		 
		 
		 
		
		 
		 
		 
	 }

	 public void notify(RemoteEvent ev) {
		 MessageObject template = new MessageObject();
		 try {
			 MessageObject value = (MessageObject)space.read(template, null, Long.MAX_VALUE);
			 model.addRow(new Object[]{value.username, value.message, value.timeStamp}); 
	     } catch (Exception e) {
	            e.printStackTrace();
	     }		
	 }
	 
	 
	 
	 
	 
	 public void Createtopic() {
		 new CreateTopic();
	 }
	 
	 public void AddMsg() {
		 try {
	        	MessageObject mObj = new MessageObject(1, 0, user, "msg1", "34/434");
	        	space.write(mObj, null, 1000000 * 60 * 3);		
	        	System.out.println("user : " + user);
			} catch (Exception e2) {
	            e2.printStackTrace();
	            System.out.println("not writing");
	        }	  
	 }
	 
	 public void AddPvtMsg() {
		 
	 }
	 
	 public void DeleteTopic() {
	 
	 }


	 
	 
	 
	 
}

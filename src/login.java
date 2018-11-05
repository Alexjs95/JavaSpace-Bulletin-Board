import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.*;

import net.jini.space.JavaSpace;

public class login extends JFrame {

	public static JavaSpace space;
	JLabel lblUsername, lblPassword;
	JTextField txtUsername;
	JButton btnLogin, btnRegister;
	JPasswordField txtPassword;
	
	login() {
		JFrame frmLogin = new JFrame("Login");
		lblUsername = new JLabel("Username: ");
		txtUsername = new JTextField(20);
		lblPassword = new JLabel("Password: ");
		txtPassword = new JPasswordField(20);
		btnLogin = new JButton("Login");
		btnRegister = new JButton("Register");
			
		frmLogin.add(lblUsername);
		frmLogin.add(txtUsername);
		frmLogin.add(lblPassword);
		frmLogin.add(txtPassword);
		frmLogin.add(btnLogin);
		frmLogin.add(btnRegister);	
		btnLogin.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				btnLogin();
			} 
		});
		
		
		btnRegister.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  btnRegister();
			  } 
			} );
		
		
		frmLogin.setLayout(new FlowLayout());
		frmLogin.setSize(400, 200);
		frmLogin.setVisible(true);
	}
		
	public static void main(String[] args) {
		new login();
		space = SpaceUtils.getSpace(); 
        if (space == null){ 
          System.err.println("Failed to find the javaspace"); 
          System.exit(1); 
        }
	}
	
	private void btnLogin() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());
		
		if ((username.isEmpty()) && (password.isEmpty())) {
			System.out.println("Enter a username and password.");
		} else if (username.isEmpty()) {
			System.out.println("Enter a username.");
		} else if (password.isEmpty()) {
			System.out.println("Enter a password.");
		} else {
			try {
				UserObject template = new UserObject(username);
				UserObject value = (UserObject)space.readIfExists(template, null, 1000 * 3);
				if (value != null) {
					if ((Objects.equals(value.username, username)) && (Objects.equals(value.password, password))) {
						LoggedInUser set = new LoggedInUser();
						set.setUser(username);
						new Home();
						// Close login form
					}
				} else {
					System.out.println("NOT LOGGED IN");
				}	
			} catch (Exception e) {
        		e.printStackTrace();
			}	
		}
	}
	
	private void btnRegister() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());
	
		if ((username.isEmpty()) && (password.isEmpty())) {
			System.out.println("To register you must enter a username and password.");
		} else if (username.isEmpty()) {
			System.out.println("To register you must enter a username.");
		} else if (password.isEmpty()) {
			System.out.println("To register you must enter a password.");
		} else {
			try {
	        	UserObject uObj = new UserObject(username, password);
	        	space.write(uObj, null, 1000000 * 60 * 3);	
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("not writing");
	        }
		}
	}
}

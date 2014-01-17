package Etape1;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

import javax.swing.JButton;
import javax.swing.JFrame;


public class IrcBis extends JFrame {
	public TextArea		text;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;
	
	public JButton write_button;
	public JButton read_button;
	public JButton writeLock_button;
	public JButton readLock_button;
	public JButton unlock_button;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
		// create the graphical part
		new IrcBis(s);
	}

	public IrcBis(SharedObject s) {
	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data=new TextField(60);
		add(data);
	
		write_button = new JButton("write");
		write_button.addActionListener(new writeListener(this));
		add(write_button);
		write_button.setEnabled(false);
		read_button = new JButton("read");
		read_button.addActionListener(new readListener(this));
		add(read_button);
		read_button.setEnabled(false);
		writeLock_button = new JButton("writeLock");
		writeLock_button.addActionListener(new writeLockListener(this));
		add(writeLock_button);
		readLock_button = new JButton("readLock");
		readLock_button.addActionListener(new readLockListener(this));
		add(readLock_button);
		unlock_button = new JButton("unlock");
		unlock_button.addActionListener(new unlockListener(this));
		add(unlock_button);
		unlock_button.setEnabled(false);
		
		setSize(470,300);
		text.setBackground(Color.black); 
		show();
		
		sentence = s;
	}


class readListener implements ActionListener {
	IrcBis irc;
	public readListener (IrcBis i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// invoke the method
		String s = ((Sentence)(irc.sentence.obj)).read();
		
		// display the read value
		irc.text.append(s+"\n");
	}
}

class writeListener implements ActionListener {
	IrcBis irc;
	public writeListener (IrcBis i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// get the value to be written from the buffer
        String s = irc.data.getText();
		
		// invoke the method
		((Sentence)(irc.sentence.obj)).write(IrcBis.myName+" wrote "+s);
		irc.data.setText("");

	}
}

class writeLockListener implements ActionListener {
	IrcBis irc;
	public writeLockListener (IrcBis i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		irc.writeLock_button.setEnabled(false);
		irc.readLock_button.setEnabled(false);
		irc.write_button.setEnabled(true);
		irc.read_button.setEnabled(false);
		irc.unlock_button.setEnabled(true);

        	// lock the object in write mode
		irc.sentence.lock_write();
	}
}

class readLockListener implements ActionListener {
	IrcBis irc;
	public readLockListener (IrcBis i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		irc.writeLock_button.setEnabled(false);
		irc.readLock_button.setEnabled(false);
		irc.write_button.setEnabled(false);
		irc.read_button.setEnabled(true);
		irc.unlock_button.setEnabled(true);

        	// lock the object in write mode
		irc.sentence.lock_read();
	}
}

class unlockListener implements ActionListener {
	IrcBis irc;
	public unlockListener (IrcBis i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		irc.writeLock_button.setEnabled(true);
		irc.readLock_button.setEnabled(true);
		irc.write_button.setEnabled(false);
		irc.read_button.setEnabled(false);
		irc.unlock_button.setEnabled(false);

        	// lock the object in write mode
		irc.sentence.unlock();
	}
}

}
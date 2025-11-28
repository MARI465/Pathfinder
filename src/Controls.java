import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Controls extends JPanel implements ActionListener{

	
	public enum State{
		GEN,SOLVING,IDLE
	}
	
	private JButton genB;
	private JButton dfsB;
	private JButton bfsB;
	private JTextField text;
	private String userInput = "";
	private State state = State.IDLE;
	
	public Controls() {
		genB = new JButton();
		dfsB = new JButton();
		bfsB = new JButton();
		text = new JTextField();
		genB.addActionListener(this);
		dfsB.addActionListener(this);
		bfsB.addActionListener(this);
		text.addActionListener(this);
		this.add(bfsB);
		this.add(genB);
		this.add(dfsB);
		this.add(text);
		
	}
	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == genB){
			state = State.GEN;
		}
	}

	
}

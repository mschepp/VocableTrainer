package ui;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class TrainerGui  extends JFrame{
	private static final long serialVersionUID = -2439250019021787479L;
	

    private JLabel actVoc,solution;
    private JButton showSol,check,next;
    private JTextField input=new JTextField(20);
	
	
	public TrainerGui() {
		this.setTitle("Vokabeltrainer");
        this.setSize(700,500);
        
        this.actVoc=new JLabel("Vokabel");
        this.solution=new JLabel();

        this.showSol=new JButton("Zeige Lösung");
        this.check=new JButton("Check/Lösung");
        this.next=new JButton("Nächste Vokabel");

        GridLayout layout = new GridLayout(3,3);
        
        this.setLayout(layout);
        
        JPanel[][] panels = new JPanel[3][3];   
		for (int m = 0; m < 3; m++) {
			for (int n = 0; n < 3; n++) {
				panels[m][n] = new JPanel();
				panels[m][n].setLayout(new GridBagLayout());
				this.add(panels[m][n]);
			}
		}
        
		panels[0][1].add(this.actVoc);
		panels[1][0].add(this.showSol);
		panels[1][1].add(this.input);
		panels[1][1].setLayout(new GridLayout(1, 1));
		panels[2][0].add(this.check);
		panels[2][1].add(this.solution);
		panels[2][2].add(this.next);
		
		
		//set window to the center of the screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); 
        
        this.setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrainerGui gui=new TrainerGui();

	}

}

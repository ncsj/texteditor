import  java.awt.event.*;

public class Closer extends WindowAdapter implements ActionListener{
	Closable  target = null;

	public Closer(Closable target){
		this.target = target;
	}

	public void windowClosing(WindowEvent e){
		target.close();
	}

	public void actionPerformed(ActionEvent e){
		target.close();
	}
}

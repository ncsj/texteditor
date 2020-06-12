import  java.awt.*;
import  java.awt.event.*;

public class MessageDialog extends Dialog{
	String	message = "";
	String	detail  = "";
	int		select  = 0;

	public final static int OK		= 1;
	public final static int CANCEL	= 0;
	public final static int YES		= 1;
	public final static int NO		= -1;

	public final static int OKCANCEL	= 2;
	public final static int YESNOCANCEL	= 3;

	int		status	= 0;

	public MessageDialog(Frame frame,String title,String message,String detail,int style){
		super(frame,title,true);
		setLayout(null);

		Label label = new Label(message);
		add(label);
		label.setBounds(80,50,300,20);

		TextArea area = new TextArea();
		add(area);
		area.setBounds(80,80,240,100);

		area.setText(detail);

		// 確認用ボタンの設定
		String [] titles = null;
		String [] titles1 = {"OK"};
		String [] titles2 = {"OK","CANCEL"};
		String [] titles3 = {"YES","NO","CANCEL"};
		if(style == OK){
			titles = titles1;
		}
		else if(style == OKCANCEL){
			titles = titles2;
		}
		else if(style == YESNOCANCEL){
			titles = titles3;
		}

		Button [] buttons = new Button [titles.length];
		for(int i=0;i<buttons.length;i++){
			buttons[i] = new Button(titles[i]);

			add(buttons[i]);
			buttons[i].setBounds(280,200+(i*30),80,30);

			switch( i ){
				case 0:		// OK or YES
					buttons[i].addActionListener((ActionEvent e)->{ status=YES; close(); });
					break;
				case 1:		// NO
					buttons[i].addActionListener((ActionEvent e)->{ status=NO; close(); });
					break;
				case 2:		// CANCEL
					buttons[i].addActionListener((ActionEvent e)->{ status=CANCEL; close(); });
					break;
			}
		}

		Rectangle rect = frame.getBounds();		// 基準となるオーナーウインドウの表示位置を取得
		int x = rect.x + 200;
		int y = rect.y + 100;
		int w = 400;
		int h = 300;
		setBounds(x,y,w,h);
		
		setVisible(true);
	}

	public int getStatus(){
		return this.status;
	}

	void close(){
		setVisible(false);
		dispose();
	}
}


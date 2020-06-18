import  java.awt.*;
import  java.awt.event.*;

class SearchDialog extends Dialog implements Closable{
	Label		label	= new Label("SEARCH STRING");
	TextField	field	= new TextField();

	Label		label2	= new Label("Candidates ...");
	List		list	= new List();

	Button		btn1	= new Button("SEARCH");
	Button		btn2	= new Button("CANCEL");

	TextEditor  owner  = null;
	WordRetriever rtvr = null;

	public SearchDialog(TextEditor owner){
		super(owner,"Search String ...",false);
		this.owner = owner;

		int w = 300;
		int h = 400;
		Rectangle rect = owner.getBounds();
		int x = rect.x + rect.width - w;
		int y = rect.y;
		setBounds(x,y,w,h);

		setLayout(null);
		initGUI();

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				setCandidates();
			}
		});
		addWindowListener(new Closer(this));
		setVisible(true);
	}

	void setCandidates(){
		String text = owner.area.getText();
		StringChecker checker = new StringChecker(text);
		WordCounter [] wlist = checker.listup();

		for(WordCounter wc : wlist){
			list.add(wc.getWord());
		}
	}

	void initGUI(){
		add(label);
		label.setBounds(30,40,120,20);

		add(field);
		field.setBounds(30,60,240,20);

		add(label2);
		label2.setBounds(30,90,150,20);

		add(list);
		list.setBounds(30,110,240,240);
		list.addItemListener((ItemEvent e)->{ selectCandidate(); });

		add(btn1);
		btn1.setBounds(60,360,90,20);
		btn1.addActionListener((ActionEvent e)->{searchString();});
		
		add(btn2);
		btn2.setBounds(150,360,90,20);
		btn2.addActionListener(new Closer(this));
	}

	void searchString(){
		if(this.rtvr == null){
			String text = owner.area.getText();
			this.rtvr = new WordRetriever(text);
		}
		String word = field.getText();

		while(true){
			WordRange range = rtvr.retrieve(word);
			if(range != null){
				owner.area.select(range.begin,range.end+1);
				break;
			}
			else{
				break;
			}
		}
	}

	void selectCandidate(){
		int index = list.getSelectedIndex();
		if(index > -1){
			String item = list.getSelectedItem();
			field.setText(item);
		}
	}

	@Override
	public void close(){
		setVisible(false);
		dispose();
	}
}

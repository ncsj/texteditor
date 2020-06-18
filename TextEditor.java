import  java.awt.*;
import  java.awt.event.*;

import  java.io.*;
import  java.util.Properties;

public class TextEditor extends Frame implements Closable{
	TextArea area = new TextArea();
	MenuBar	mbar = new MenuBar();

	FileManager fileManager = null;

	public TextEditor(){
		// setBounds(1600,0,800,600);
		add("Center",area);

		initMenu();

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				loadProps();
			}
		});

		addWindowListener(new Closer(this));

		setVisible(true);
	}

	void initMenu(){
		{
			Menu menu = new Menu("File");
			String [] items = {"New","-","Open","Save","SaveAs","-","Print","-","Exit"};

			ActionListener  [] listeners = {
				(ActionEvent e)->{newContents();} , // New
				null							  ,	// -
				(ActionEvent e)->{openFile();} ,	// Open
				(ActionEvent e)->{saveFile();} ,	// Save
				(ActionEvent e)->{saveAsFile();} ,	// SaveAs
				null,								// -
				(ActionEvent e)->{print();},		// Print
				null,								// -
				new Closer(this)					// Exit
			};

			// for(String s : items){
			for(int i=0;i<items.length;i++){
				String s = items[i];
				MenuItem item = new MenuItem( s );
				item.addActionListener(listeners[i]);
				menu.add(item);
			}

			mbar.add( menu );
		}
		
		{
			Menu menu = new Menu("Edit");
			String [] items = {"Undo","-","Cut","Copy","Paste"};

			for(String s : items){
				MenuItem item = new MenuItem( s );
				menu.add(item);
			}

			mbar.add( menu );
		}
		
		{
			Menu menu = new Menu("Options");
			String [] items = {"Search","Replace","-","Word List"};

			ActionListener [] listeners = {
				(ActionEvent e)->{ search(); },		// Search
				(ActionEvent e)->{ replace(); },	// Replace
				null,								// null
				(ActionEvent e)->{ wordList(); }	// Word List
			};


			for(int i=0;i<items.length;i++){
				MenuItem item = new MenuItem( items[i] );
				if(listeners[i] != null){
					item.addActionListener(listeners[i]);
				}
				menu.add(item);
			}

			mbar.add( menu );
		}

		setMenuBar(mbar);
	}

	void newContents(){
		MessageDialog dlg = new MessageDialog(this
											,"ALART"
											,"The Text Area will be Clear!"
											,"IF YOU SELECT 'OK',\n THEN TEXT AREA WILL BE CLEAR!"
											,MessageDialog.OKCANCEL);
		
		if(dlg.getStatus() == MessageDialog.OK){
			area.setText("");
			this.fileManager = null;
		}
	}

	void openFile(){
		FileDialog dlg = new FileDialog(this,"File Open ...",FileDialog.LOAD);
		dlg.setVisible(true);

		String dir = dlg.getDirectory();
		String file = dlg.getFile();

		if(dir != null && file != null){
			if(this.fileManager == null){
				try{
					this.fileManager = FileManager.getManager(dir + file);
					loadFile();
				}
				catch(FileManagerException e){
					String err = "File Manager Error";
					String msg = "FileManager Error Detected.";
					String detail = e.toString();
					new MessageDialog(this,err,msg,detail,MessageDialog.OK);
				}
			}
		}
	}

	void loadFile(){
		if(this.fileManager != null){
			try{
				String text = this.fileManager.load();
				this.area.setText(text);
			}
			catch(FileManagerException e){
				String err = "File Manager Error";
				String msg = "FileManager Error Detected.";
				String detail = e.toString();
				MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
			}
		}
	}

	void saveFile(){
		if(this.fileManager != null){
			String text = area.getText();
			try{
				this.fileManager.save(text);
			}
			catch(FileManagerException e){
				String err = "File Manager Error";
				String msg = "FileManager Error Detected.";
				String detail = e.toString();
				MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
			}
		}
		else{
			saveAsFile();
		}
	}

	void saveAsFile(){
		FileDialog dlg = new FileDialog(this,"Save As ...",FileDialog.SAVE);
		dlg.setVisible(true);

		String dir		= dlg.getDirectory();
		String fname	= dlg.getFile();
		
		if(dir != null && fname != null){
			try{
				this.fileManager = FileManager.getManager(dir+fname);
				saveFile();
			}
			catch(FileManagerException e){
				String err = "File Manager Error";
				String msg = "FileManager Error Detected.";
				String detail = e.toString();
				new MessageDialog(this,err,msg,detail,MessageDialog.OK);
			}
		}
	}

	void print(){
		String s = "Print!";
		String msg = "Print is Selected.";
		String detail = "The Function To Print is not Supported.";
		new MessageDialog(this,s,msg,detail,MessageDialog.OK);
	}

	/*
	   プロパティの保存
	*/
	void saveProps(){
		// Rectangle = 矩形
		Rectangle rect = getBounds();

		Properties props = new Properties();
		String sx = String.valueOf(rect.x);
		String sy = String.valueOf(rect.y);
		String sw = String.valueOf(rect.width);
		String sh = String.valueOf(rect.height);

		props.put("x",sx);
		props.put("y",sy);
		props.put("w",sw);
		props.put("h",sh);

		try{
			FileOutputStream fout = new FileOutputStream("bounds.xml");
			props.storeToXML(fout,"BOUNDS OF TEXTEDITOR");
			fout.close();
		}
		catch(FileNotFoundException e){
			String err = "File Not Found";
			String msg = "File Not Found.";
			String detail = e.toString();
			MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
		}
		catch(IOException e){
			String err = "I/O Error";
			String msg = "I/O Error Detected.";
			String detail = e.toString();
			MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
		}

	}

	/*
	   プロパティの読み込み
	*/
	void loadProps(){
		try{
			FileInputStream fin = new FileInputStream("bounds.xml");

			Properties props = new Properties();
			props.loadFromXML(fin);
			fin.close();

			String sx = (String)props.get("x");
			String sy = (String)props.get("y");
			String sw = (String)props.get("w");
			String sh = (String)props.get("h");

			int x = Integer.valueOf(sx).intValue();
			int y = Integer.valueOf(sy).intValue();
			int w = Integer.valueOf(sw).intValue();
			int h = Integer.valueOf(sh).intValue();

			setBounds(x,y,w,h);
		}
		catch(FileNotFoundException e){
			setBounds(0,0,800,600);
		}
		catch(IOException e){
			String err = "I/O Error";
			String msg = "I/O Error Detected.";
			String detail = e.toString();
			MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
		}
	}

	void search(){
		new SearchDialog(this);
	}

	void replace(){
		System.out.println("REPLACE");
	}

	void wordList(){
		String text = area.getText();

		StringChecker checker = new StringChecker( text );
		WordCounter [] wlist = checker.listup();

		StringBuilder sb = new StringBuilder();
		int total = 0;
		for(WordCounter wc : wlist){
			int count = wc.count();

			// System.out.printf("%s : %d\n",wc.getWord(),count);
			String s = String.format("%s : %d\n",wc.getWord(),count);
			sb.append( s );
			
			total = total + count;
		}

		String s = String.format("TOTAL : " + total);
		sb.append( s );

		String message = sb.toString();

		MessageDialog dlg = new MessageDialog(this,
								"Word List",
								"Word List ...",
								message,
								MessageDialog.OK);
	}

	@Override 
	public void close(){
		saveProps();

		setVisible(false);
		dispose();
	}

	public static void main(String args[]){
		new TextEditor();
	}
}

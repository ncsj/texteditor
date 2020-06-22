import  java.awt.*;
import  java.awt.event.*;

import  java.io.*;
import  java.util.Properties;

/**
 TextEditor : $B%F%-%9%H%(%G%#%?!<(B
 $B@=:n4|4V(B : 2020.6.15-19$B!J#5F|4V!K(B

 $B%F%-%9%H%(%G%#%?!<$N:n@.2aDx$rDL$8$F!"(BJava$B%W%m%0%i%_%s%0$N(B
 $BMM!9$JCN<1$r3X$s$G$$$-$^$9!#(B

 $B$3$3$G3X$V<g$JFbMF$O0J2<$NDL$j$G$9!#(B

 $B#1(B.GUI$B4XO"(B
   $B%&%$%s%I%&!J(BFrame/Dialog/FileDialog$B!K$NA`:n(B
   $B%$%Y%s%H$N=hM}(B
     $B%$%s%?!<%U%'!<%9$H%i%`%@<0(B
	 $B%"%@%W%?!<$HF?L>%/%i%9(B
   GUI$B%3%s%]!<%M%s%H$N3hMQ(B
     Label
	 Button
	 TextField
	 TextArea
	 List
	 Menu$B!J(BMenu,MenuBar,MenuItem$B!K(B
 $B#2(B.$B0u:~(B
   $B%W%j%s%H%8%g%V!J(BPrintJob$B!K$H%0%i%U%#%/%9(B(Graphics)$B$NA`:n(B
     Toolkit$B$H(BPrintJob
	 Graphics$B$NA`:n(B
	 $B%Z!<%8B0@-!J(BPageAttributes$B!K$N@_Dj(B
 $B#3(B.$BJ8;zNs$NA`:n(B
   String$B%/%i%9$rMQ$$$?J8;zNs$NA`:n(B
	 length()$B%a%=%C%I(B	: $BJ8;zNs$ND9$5(B
     split()$B%a%=%C%I(B	: $BJ8;zNs$NJ,3d(B
	 charAt()$B%a%=%C%I(B	: $BJ8;zNs$+$i#1J8;z$r<h$j=P$9(B
	 format()$B%a%=%C%I(B	: $B=q<0@_Dj$rMxMQ$7$?J8;zNs$N@8@.(B
 $B#4(B.$B%U%!%$%kF~=PNO(B
   $B%F%-%9%H%U%!%$%k$NF~NO!JFI$_9~$_!K(B
     java.io.FileInputStream
	 java.io.InputStreamReader
	 java.io.BufferedReader
	 java.lang.StringBuilder
   $B%F%-%9%H%U%!%$%k$N=PNO!J=q$-=P$7!K(B
     java.io.FileOutputStream
	 java.io.PrintStream
   $B%W%m%Q%F%#!J(Bjava.util.Properties$B!K$rMxMQ$7$?%U%!%$%kF~=PNO(B
     java.util.Properties
**/
public class TextEditor extends Frame implements Closable{
	TextArea area = new TextArea();
	MenuBar	mbar = new MenuBar();

	FileManager fileManager = null;

	/**
	  $B%G%U%)%k%H%3%s%9%H%i%/%?!<(B
	**/
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

	/**
	  $B%a%K%e!<$N@_Dj(B
	**/
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

	/**
	  $B?75,$N%3%s%F%s%D$r:n@.$9$k$?$a$K!"(B
	  $B%F%-%9%H%(%j%"!J(BTextArea$B!K$H%U%!%$%k%^%M!<%8%c$r=i4|2=$7$F$$$k!#(B
	**/
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

	/**
	  $B%U%!%$%k$r%*!<%W%s$9$k!#(B
	  $B%U%!%$%k$N;XDj$K$O!"(BFileDialog$B$rMxMQ$9$k!#(B
	  $B%U%!%$%k$N%m!<%I$K$O!"(BloadFile()$B$rMxMQ$9$k!#(B
	**/
	void openFile(){
		FileDialog dlg = new FileDialog(this,"File Open ...",FileDialog.LOAD);
		dlg.setVisible(true);

		String dir = dlg.getDirectory();
		String file = dlg.getFile();

		if(dir != null && file != null){
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

	/**
	  $B%U%!%$%k$r%m!<%I$9$k!#(B
	  $B%U%!%$%k$N%m!<%I$K$O!"(BFileManager$B$rMxMQ$7$F$$$k!#(B
	  $B>\:Y$O!"(BFileManager.java$B$r;2>H$N$3$H!#(B
	**/
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

	/**
	  $BJ];}$7$F$$$k%F%-%9%H$r!"%U%!%$%k$XJ]B8$9$k!#(B
	  $B%U%!%$%kL>$,L$Dj$N>l9g$O!"(BsaveAsFile()$B$X0\9T$9$k!#(B
	  $B<B:]$K%U%!%$%k$rJ]B8$7$F$$$k$N$O!"(BFileManager$B$G$"$k!#(B
	  $B>\:Y$O!"(BFileManager.java$B$r;2>H$N$3$H!#(B
	**/
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

	/**
	  $B%U%!%$%kL>$r;XDj$7$F!"%U%!%$%k$XJ]B8$9$k!#(B
	  $B%U%!%$%kL>$N;XDj$K$O!"(BFileDialog$B$rMxMQ$9$k!#(B
	**/
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

	/*
	   $B%F%-%9%H$N0u:~(B
	*/
	void print(){
		String text = area.getText();
		String [] lines = text.split("\n");

		String title = "TextEditor v1.0";
		if(this.fileManager != null){
			title = "TextEditor - " + this.fileManager.fname;
		}

		// PrintManager$B$rMxMQ$7$F0u:~$r9T$&!#(B
		// $B>\:Y$O!"(BPrintManager.java$B$r;2>H$N$3$H!#(B
		PrintManager manager = PrintManager.getInstance(this,title);
		manager.print(lines);
	}

	/*
	   $B%W%m%Q%F%#$NJ]B8(B
	   $B%&%$%s%I%&(B(Frame)$B$NI=<(0LCV>pJs$r(BProperies$B$N5!G=$rMxMQ$7$FJ]B8$9$k!#(B
	   $B$3$3$G$O!"(BXML$B7A<0$GJ]B8$9$k5!G=!J(BstoreToXML()$B!K$rMxMQ$7$F$$$k!#(B
	*/
	void saveProps(){
		// Rectangle = $B6k7A(B
		// $B8=:_$NI=<(0LCV$r(BRectangle$B$N%$%s%9%?%s%9$H$7$F<hF@$9$k!#(B
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
			// $BJ]B8$9$k%U%!%$%k!J(Bbounds.xml$B!K$N;XDj(B
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
	   $B%W%m%Q%F%#$NFI$_9~$_(B
	   $B%F%-%9%H%(%G%#%?5/F0;~$NI=<(0LCV$rFI$_9~$`!#(B
	   saveProps()$B$K$FJ]4I$5$l$?I=<(0LCV>pJs$rFI$_9~$_!"(B
	   $BA02s=*N;;~$KI=<($5$l$F$$$?0LCV$KI=<($9$k$?$a$KMxMQ$9$k!#(B
	   $B=i2s5/F0;~$J$I!"@_Dj%U%!%$%k!J(Bbounds.xml$B!K$,B8:_$7$J$$(B
	   $B>l9g$O!"=i4|@_Dj!J(B0,0,800,600$B!K$rE,MQ$9$k!#(B
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

			// $BFI$_9~$s$@CM$rE,MQ$9$k!#(B
			setBounds(x,y,w,h);
		}
		catch(FileNotFoundException e){
			// $B%U%!%$%k$,B8:_$7$J$$>l9g$K$O!"=i4|@_Dj$rMQ$$$k!#(B
			setBounds(0,0,800,600);
		}
		catch(IOException e){
			String err = "I/O Error";
			String msg = "I/O Error Detected.";
			String detail = e.toString();
			MessageDialog dlg = new MessageDialog(this,err,msg,detail,MessageDialog.OK);
		}
	}

	/**
	  $BJ8;zNs8!:w5!G=(B
	  $B$[$H$s$I$N5!G=$O!"(BSearchDialog$B$*$h$S(BSearchDialog$B$K$F(B
	  $BMxMQ$5$l$F$$$k0J2<$N%/%i%9$K$h$j<B8=$5$l$F$$$k!#(B

		SearchDialog	: $BJ8;zNs8!:w$N%?%9%/$r9T$&$?$a$N%@%$%"%m%0%\%C%/%9(B
		WordRetriever	: $BJ8;zNs8!:w$r9T$&%/%i%9(B
		WordRange		: $B8!:w$7$?J8;zNs$N0LCV$rI=$9%/%i%9(B
		StringChecker	: $BJ8;zNsCf$KB8:_$9$kC18l$N0lMw$r<hF@$9$k$?$a$KMxMQ(B
						  $BFCDj$NC18l$,$$$/$DB8:_$9$k$N$+$O!"(BWordCounter$B$N(B
						  $B%$%s%9%?%s%9$H$7$FI=8=$7$F$$$k!#(B
		WordCounter		: $BFCDj$NJ8;zNs$,J8;zNsCf$KB8:_$7$F$$$k?t$rI=$9!#(B
						  $B8!:w8uJd$N0lMw$N$?$a$KMxMQ$7$F$$$k!#(B
	**/
	void search(){
		new SearchDialog(this);
	}

	/*
	   $BJ8;zNs$NCV49(B
	   $BL$<BAu(B
	*/
	void replace(){
		System.out.println("REPLACE");
	}

	/*
	   $B8=:_(Barea(TextArea)$B$K$FJ];}$7$F$$$kJ8;zNs$NCf$KB8:_$9$k(B
	   $BC18l$N0lMw$r<hF@$7!"%@%$%"%m%0(B(MessageDialog)$B$K$FI=<($9$k!#(B
	*/
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

	/**
	  $B%&%$%s%I%&$rJD$8$k$H$-$K5/F0$9$k%a%=%C%I!#(B
	  Closable$B%$%s%?!<%U%'!<%9$N(Bclose()$B%a%=%C%I$r(B
	  $B%*!<%P!<%i%$%I$7$F$$$k!#(B
	**/
	@Override 
	public void close(){
		saveProps();

		setVisible(false);
		dispose();
	}

	/**
	  $B%a%$%s%a%=%C%I(B
	  $B%F%-%9%H%(%G%#%?$r5/F0$9$k!#(B
	**/
	public static void main(String args[]){
		new TextEditor();
	}
}

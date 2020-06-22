import  java.awt.*;
import  java.awt.event.*;

import  java.io.*;
import  java.util.Properties;

/**
 TextEditor : テキストエディター
 製作期間 : 2020.6.15-19（５日間）

 テキストエディターの作成過程を通じて、Javaプログラミングの
 様々な知識を学んでいきます。

 ここで学ぶ主な内容は以下の通りです。

 １.GUI関連
   ウインドウ（Frame/Dialog/FileDialog）の操作
   イベントの処理
     インターフェースとラムダ式
	 アダプターと匿名クラス
   GUIコンポーネントの活用
     Label
	 Button
	 TextField
	 TextArea
	 List
	 Menu（Menu,MenuBar,MenuItem）
 ２.印刷
   プリントジョブ（PrintJob）とグラフィクス(Graphics)の操作
     ToolkitとPrintJob
	 Graphicsの操作
	 ページ属性（PageAttributes）の設定
 ３.文字列の操作
   Stringクラスを用いた文字列の操作
	 length()メソッド	: 文字列の長さ
     split()メソッド	: 文字列の分割
	 charAt()メソッド	: 文字列から１文字を取り出す
	 format()メソッド	: 書式設定を利用した文字列の生成
 ４.ファイル入出力
   テキストファイルの入力（読み込み）
     java.io.FileInputStream
	 java.io.InputStreamReader
	 java.io.BufferedReader
	 java.lang.StringBuilder
   テキストファイルの出力（書き出し）
     java.io.FileOutputStream
	 java.io.PrintStream
   プロパティ（java.util.Properties）を利用したファイル入出力
     java.util.Properties
**/
public class TextEditor extends Frame implements Closable{
	TextArea area = new TextArea();
	MenuBar	mbar = new MenuBar();

	FileManager fileManager = null;

	/**
	  デフォルトコンストラクター
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
	  メニューの設定
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
	  新規のコンテンツを作成するために、
	  テキストエリア（TextArea）とファイルマネージャを初期化している。
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
	  ファイルをオープンする。
	  ファイルの指定には、FileDialogを利用する。
	  ファイルのロードには、loadFile()を利用する。
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
	  ファイルをロードする。
	  ファイルのロードには、FileManagerを利用している。
	  詳細は、FileManager.javaを参照のこと。
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
	  保持しているテキストを、ファイルへ保存する。
	  ファイル名が未定の場合は、saveAsFile()へ移行する。
	  実際にファイルを保存しているのは、FileManagerである。
	  詳細は、FileManager.javaを参照のこと。
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
	  ファイル名を指定して、ファイルへ保存する。
	  ファイル名の指定には、FileDialogを利用する。
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
	   テキストの印刷
	*/
	void print(){
		String text = area.getText();
		String [] lines = text.split("\n");

		String title = "TextEditor v1.0";
		if(this.fileManager != null){
			title = "TextEditor - " + this.fileManager.fname;
		}

		// PrintManagerを利用して印刷を行う。
		// 詳細は、PrintManager.javaを参照のこと。
		PrintManager manager = PrintManager.getInstance(this,title);
		manager.print(lines);
	}

	/*
	   プロパティの保存
	   ウインドウ(Frame)の表示位置情報をProperiesの機能を利用して保存する。
	   ここでは、XML形式で保存する機能（storeToXML()）を利用している。
	*/
	void saveProps(){
		// Rectangle = 矩形
		// 現在の表示位置をRectangleのインスタンスとして取得する。
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
			// 保存するファイル（bounds.xml）の指定
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
	   テキストエディタ起動時の表示位置を読み込む。
	   saveProps()にて保管された表示位置情報を読み込み、
	   前回終了時に表示されていた位置に表示するために利用する。
	   初回起動時など、設定ファイル（bounds.xml）が存在しない
	   場合は、初期設定（0,0,800,600）を適用する。
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

			// 読み込んだ値を適用する。
			setBounds(x,y,w,h);
		}
		catch(FileNotFoundException e){
			// ファイルが存在しない場合には、初期設定を用いる。
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
	  文字列検索機能
	  ほとんどの機能は、SearchDialogおよびSearchDialogにて
	  利用されている以下のクラスにより実現されている。

		SearchDialog	: 文字列検索のタスクを行うためのダイアログボックス
		WordRetriever	: 文字列検索を行うクラス
		WordRange		: 検索した文字列の位置を表すクラス
		StringChecker	: 文字列中に存在する単語の一覧を取得するために利用
						  特定の単語がいくつ存在するのかは、WordCounterの
						  インスタンスとして表現している。
		WordCounter		: 特定の文字列が文字列中に存在している数を表す。
						  検索候補の一覧のために利用している。
	**/
	void search(){
		new SearchDialog(this);
	}

	/*
	   文字列の置換
	   未実装
	*/
	void replace(){
		System.out.println("REPLACE");
	}

	/*
	   現在area(TextArea)にて保持している文字列の中に存在する
	   単語の一覧を取得し、ダイアログ(MessageDialog)にて表示する。
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
	  ウインドウを閉じるときに起動するメソッド。
	  Closableインターフェースのclose()メソッドを
	  オーバーライドしている。
	**/
	@Override 
	public void close(){
		saveProps();

		setVisible(false);
		dispose();
	}

	/**
	  メインメソッド
	  テキストエディタを起動する。
	**/
	public static void main(String args[]){
		new TextEditor();
	}
}

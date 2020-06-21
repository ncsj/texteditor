import  java.awt.*;
import  java.awt.event.*;

import  java.io.*;
import  java.util.Properties;

/**
 TextEditor : �e�L�X�g�G�f�B�^�[
 ������� : 2020.6.15-19�i�T���ԁj

 �e�L�X�g�G�f�B�^�[�̍쐬�ߒ���ʂ��āAJava�v���O���~���O��
 �l�X�Ȓm�����w��ł����܂��B

 �����Ŋw�Ԏ�ȓ��e�͈ȉ��̒ʂ�ł��B

 �P.GUI�֘A
   �E�C���h�E�iFrame/Dialog/FileDialog�j�̑���
   �C�x���g�̏���
     �C���^�[�t�F�[�X�ƃ����_��
	 �A�_�v�^�[�Ɠ����N���X
   GUI�R���|�[�l���g�̊��p
     Label
	 Button
	 TextField
	 TextArea
	 List
	 Menu�iMenu,MenuBar,MenuItem�j
 �Q.���
   �v�����g�W���u�iPrintJob�j�ƃO���t�B�N�X(Graphics)�̑���
     Toolkit��PrintJob
	 Graphics�̑���
	 �y�[�W�����iPageAttributes�j�̐ݒ�
 �R.������̑���
   String�N���X��p����������̑���
	 length()���\�b�h	: ������̒���
     split()���\�b�h	: ������̕���
	 charAt()���\�b�h	: �����񂩂�P���������o��
	 format()���\�b�h	: �����ݒ�𗘗p����������̐���
 �S.�t�@�C�����o��
   �e�L�X�g�t�@�C���̓��́i�ǂݍ��݁j
     java.io.FileInputStream
	 java.io.InputStreamReader
	 java.io.BufferedReader
	 java.lang.StringBuilder
   �e�L�X�g�t�@�C���̏o�́i�����o���j
     java.io.FileOutputStream
	 java.io.PrintStream
   �v���p�e�B�ijava.util.Properties�j�𗘗p�����t�@�C�����o��
     java.util.Properties
**/
public class TextEditor extends Frame implements Closable{
	TextArea area = new TextArea();
	MenuBar	mbar = new MenuBar();

	FileManager fileManager = null;

	/**
	  �f�t�H���g�R���X�g���N�^�[
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
	  ���j���[�̐ݒ�
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
	  �V�K�̃R���e���c���쐬���邽�߂ɁA
	  �e�L�X�g�G���A�iTextArea�j�ƃt�@�C���}�l�[�W�������������Ă���B
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
	  �t�@�C�����I�[�v������B
	  �t�@�C���̎w��ɂ́AFileDialog�𗘗p����B
	  �t�@�C���̃��[�h�ɂ́AloadFile()�𗘗p����B
	**/
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

	/**
	  �t�@�C�������[�h����B
	  �t�@�C���̃��[�h�ɂ́AFileManager�𗘗p���Ă���B
	  �ڍׂ́AFileManager.java���Q�Ƃ̂��ƁB
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
	  �ێ����Ă���e�L�X�g���A�t�@�C���֕ۑ�����B
	  �t�@�C����������̏ꍇ�́AsaveAsFile()�ֈڍs����B
	  ���ۂɃt�@�C����ۑ����Ă���̂́AFileManager�ł���B
	  �ڍׂ́AFileManager.java���Q�Ƃ̂��ƁB
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
	  �t�@�C�������w�肵�āA�t�@�C���֕ۑ�����B
	  �t�@�C�����̎w��ɂ́AFileDialog�𗘗p����B
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
	   �e�L�X�g�̈��
	*/
	void print(){
		String text = area.getText();
		String [] lines = text.split("\n");

		String title = "TextEditor v1.0";
		if(this.fileManager != null){
			title = "TextEditor - " + this.fileManager.fname;
		}

		// PrintManager�𗘗p���Ĉ�����s���B
		// �ڍׂ́APrintManager.java���Q�Ƃ̂��ƁB
		PrintManager manager = PrintManager.getInstance(this,title);
		manager.print(lines);
	}

	/*
	   �v���p�e�B�̕ۑ�
	   �E�C���h�E(Frame)�̕\���ʒu����Properies�̋@�\�𗘗p���ĕۑ�����B
	   �����ł́AXML�`���ŕۑ�����@�\�istoreToXML()�j�𗘗p���Ă���B
	*/
	void saveProps(){
		// Rectangle = ��`
		// ���݂̕\���ʒu��Rectangle�̃C���X�^���X�Ƃ��Ď擾����B
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
			// �ۑ�����t�@�C���ibounds.xml�j�̎w��
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
	   �v���p�e�B�̓ǂݍ���
	   �e�L�X�g�G�f�B�^�N�����̕\���ʒu��ǂݍ��ށB
	   saveProps()�ɂĕۊǂ��ꂽ�\���ʒu����ǂݍ��݁A
	   �O��I�����ɕ\������Ă����ʒu�ɕ\�����邽�߂ɗ��p����B
	   ����N�����ȂǁA�ݒ�t�@�C���ibounds.xml�j�����݂��Ȃ�
	   �ꍇ�́A�����ݒ�i0,0,800,600�j��K�p����B
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

			// �ǂݍ��񂾒l��K�p����B
			setBounds(x,y,w,h);
		}
		catch(FileNotFoundException e){
			// �t�@�C�������݂��Ȃ��ꍇ�ɂ́A�����ݒ��p����B
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
	  �����񌟍��@�\
	  �قƂ�ǂ̋@�\�́ASearchDialog�����SearchDialog�ɂ�
	  ���p����Ă���ȉ��̃N���X�ɂ���������Ă���B

		SearchDialog	: �����񌟍��̃^�X�N���s�����߂̃_�C�A���O�{�b�N�X
		WordRetriever	: �����񌟍����s���N���X
		WordRange		: ��������������̈ʒu��\���N���X
		StringChecker	: �����񒆂ɑ��݂���P��̈ꗗ���擾���邽�߂ɗ��p
						  ����̒P�ꂪ�������݂���̂��́AWordCounter��
						  �C���X�^���X�Ƃ��ĕ\�����Ă���B
		WordCounter		: ����̕����񂪕����񒆂ɑ��݂��Ă��鐔��\���B
						  �������̈ꗗ�̂��߂ɗ��p���Ă���B
	**/
	void search(){
		new SearchDialog(this);
	}

	/*
	   ������̒u��
	   ������
	*/
	void replace(){
		System.out.println("REPLACE");
	}

	/*
	   ����area(TextArea)�ɂĕێ����Ă��镶����̒��ɑ��݂���
	   �P��̈ꗗ���擾���A�_�C�A���O(MessageDialog)�ɂĕ\������B
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
	  �E�C���h�E�����Ƃ��ɋN�����郁�\�b�h�B
	  Closable�C���^�[�t�F�[�X��close()���\�b�h��
	  �I�[�o�[���C�h���Ă���B
	**/
	@Override 
	public void close(){
		saveProps();

		setVisible(false);
		dispose();
	}

	/**
	  ���C�����\�b�h
	  �e�L�X�g�G�f�B�^���N������B
	**/
	public static void main(String args[]){
		new TextEditor();
	}
}

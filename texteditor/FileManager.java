import  java.io.*;

/*
   テキストファイルの入出力を行うオブジェクトを定義しているクラス。
   このクラスのインスタンスを得るには、スタティック・メソッドgetManager()を
   使用する必要がある。

   インスタンスの取得		: static FileManager getManager(String fname);
								String fname --- 入出力の対象となるファイル
   ファイルの出力（保存）	: void save(String text);
								引数でしていされる文字列をファイルへ出力する。
   ファイルの入力（読込）	: String load(); 
								ファイルから読み込んだテキストを返す。
*/
public class FileManager{
	static FileManager manager = null;

	public static FileManager getManager(String fname) throws FileManagerException{
		File file = new File(fname);
		if(file.exists()){				// ファイルの存在をチェックする。
			if(file.isDirectory()){		// ディレクトリかどうかを確認する。
				String err = "THE FILE IS DIRECTORY : " + fname;
				throw new FileManagerException(err);
			}
		}
		else{
			;
		}

		if(manager == null){
			manager = new FileManager(fname);
		}
		else{
			manager.fname = fname;
		}

		return manager;
	}

	String  fname = null;

	private FileManager(){
	}

	private FileManager(String fname){
		this.fname = fname;
	}

	public void save(String text) throws FileManagerException{
		try{
			FileOutputStream fout = new FileOutputStream(fname);
			PrintStream out = new PrintStream(fout);
			out.print(text);
			out.close();
			fout.close();
		}
		catch(FileNotFoundException e){
			String err = "FileManager.save()\n" + e.toString();
			throw new FileManagerException(err);
		}
		catch(IOException e){
			String err = "FileManager.save()\n" + e.toString();
			throw new FileManagerException(err);
		}
	}

	public String load() throws FileManagerException{
		String text = null;
		try{
			FileInputStream fin = new FileInputStream(fname);
			InputStreamReader is = new InputStreamReader(fin);

			StringBuilder sb = new StringBuilder();
			while(true){
				int c = is.read();
				if(c == -1){
					break;
				}
				else{
					sb.append((char)c);
				}
			}
			text = sb.toString();
			is.close();
			fin.close();
		}
		catch(FileNotFoundException e){
			String err = "FileManager.save()\n" + e.toString();
			throw new FileManagerException(err);
		}
		catch(IOException e){
			String err = "FileManager.save()\n" + e.toString();
			throw new FileManagerException(err);
		}
		return text;
	}
}

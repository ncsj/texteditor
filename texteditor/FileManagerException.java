/*
   FileManagerクラスを利用するときに発生する可能性のあるエラーを
   通知するために利用する例外クラス
*/
public class FileManagerException extends Exception{
	String err = null;

	public FileManagerException(){
		super();
	}

	public FileManagerException(String s){
		super(s);
	}

	@Override
	public String toString(){
		String s = null;
		if(err == null){
			s = super.toString();
		}
		else{
			s = err;
		}
		return s;
	}
}

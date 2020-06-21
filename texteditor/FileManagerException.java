/*
   FileManager�N���X�𗘗p����Ƃ��ɔ�������\���̂���G���[��
   �ʒm���邽�߂ɗ��p�����O�N���X
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

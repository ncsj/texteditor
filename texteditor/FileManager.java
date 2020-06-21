import  java.io.*;

/*
   �e�L�X�g�t�@�C���̓��o�͂��s���I�u�W�F�N�g���`���Ă���N���X�B
   ���̃N���X�̃C���X�^���X�𓾂�ɂ́A�X�^�e�B�b�N�E���\�b�hgetManager()��
   �g�p����K�v������B

   �C���X�^���X�̎擾		: static FileManager getManager(String fname);
								String fname --- ���o�͂̑ΏۂƂȂ�t�@�C��
   �t�@�C���̏o�́i�ۑ��j	: void save(String text);
								�����ł��Ă�����镶������t�@�C���֏o�͂���B
   �t�@�C���̓��́i�Ǎ��j	: String load(); 
								�t�@�C������ǂݍ��񂾃e�L�X�g��Ԃ��B
*/
public class FileManager{
	static FileManager manager = null;

	public static FileManager getManager(String fname) throws FileManagerException{
		File file = new File(fname);
		if(file.exists()){				// �t�@�C���̑��݂��`�F�b�N����B
			if(file.isDirectory()){		// �f�B���N�g�����ǂ������m�F����B
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

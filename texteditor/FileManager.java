import  java.io.*;

/*
   $B%F%-%9%H%U%!%$%k$NF~=PNO$r9T$&%*%V%8%'%/%H$rDj5A$7$F$$$k%/%i%9!#(B
   $B$3$N%/%i%9$N%$%s%9%?%s%9$rF@$k$K$O!"%9%?%F%#%C%/!&%a%=%C%I(BgetManager()$B$r(B
   $B;HMQ$9$kI,MW$,$"$k!#(B

   $B%$%s%9%?%s%9$N<hF@(B		: static FileManager getManager(String fname);
								String fname --- $BF~=PNO$NBP>]$H$J$k%U%!%$%k(B
   $B%U%!%$%k$N=PNO!JJ]B8!K(B	: void save(String text);
								$B0z?t$G$7$F$$$5$l$kJ8;zNs$r%U%!%$%k$X=PNO$9$k!#(B
   $B%U%!%$%k$NF~NO!JFI9~!K(B	: String load(); 
								$B%U%!%$%k$+$iFI$_9~$s$@%F%-%9%H$rJV$9!#(B
*/
public class FileManager{
	static FileManager manager = null;

	public static FileManager getManager(String fname) throws FileManagerException{
		File file = new File(fname);
		if(file.exists()){				// $B%U%!%$%k$NB8:_$r%A%'%C%/$9$k!#(B
			if(file.isDirectory()){		// $B%G%#%l%/%H%j$+$I$&$+$r3NG'$9$k!#(B
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

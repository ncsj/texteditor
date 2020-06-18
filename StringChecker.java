import  java.util.ArrayList;
import  java.util.Collections;

class StringChecker{
	String target = null;
	ArrayList <WordCounter> wlist = new ArrayList <WordCounter> ();

	public StringChecker(String target){
		this.target = filter(target);
	}

	String filter(String text){
		StringBuilder sb = new StringBuilder();

		char  prev = 0x0000;
		for(int i=0;i<text.length();i++){
			char c = text.charAt(i);
			switch( c ){
				case 0x000d:	//  CR
				case 0x000a:	//  LF
				case 0x0009:	//  TAB
				case '.':
					c = ' ';
					break;
				default:
					break;
			}

			// $B0J2<$N>r7o<0$G!"O"B3$7$F$$$k6uGr!J%9%Z!<%9!K$rH=Dj(B
			if((prev == ' ') && (c == ' ')){
				;
			}
			else{ // $BO"B3$7$?6uGr$G$J$1$l$P!"%P%C%U%!$XDI2C(B
				sb.append( c );
				prev = c;
			}
		}

		String s = sb.toString();
		return s;
	}

	/*
	   $BBP>]$H$7$F$$$kJ8;zNsFb$KB8:_$9$kC18l$NAm?t$r?t$($F!"(B
	   $BC18l?t(B(WordCounter)$B$N0lMw!JG[Ns!K$rJV$9(B
	*/
	public WordCounter [] listup(){
		String [] words = target.split(" ");

		for(int i=0;i<words.length;i++){
			boolean flag = false;
			for(WordCounter wc : wlist){
				flag = wc.check(words[i]);
				if(flag){
					break;
				}
			}

			if(flag){
				;		// $B$J$K$b$7$J$$(B
			}
			else{		// $B?7$7$$C18l$J$N$G!"EPO?$9$k!#(B
				WordCounter wc = new WordCounter(words[i]);
				wlist.add(wc);
			}
		}

		// $BC18l$N0lMw$r%=!<%H$9$k!#(B
		Collections.sort(wlist);

		WordCounter [] warray = new WordCounter [wlist.size()];
		for(int i=0;i<wlist.size();i++){
			warray[i] = wlist.get(i);
		}

		return warray;
	}

	/*
	   $BC18l$N1d$Y?t(B
	*/
	public int count(){
		int count = 0;
		WordCounter [] array = listup();

		for(WordCounter wc : array){
			count = count + wc.count();
		}

		return count;
	}

	/*
	   $B;XDj$7$?C18l$,B8:_$9$k?t(B
	*/
	public int count(String w){
		int rtc = 0;

		String [] words = target.split(" ");
		for(String word : words){
			if(w.equals(word)){		// w == word : w.equals(word)
				rtc++;
			}
		}

		return rtc;
	}

	public int size(){
		return target.length();
	}
}

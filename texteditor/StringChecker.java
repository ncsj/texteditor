import  java.util.ArrayList;
import  java.util.Collections;

/**
  $BJ8;zNs$r%A%'%C%/$7!"$=$NCf$KEP>l$9$kC18l$r%j%9%H2=$9$k!#(B
  $B%j%9%H2=$5$l$kC18l$O!"EP>l2s?t$H$H$b$K!"(BWordCounter$B$N(B
  $B%*%V%8%'%/%H$H$7$FI=8=$5$l$k!#(B

  $B$J$*!"F|K\8l$K$OBP1~$7$F$$$J$$!#(B
**/
class StringChecker{
	String target = null;
	ArrayList <WordCounter> wlist = new ArrayList <WordCounter> ();

	public StringChecker(String target){
		this.target = filter(target);
	}

	/**
	  $B2~9T$d%?%V!"6gFIE@!J%T%j%*%I$d%+%s%^!K!"6uGr$O!"(B
	  $BC18l$r?t$($k>e$GITMW$J$N$G!"$"$i$+$8$a>C$7$F$*$/!#(B
	  $BC18l$r6h@Z$k$?$a$N6uGr$N$_!";D$7$F$*$/!#(B
	**/
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
				case ',':
					c = ' ';	// $B$H$j$"$($:6uGr!J%9%Z!<%9!K$XCV$-49$($k(B
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

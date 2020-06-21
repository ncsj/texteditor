/**
  $BJ];}$9$kJ8;zNs$,!"FCDj$NJ8;zNsCf$K2?2sEP>l$9$k$N$+$rI=$9$?$a$N%*%V%8%'%/%H(B
  StringChecker$B$K$FC18l$N0lMw$r:n@.$9$k$H$-$K;HMQ$9$k!#(B
**/
class WordCounter implements Comparable<WordCounter>{
	String	word = null;
	int		count = 0;

	public WordCounter(String word){
		this.word = word;
		count = 1;
	}

	public boolean check(String word){
		boolean rtc = false;
		if(this.word.equals(word)){
			rtc = true;
			count++;
		}
		return rtc;
	}

	public String getWord(){
		return this.word;
	}

	public int count(){
		return this.count;
	}

	@Override
	public int compareTo(WordCounter wc){
		int  rtc = this.word.compareTo(wc.word);
		return rtc;
	}
}


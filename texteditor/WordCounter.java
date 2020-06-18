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


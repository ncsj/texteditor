/**
  保持する文字列が、特定の文字列中に何回登場するのかを表すためのオブジェクト
  StringCheckerにて単語の一覧を作成するときに使用する。
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


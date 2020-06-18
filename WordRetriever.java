class WordRetriever{
	String text;
	int  index = 0;

	public WordRetriever(String text){
		this.text = text;
	}

	// 1文字目と一致するところを見つけるだけ
	int retrieve(char c){
		int rtc = -1;
		for(;index<text.length();index++){
			char a = text.charAt(index);
			if(a == c){
				rtc = index;
				index++;
				break;
			}
		}
		return rtc;
	}

	/**
		指定された文字列を検索する。
		検索位置は、初回は文字列の先頭から、
		２回目以降は、次の文字から検索する。

		文字列が見つかれば、WordRange（検索した文字列とその位置）
		のインスタンスを返す。
		見つからなければ、nullを返す。
	**/
	public WordRange retrieve(String word){
		WordRange  range = null;
		char c = word.charAt(0);

		while(this.index <= text.length()-word.length()){
			int index = retrieve( c );
			if(index > -1){
				int length = word.length();

				// 文字列（text）の中から、検索する文字列(word)と同じ長さの文字列を取り出す。
				String s = text.substring(index,index+length);
			
				// 取り出した文字列（s）と検索している文字列（word）とが一致するかチェック
				if(word.equals(s)){
					int begin = index;
					int end   = index + length - 1;

					range = new WordRange(word,begin,end);
					break;
				}
			}
		}

		return range;
	}
}

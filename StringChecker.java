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

			// 以下の条件式で、連続している空白（スペース）を判定
			if((prev == ' ') && (c == ' ')){
				;
			}
			else{ // 連続した空白でなければ、バッファへ追加
				sb.append( c );
				prev = c;
			}
		}

		String s = sb.toString();
		return s;
	}

	/*
	   対象としている文字列内に存在する単語の総数を数えて、
	   単語数(WordCounter)の一覧（配列）を返す
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
				;		// なにもしない
			}
			else{		// 新しい単語なので、登録する。
				WordCounter wc = new WordCounter(words[i]);
				wlist.add(wc);
			}
		}

		// 単語の一覧をソートする。
		Collections.sort(wlist);

		WordCounter [] warray = new WordCounter [wlist.size()];
		for(int i=0;i<wlist.size();i++){
			warray[i] = wlist.get(i);
		}

		return warray;
	}

	/*
	   単語の延べ数
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
	   指定した単語が存在する数
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

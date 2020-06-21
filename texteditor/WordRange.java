/**
  単語が文字列中で出現する場所を表すオブジェクトを定義。
**/
class WordRange{
	String	word;
	int		begin;
	int		end;

	public WordRange(String word,int begin,int end){
		this.word	= word;
		this.begin	= begin;
		this.end	= end;
	}
}

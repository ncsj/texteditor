/**
  ������̈ʒu����肷��B
  �C���X�^���X�������ɐݒ肵���e�L�X�g(Strint text)���ɁA
  retrieve(String word)���\�b�h�ɂĎw�肷�镶����iword�j��
  �o������ʒu���AWordRange�Ƃ��ĕԂ��B
**/
class WordRetriever{
	String text;
	int  index = 0;

	public WordRetriever(String text){
		this.text = text;
	}

	// 1�����ڂƈ�v����Ƃ���������邾��
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
		�w�肳�ꂽ���������������B
		�����ʒu�́A����͕�����̐擪����A
		�Q��ڈȍ~�́A���̕������猟������B

		�����񂪌�����΁AWordRange�i��������������Ƃ��̈ʒu�j
		�̃C���X�^���X��Ԃ��B
		������Ȃ���΁Anull��Ԃ��B
	**/
	public WordRange retrieve(String word){
		WordRange  range = null;
		char c = word.charAt(0);

		while(this.index <= text.length()-word.length()){
			int index = retrieve( c );
			if(index > -1){
				int length = word.length();

				// ������itext�j�̒�����A�������镶����(word)�Ɠ��������̕���������o���B
				String s = text.substring(index,index+length);
			
				// ���o����������is�j�ƌ������Ă��镶����iword�j�Ƃ���v���邩�`�F�b�N
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

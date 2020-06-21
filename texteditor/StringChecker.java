import  java.util.ArrayList;
import  java.util.Collections;

/**
  ��������`�F�b�N���A���̒��ɓo�ꂷ��P������X�g������B
  ���X�g�������P��́A�o��񐔂ƂƂ��ɁAWordCounter��
  �I�u�W�F�N�g�Ƃ��ĕ\�������B

  �Ȃ��A���{��ɂ͑Ή����Ă��Ȃ��B
**/
class StringChecker{
	String target = null;
	ArrayList <WordCounter> wlist = new ArrayList <WordCounter> ();

	public StringChecker(String target){
		this.target = filter(target);
	}

	/**
	  ���s��^�u�A��Ǔ_�i�s���I�h��J���}�j�A�󔒂́A
	  �P��𐔂����ŕs�v�Ȃ̂ŁA���炩���ߏ����Ă����B
	  �P�����؂邽�߂̋󔒂̂݁A�c���Ă����B
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
					c = ' ';	// �Ƃ肠�����󔒁i�X�y�[�X�j�֒u��������
					break;
				default:
					break;
			}

			// �ȉ��̏������ŁA�A�����Ă���󔒁i�X�y�[�X�j�𔻒�
			if((prev == ' ') && (c == ' ')){
				;
			}
			else{ // �A�������󔒂łȂ���΁A�o�b�t�@�֒ǉ�
				sb.append( c );
				prev = c;
			}
		}

		String s = sb.toString();
		return s;
	}

	/*
	   �ΏۂƂ��Ă��镶������ɑ��݂���P��̑����𐔂��āA
	   �P�ꐔ(WordCounter)�̈ꗗ�i�z��j��Ԃ�
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
				;		// �Ȃɂ����Ȃ�
			}
			else{		// �V�����P��Ȃ̂ŁA�o�^����B
				WordCounter wc = new WordCounter(words[i]);
				wlist.add(wc);
			}
		}

		// �P��̈ꗗ���\�[�g����B
		Collections.sort(wlist);

		WordCounter [] warray = new WordCounter [wlist.size()];
		for(int i=0;i<wlist.size();i++){
			warray[i] = wlist.get(i);
		}

		return warray;
	}

	/*
	   �P��̉��א�
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
	   �w�肵���P�ꂪ���݂��鐔
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

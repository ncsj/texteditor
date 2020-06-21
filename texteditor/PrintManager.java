import  java.awt.*;
import  java.util.Calendar;

/**
  �v�����g�W���u���Ǘ����A����������Ȃ����߂̃I�u�W�F�N�g�̒�`�B
  ���̃N���X���̂́A������̈���݂̂�z�肵�Ă���B
**/
public
class PrintManager{
	Frame  owner			= null;			// ���̃C���X�^���X�̃I�[�i�[�ƂȂ�t���[���̎w��
											// �v�����g�W���u���擾����Ƃ��ɕK�v�B
	Toolkit  toolkit		= null;			// �v�����g�W���u���擾���邽�߂̃c�[���L�b�g

	PageAttributes pageAttr	= null;			// �y�[�W�����i�p���̃T�C�Y������Ȃǁj
	JobAttributes  jobAttr	= null;			// �W���u�̑����i�o�͐��R�s�[�����ȂǁA���g�p�j

	static PrintManager manager = null;		// ���̃N���X�̃C���X�^���X�́A�P�݂̂ɐ�������B

	String title;							// Job�̃^�C�g��
	int max = 75;							// 1�y�[�W�̍ő�s���B�����l��75�ɐݒ�B
	
	/**
	  �f�t�H���g�R���X�g���N�^�[
	  �C���X�^���X�̐���}�����邽�߂ɁAprivate�ɂ��Ă��邱�Ƃɒ��ӁB
	**/
	private PrintManager(){
		// �c�[���L�b�g�̎擾
		toolkit = Toolkit.getDefaultToolkit();

		// �y�[�W�����̐ݒ�  �p���T�C�Y:A4,����:�c�iPORTRAIT�j
		pageAttr = new PageAttributes();
		pageAttr.setMedia(PageAttributes.MediaType.A4);
		pageAttr.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
	}

	/**
	  �C���X�^���X���擾���邽�߂̃X�^�e�B�b�N���\�b�h
	  �����A�t�@�N�g���[���\�b�h
	**/
	public static PrintManager getInstance(Frame owner,String title){
		if(manager == null){
			manager = new PrintManager();

			if(owner != null){
				manager.owner = owner;
			}
			else{
				manager.owner = new Frame();
			}

			manager.title = title;
		}
		return manager;
	}

	/**
	  ����������Ȃ��B
	  �P�s��������̔z��P�ɑ�������B
	  �P�y�[�W�̍ő�s���́A75�ɐݒ肵�Ă���B
	**/
	public void print(String [] lines){
		PrintJob  job = toolkit.getPrintJob(owner,title,jobAttr,pageAttr);

		// �y�[�W���̌v�Z
		int pageCount = getPageCount(lines.length,max);

		// ���
		for(int pi=0;pi<pageCount;pi++){
			Graphics g = job.getGraphics();			// �V�����y�[�W�̃O���t�B�N�X���擾
			
			int bi = pi * max;						// �y�[�W�Ɉ�������ŏ��̔z��̃C���f�b�N�X
			int ei = bi + max;						// �y�[�W���̏��
			if(ei > lines.length){					// ����̐����B�z��̒����𒴂��Ȃ����ƁB
				ei = lines.length;
			}

			printHeader(g);							// �w�b�_�[�̈��
			printPage(g,lines,bi,ei);				// �y�[�W�̖{�������
			printFooter(g,pi+1);					// �t�b�^�[�̈��
			g.dispose();							// ���y�[�W
		}

		job.end();
	}

	/**
	  �{���̈�����P�y�[�W���̈��
	**/
	void printPage(Graphics g,String [] lines,int bi,int ei){
		int yi = 0;
		for(int i=bi;i<ei;i++){		// �{���̈��
			int x = 65;
			int offset = yi * 10;
			int y = 60 + offset;
			g.drawString(lines[i],x,y);

			yi++;
		}
	}

	/**
	  �y�[�W���̌v�Z
	**/
	int getPageCount(int line,int max){
		int page_count = 0;
		
		int m = line % max;
		int rem = 0; 
		if(m > 0){
			rem = 1;
		}
		page_count = (line / max) + rem;

		return page_count;
	}

	/**
	  �w�b�_�[�̈��
	**/
	void printHeader(Graphics g){
		g.drawLine(30,48,550,48);

		String s = "[" + title + "]";
		g.drawString(s,30,40);

		// ���t�E����
		Calendar cal = Calendar.getInstance();
		int year  = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date  = cal.get(Calendar.DAY_OF_MONTH);

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min  = cal.get(Calendar.MINUTE);
		int sec  = cal.get(Calendar.SECOND);

		String sdate = String.format("%d/%02d/%02d",year,month,date);
		String stime = String.format("%02d:%02d:%02d",hour,min,sec);

		g.drawString(sdate,480,34);
		g.drawString(stime,500,44);
	}

	/**
	  �t�b�^�[�̈��
	**/
	void printFooter(Graphics g,int page){
		int y = 805;
		g.drawLine(30,y,550,y);

		g.drawString("["+page+"]",295,y+12);
	}
}


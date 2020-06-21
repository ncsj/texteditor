import  java.awt.*;
import  java.util.Calendar;

/**
  プリントジョブを管理し、印刷をおこなうためのオブジェクトの定義。
  このクラス自体は、文字列の印刷のみを想定している。
**/
public
class PrintManager{
	Frame  owner			= null;			// このインスタンスのオーナーとなるフレームの指定
											// プリントジョブを取得するときに必要。
	Toolkit  toolkit		= null;			// プリントジョブを取得するためのツールキット

	PageAttributes pageAttr	= null;			// ページ属性（用紙のサイズや向きなど）
	JobAttributes  jobAttr	= null;			// ジョブの属性（出力先やコピー部数など、未使用）

	static PrintManager manager = null;		// このクラスのインスタンスは、１つのみに制限する。

	String title;							// Jobのタイトル
	int max = 75;							// 1ページの最大行数。初期値を75に設定。
	
	/**
	  デフォルトコンストラクター
	  インスタンスの数を抑制するために、privateにしてあることに注意。
	**/
	private PrintManager(){
		// ツールキットの取得
		toolkit = Toolkit.getDefaultToolkit();

		// ページ属性の設定  用紙サイズ:A4,向き:縦（PORTRAIT）
		pageAttr = new PageAttributes();
		pageAttr.setMedia(PageAttributes.MediaType.A4);
		pageAttr.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
	}

	/**
	  インスタンスを取得するためのスタティックメソッド
	  所謂、ファクトリーメソッド
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
	  印刷をおこなう。
	  １行が文字列の配列１つに相当する。
	  １ページの最大行数は、75に設定してある。
	**/
	public void print(String [] lines){
		PrintJob  job = toolkit.getPrintJob(owner,title,jobAttr,pageAttr);

		// ページ数の計算
		int pageCount = getPageCount(lines.length,max);

		// 印刷
		for(int pi=0;pi<pageCount;pi++){
			Graphics g = job.getGraphics();			// 新しいページのグラフィクスを取得
			
			int bi = pi * max;						// ページに印刷される最初の配列のインデックス
			int ei = bi + max;						// ページ毎の上限
			if(ei > lines.length){					// 上限の制限。配列の長さを超えないこと。
				ei = lines.length;
			}

			printHeader(g);							// ヘッダーの印刷
			printPage(g,lines,bi,ei);				// ページの本文を印刷
			printFooter(g,pi+1);					// フッターの印刷
			g.dispose();							// 改ページ
		}

		job.end();
	}

	/**
	  本文の印刷＝１ページ分の印刷
	**/
	void printPage(Graphics g,String [] lines,int bi,int ei){
		int yi = 0;
		for(int i=bi;i<ei;i++){		// 本文の印刷
			int x = 65;
			int offset = yi * 10;
			int y = 60 + offset;
			g.drawString(lines[i],x,y);

			yi++;
		}
	}

	/**
	  ページ数の計算
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
	  ヘッダーの印刷
	**/
	void printHeader(Graphics g){
		g.drawLine(30,48,550,48);

		String s = "[" + title + "]";
		g.drawString(s,30,40);

		// 日付・時刻
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
	  フッターの印刷
	**/
	void printFooter(Graphics g,int page){
		int y = 805;
		g.drawLine(30,y,550,y);

		g.drawString("["+page+"]",295,y+12);
	}
}


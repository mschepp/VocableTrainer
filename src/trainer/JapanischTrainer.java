package trainer;

public class JapanischTrainer extends Vokabeltrainer {
	public enum japaneseWriting{
		KANA(1),KANJI(3),ROMAJI(2);
		private int idx;
		private japaneseWriting(int idx) {
			this.idx=idx;
		}
		
		public int getIdx() {
			return this.idx;
		}
	}
	public JapanischTrainer(String dbPath) {
		super(dbPath);
		// TODO Auto-generated constructor stub
	}

}

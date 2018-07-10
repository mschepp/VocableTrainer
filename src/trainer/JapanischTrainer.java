package trainer;

public class JapanischTrainer extends Vokabeltrainer {
	public enum japaneseWriting {
		KANA(-1), KANJI(-1), ROMAJI(-1);
		private int idx;

		private japaneseWriting(int idx) {
			this.idx = idx;
		}

		public int getIdx() {
			return this.idx;
		}

		public void setIdx(int idx) {
			this.idx = idx;
		}
	}

	public JapanischTrainer(String dbPath) {
		super(dbPath);
		japaneseWriting.KANA.setIdx(this.colums.indexOf("Kana"));
		japaneseWriting.KANJI.setIdx(this.colums.indexOf("Kanji"));
		japaneseWriting.ROMAJI.setIdx(this.colums.indexOf("Romaji"));

		// TODO Auto-generated constructor stub
	}

}

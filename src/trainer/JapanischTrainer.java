package trainer;

public class JapanischTrainer extends Vokabeltrainer {
	public enum japaneseWriting implements LanguageModi {
		KANA(-1), KANJI(-1), ROMAJI(-1), GERMAN(-1);
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
		japaneseWriting.GERMAN.setIdx(this.colums.indexOf("Deutsch"));

		if (japaneseWriting.KANA.getIdx() != -1) {
			this.askId = japaneseWriting.KANA.getIdx();
		} else if (japaneseWriting.KANJI.getIdx() != -1) {
			this.askId = japaneseWriting.KANJI.getIdx();
		} else if (japaneseWriting.ROMAJI.getIdx() != -1) {
			this.askId = japaneseWriting.ROMAJI.getIdx();
		} else
			throw new RuntimeException("Database needs a column with the name Kana, Kanji or Romji.");

	}

}

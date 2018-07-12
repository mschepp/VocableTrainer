package trainer;

public class Algorithm {

	static public int editDistance(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2))
			return 0;
		int[] act_row = new int[s2.length() + 1];
		int[] old_row = new int[s2.length() + 1];

		for (int i = 0; i < s1.length() + 1; i++) {
			old_row = act_row.clone();
			for (int j = 0; j < s2.length() + 1; j++) {
				if (i == 0) {
					act_row[j] = j;
					continue;
				}
				if (j == 0) {
					act_row[j] += 1;
					continue;
				}
				if (s1.charAt(i - 1) == s2.charAt(j - 1))
					act_row[j] = Math.min(act_row[j - 1], Math.min(old_row[j], old_row[j - 1]));
				else
					act_row[j] = Math.min(act_row[j - 1] + 1, Math.min(old_row[j] + 1, old_row[j - 1] + 1));
			}
		}

		return act_row[s2.length()];

	}

}

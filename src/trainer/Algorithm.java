package trainer;

import java.util.ArrayList;

public class Algorithm {

	static public int editDistance(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2))
			return 0;
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		int[] act_row = new int[s2.length() + 1];
		ArrayList<String> s1_list=new ArrayList<>();
		ArrayList<String> s2_list=new ArrayList<>();

		s1_list=stringToArrayList(s1);
		s2_list=stringToArrayList(s2);
		
		act_row=detemineEditDistance(s1_list, s2_list);


		return act_row[s2.length()];

	}

	public static ArrayList<String> stringToArrayList(String s1) {
		ArrayList<String> s1_list=new ArrayList<>();
		for(int i=0;i<s1.length();i++) {
			s1_list.add(""+s1.charAt(i));
		}
		return s1_list;
	}

	static public int editDistanceRomaji(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2))
			return 0;
		ArrayList<String> s1_list, s2_list;
		s1_list = new ArrayList<>();
		s2_list = new ArrayList<>();
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		s1_list = getSyllableArr(s1);
		s2_list = getSyllableArr(s2);

		int[] act_row = new int[s2_list.size() + 1];

		act_row=detemineEditDistance(s1_list, s2_list);

		return act_row[s2_list.size()];

	}

	public static int[] detemineEditDistance(ArrayList<String> s1_list, ArrayList<String> s2_list) {
		int[] old_row;
		int[] act_row = new int[s2_list.size()+1];
		for (int i = 0; i < s1_list.size() + 1; i++) {
			old_row = act_row.clone();
			for (int j = 0; j < s2_list.size() + 1; j++) {
				if (i == 0) {
					act_row[j] = j;
					continue;
				}
				if (j == 0) {
					act_row[j] += 1;
					continue;
				}
				if (s1_list.get(i - 1).equals(s2_list.get(j - 1)))
					act_row[j] = Math.min(act_row[j - 1], Math.min(old_row[j], old_row[j - 1]));
				else
					act_row[j] = Math.min(act_row[j - 1] + 1, Math.min(old_row[j] + 1, old_row[j - 1] + 1));
			}
		}
		return act_row;
	}

	public static ArrayList<String> getSyllableArr(String s1) {
		ArrayList<String> s1_list = new ArrayList<>();
		for (int i = 0; i < s1.length(); i++) {
			boolean isNextLetterVocal = i < s1.length() - 1 && (s1.charAt(i + 1) == 'a' || s1.charAt(i + 1) == 'e'
					|| s1.charAt(i + 1) == 'i' || s1.charAt(i + 1) == 'o' || s1.charAt(i + 1) == 'u');
			if (s1.charAt(i) == 'n' && !isNextLetterVocal)
				s1_list.add("" + s1.charAt(i));
			else if (s1.charAt(i) == 'a' || s1.charAt(i) == 'e' || s1.charAt(i) == 'i' || s1.charAt(i) == 'o'
					|| s1.charAt(i) == 'u')
				s1_list.add("" + s1.charAt(i));
			// check for shi
			else if (s1.charAt(i) == 's' && i < s1.length() - 1 && s1.charAt(i + 1) == 'h') {
				s1_list.add("" + s1.charAt(i) + s1.charAt(i + 1) + s1.charAt(i + 1));
				i += 2;
			}
			// check for chi
			else if (s1.charAt(i) == 'c') {
				s1_list.add("" + s1.charAt(i) + s1.charAt(i + 1) + s1.charAt(i + 1));
				i += 2;
			}
			// check for tsu
			else if (s1.charAt(i) == 't' && i < s1.length() - 2 && s1.charAt(i + 1) == 's') {
				s1_list.add("" + s1.charAt(i) + s1.charAt(i + 1) + s1.charAt(i + 1));
				i += 2;
			} else if (i < s1.length() - 1 && isNextLetterVocal) {
				s1_list.add("" + s1.charAt(i) + s1.charAt(i + 1));
				i++;
			} else {
				s1_list.add("" + s1.charAt(i));
			}
		}
		return s1_list;
	}

}

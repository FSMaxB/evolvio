package io.evolvio;

public class NameGenerator extends  GlobalScope {
	private static final int MIN_NAME_LENGTH = 3;
	private static final int MAX_NAME_LENGTH = 10;
	private static final float[] LETTER_FREQUENCIES = {8.167f, 1.492f, 2.782f, 4.253f, 12.702f, 2.228f, 2.015f, 6.094f, 6.966f, 0.153f, 0.772f, 4.025f, 2.406f, 6.749f,
		7.507f, 1.929f, 0.095f, 5.987f, 6.327f, 9.056f, 2.758f, 0.978f, 2.361f, 0.150f, 1.974f, 10000.0f};//0.074};


	static String stitchName(String[] s) {
		String result = "";
		for (int i = 0; i < s.length; i++) {
			float portion = ((float) s[i].length()) / s.length;
			int start = (int) min(max(round(portion * i), 0), s[i].length());
			int end = (int) min(max(round(portion * (i + 1)), 0), s[i].length());
			result = result + s[i].substring(start, end);
		}
		return result;
	}

	static String andifyParents(String[] s) {
		String result = "";
		for (int i = 0; i < s.length; i++) {
			if (i >= 1) {
				result = result + " & ";
			}
			result = result + capitalize(s[i]);
		}
		return result;
	}

	static String createNewName() {
		String nameSoFar = "";
		int chosenLength = (int) (random(MIN_NAME_LENGTH, MAX_NAME_LENGTH));
		for (int i = 0; i < chosenLength; i++) {
			nameSoFar += getRandomChar();
		}
		return sanitizeName(nameSoFar);
	}

	private static char getRandomChar() {
		float letterFactor = random(0, 100);
		int letterChoice = 0;
		while (letterFactor > 0) {
			letterFactor -= LETTER_FREQUENCIES[letterChoice];
			letterChoice++;
		}
		return (char) (letterChoice + 96);
	}

	static String sanitizeName(String input) {
		String output = "";
		int vowelsSoFar = 0;
		int consonantsSoFar = 0;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (isVowel(ch)) {
				consonantsSoFar = 0;
				vowelsSoFar++;
			} else {
				vowelsSoFar = 0;
				consonantsSoFar++;
			}
			if (vowelsSoFar <= 2 && consonantsSoFar <= 2) {
				output = output + ch;
			} else {
				double chanceOfAddingChar = 0.5;
				if (input.length() <= MIN_NAME_LENGTH) {
					chanceOfAddingChar = 1.0;
				} else if (input.length() >= MAX_NAME_LENGTH) {
					chanceOfAddingChar = 0.0;
				}
				if (random(0, 1) < chanceOfAddingChar) {
					char extraChar = ' ';
					while (extraChar == ' ' || (isVowel(ch) == isVowel(extraChar))) {
						extraChar = getRandomChar();
					}
					output = output + extraChar + ch;
					if (isVowel(ch)) {
						consonantsSoFar = 0;
						vowelsSoFar = 1;
					} else {
						consonantsSoFar = 1;
						vowelsSoFar = 0;
					}
				} else { // do nothing
				}
			}
		}
		return output;
	}

	static String capitalize(String n) {
		return n.substring(0, 1).toUpperCase() + n.substring(1);
	}

	private static boolean isVowel(char a) {
		return (a == 'a' || a == 'e' || a == 'i' || a == 'o' || a == 'u' || a == 'y');
	}

	static String mutateName(String input) {
		if (input.length() >= 3) {
			if (random(0, 1) < 0.2) {
				int removeIndex = (int) random(0, input.length());
				input = input.substring(0, removeIndex) + input.substring(removeIndex + 1);
			}
		}
		if (input.length() <= 9) {
			if (random(0, 1) < 0.2) {
				int insertIndex = (int) random(0, input.length() + 1);
				input = input.substring(0, insertIndex) + getRandomChar() + input.substring(insertIndex);
			}
		}
		int changeIndex = (int) random(0, input.length());
		input = input.substring(0, changeIndex) + getRandomChar() + input.substring(changeIndex + 1);
		return input;
	}
}

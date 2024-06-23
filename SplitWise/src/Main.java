import java.util.LinkedList;

public class Main {
    public int lengthOfLastWord(String s) {
        char[] arr = s.toCharArray();
        LinkedList<String> words = new LinkedList<>();
        StringBuilder currWord = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != ' ') {
                currWord.append(arr[i]);
            } else {
                words.add(currWord.toString());
                currWord.delete(0, currWord.length());
            }
        }

        if (!currWord.isEmpty()) {
            words.add(currWord.toString());
        }

        return words.getLast().length();
    }

    int ans = lengthOfLastWord("   fly me   to   the moon  ");

}
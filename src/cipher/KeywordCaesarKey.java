package cipher;

/**
 * Ключ шифру Цезаря з ключовим словом.
 *
 * Під рядком відкритого алфавіту, починаючи з позиції k, записується ключове
 * слово без повторних літер, після нього — решта літер алфавіту в природному
 * порядку; заповнення циклічне. Утворений рядок є шифрувальним алфавітом:
 * символ на позиції j замінює символ відкритого алфавіту з тією ж позицією.
 */
public final class KeywordCaesarKey {

    private final Alphabet alphabet;
    private final int shift;
    private final String keyword;
    private final String cipherAlphabet;

    public KeywordCaesarKey(Alphabet alphabet, int shift, String rawKeyword)
            throws CipherException {
        this.alphabet = alphabet;
        this.shift = validateShift(alphabet, shift);
        this.keyword = normalizeKeyword(alphabet, rawKeyword);
        this.cipherAlphabet = buildCipherAlphabet(alphabet, this.shift, this.keyword);
    }

    private static int validateShift(Alphabet alphabet, int shift)
            throws CipherException {
        int lastAllowed = alphabet.size() - 1;
        if (shift < 0 || shift > lastAllowed) {
            throw new CipherException("Зсув k має бути в межах від 0 до "
                    + lastAllowed + ", а введено " + shift + ".");
        }
        return shift;
    }

    /** Відкидає повторні входження символів і перевіряє їх належність алфавіту. */
    private static String normalizeKeyword(Alphabet alphabet, String rawKeyword)
            throws CipherException {
        if (rawKeyword == null || rawKeyword.trim().isEmpty()) {
            throw new CipherException("Ключове слово не може бути порожнім.");
        }
        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < rawKeyword.length(); i++) {
            char symbol = Character.toUpperCase(rawKeyword.charAt(i));
            if (!alphabet.contains(symbol)) {
                throw new CipherException("Символ '" + rawKeyword.charAt(i)
                        + "' з ключового слова відсутній в алфавіті.");
            }
            if (normalized.indexOf(String.valueOf(symbol)) < 0) {
                normalized.append(symbol);
            }
        }
        return normalized.toString();
    }

    private static String buildCipherAlphabet(Alphabet alphabet, int shift,
                                              String keyword) {
        int size = alphabet.size();
        char[] substitution = new char[size];

        // Від позиції k циклічно розміщується ключове слово.
        int position = shift;
        for (int i = 0; i < keyword.length(); i++) {
            substitution[position] = keyword.charAt(i);
            position = nextPosition(position, size);
        }

        // Далі так само циклічно дописуються літери, які не увійшли до слова.
        for (int i = 0; i < size; i++) {
            char symbol = alphabet.symbolAt(i);
            if (keyword.indexOf(symbol) < 0) {
                substitution[position] = symbol;
                position = nextPosition(position, size);
            }
        }
        return new String(substitution);
    }

    private static int nextPosition(int position, int size) {
        return (position + 1) % size;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public int getShift() {
        return shift;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCipherAlphabet() {
        return cipherAlphabet;
    }
}

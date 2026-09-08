package cipher;

/**
 * Шифр Цезаря з ключовим словом: моноалфавітна заміна, у якій символ
 * відкритого тексту замінюється символом шифрувального алфавіту з тієї ж
 * позиції. Клас не зберігає стану, тому придатний для будь-яких ключів.
 */
public final class KeywordCaesarCipher {

    public String encrypt(String text, KeywordCaesarKey key) {
        return substitute(text, key.getAlphabet().getSymbols(),
                key.getCipherAlphabet(), key.getAlphabet());
    }

    public String decrypt(String text, KeywordCaesarKey key) {
        return substitute(text, key.getCipherAlphabet(),
                key.getAlphabet().getSymbols(), key.getAlphabet());
    }

    /**
     * Символи поза алфавітом (пробіли, розділові знаки, цифри, переходи на
     * новий рядок) переносяться без змін — це зберігає структуру тексту
     * файлу. Регістр літери також зберігається.
     */
    private String substitute(String text, String from, String to,
                              Alphabet alphabet) {
        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char original = text.charAt(i);
            char upperCase = Character.toUpperCase(original);
            int position = from.indexOf(upperCase);

            if (position < 0 || position >= alphabet.size()) {
                result.append(original);
            } else {
                char replacement = to.charAt(position);
                result.append(Character.isLowerCase(original)
                        ? Character.toLowerCase(replacement)
                        : replacement);
            }
        }
        return result.toString();
    }
}

package cipher;

/**
 * Алфавіт, над яким виконується шифрування. Клас незмінний; символи не
 * дублюються — інакше підстановка не була б оборотною.
 */
public final class Alphabet {

    /** Позиція, яка повертається, якщо символ не належить алфавіту. */
    public static final int SYMBOL_NOT_FOUND = -1;

    private static final int MIN_SIZE = 2;

    private final String symbols;

    public Alphabet(String symbols) throws CipherException {
        if (symbols == null || symbols.length() < MIN_SIZE) {
            throw new CipherException("Алфавіт має містити щонайменше "
                    + MIN_SIZE + " символи.");
        }
        char duplicate = findDuplicate(symbols);
        if (duplicate != 0) {
            throw new CipherException("Алфавіт містить повторюваний символ: '"
                    + duplicate + "'. Кожен символ має бути унікальним.");
        }
        this.symbols = symbols;
    }

    private static char findDuplicate(String source) {
        for (int i = 0; i < source.length(); i++) {
            char current = source.charAt(i);
            if (source.indexOf(current, i + 1) != SYMBOL_NOT_FOUND) {
                return current;
            }
        }
        return 0;
    }

    public int size() {
        return symbols.length();
    }

    public char symbolAt(int index) {
        return symbols.charAt(index);
    }

    public int indexOf(char symbol) {
        return symbols.indexOf(symbol);
    }

    public boolean contains(char symbol) {
        return indexOf(symbol) != SYMBOL_NOT_FOUND;
    }

    public String getSymbols() {
        return symbols;
    }
}

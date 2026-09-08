package cipher;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Точка входу програми. Тут задані початкові значення параметрів; ключ
 * користувач змінює з меню під час роботи, без перекомпіляції.
 */
public final class Main {

    private static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int DEFAULT_SHIFT = 5;
    private static final String DEFAULT_KEYWORD = "CRYPTO";
    private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    private Main() {
    }

    public static void main(String[] args) {
        PrintStream output = new PrintStream(System.out, true, FILE_CHARSET);
        try {
            ConsoleUi ui = new ConsoleUi(
                    new Alphabet(ENGLISH_ALPHABET),
                    new KeywordCaesarCipher(),
                    new TextFileService(FILE_CHARSET),
                    new Scanner(System.in, FILE_CHARSET),
                    output,
                    DEFAULT_SHIFT,
                    DEFAULT_KEYWORD);
            ui.run();
        } catch (CipherException exception) {
            output.println("ПОМИЛКА: " + exception.getMessage());
        }
    }
}

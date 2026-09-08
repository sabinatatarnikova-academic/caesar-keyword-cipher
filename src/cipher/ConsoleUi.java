package cipher;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Консольний інтерфейс програми. Перед кожним показом меню виводяться
 * поточний ключ, відкритий і шифрувальний алфавіти, тому всі складові ключа
 * та обидва тексти видно в одному протоколі сеансу.
 */
public final class ConsoleUi {

    private static final String MENU_EXIT = "0";
    private static final String MENU_SET_KEY = "1";
    private static final String MENU_ENCRYPT_TEXT = "2";
    private static final String MENU_DECRYPT_TEXT = "3";
    private static final String MENU_ENCRYPT_FILE = "4";
    private static final String MENU_DECRYPT_FILE = "5";

    private static final String SEPARATOR =
            "-----------------------------------------------------------";

    private static final String PROMPT_CHOICE = "Ваш вибір: ";
    private static final String PROMPT_SHIFT = "Введіть зсув k: ";
    private static final String PROMPT_KEYWORD = "Введіть ключове слово: ";
    private static final String PROMPT_TEXT = "Введіть текст: ";
    private static final String PROMPT_SOURCE_FILE = "Файл-джерело: ";
    private static final String PROMPT_TARGET_FILE = "Файл-результат: ";

    private final Alphabet alphabet;
    private final KeywordCaesarCipher cipher;
    private final TextFileService fileService;
    private final Scanner input;
    private final PrintStream output;

    private KeywordCaesarKey key;

    public ConsoleUi(Alphabet alphabet,
                     KeywordCaesarCipher cipher,
                     TextFileService fileService,
                     Scanner input,
                     PrintStream output,
                     int defaultShift,
                     String defaultKeyword) throws CipherException {
        this.alphabet = alphabet;
        this.cipher = cipher;
        this.fileService = fileService;
        this.input = input;
        this.output = output;
        this.key = new KeywordCaesarKey(alphabet, defaultShift, defaultKeyword);
    }

    /** Основний цикл роботи програми. */
    public void run() {
        output.println(SEPARATOR);
        output.println("  Шифр Цезаря з ключовим словом");
        output.println(SEPARATOR);

        while (true) {
            printKeyState();
            printMenu();
            String choice = readLine(PROMPT_CHOICE);
            if (choice == null || choice.equals(MENU_EXIT)) {
                output.println("Роботу завершено.");
                return;
            }
            try {
                handleChoice(choice);
            } catch (CipherException exception) {
                output.println("ПОМИЛКА: " + exception.getMessage());
            }
        }
    }

    private void handleChoice(String choice) throws CipherException {
        switch (choice) {
            case MENU_SET_KEY:
                changeKey();
                break;
            case MENU_ENCRYPT_TEXT:
                transformText(true);
                break;
            case MENU_DECRYPT_TEXT:
                transformText(false);
                break;
            case MENU_ENCRYPT_FILE:
                transformFile(true);
                break;
            case MENU_DECRYPT_FILE:
                transformFile(false);
                break;
            default:
                output.println("ПОМИЛКА: немає пункту меню «" + choice + "».");
        }
    }

    private void printKeyState() {
        output.println();
        output.println("Поточний ключ: зсув k = " + key.getShift()
                + ", ключове слово = " + key.getKeyword());
        output.println("Відкритий алфавіт:    " + alphabet.getSymbols());
        output.println("Шифрувальний алфавіт: " + key.getCipherAlphabet());
    }

    private void printMenu() {
        output.println();
        output.println("  " + MENU_SET_KEY + " - задати ключ");
        output.println("  " + MENU_ENCRYPT_TEXT + " - зашифрувати введений текст");
        output.println("  " + MENU_DECRYPT_TEXT + " - розшифрувати введений текст");
        output.println("  " + MENU_ENCRYPT_FILE + " - зашифрувати вміст файлу");
        output.println("  " + MENU_DECRYPT_FILE + " - розшифрувати вміст файлу");
        output.println("  " + MENU_EXIT + " - завершити роботу");
    }

    private void changeKey() throws CipherException {
        int shift = readShift();
        String keyword = readLine(PROMPT_KEYWORD);
        key = new KeywordCaesarKey(alphabet, shift, keyword);
        output.println("Ключ прийнято.");
    }

    private int readShift() throws CipherException {
        String value = readLine(PROMPT_SHIFT);
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            throw new CipherException("Зсув k має бути цілим числом, а введено «"
                    + value.trim() + "».", exception);
        }
    }

    private void transformText(boolean encrypting) throws CipherException {
        String text = readLine(PROMPT_TEXT);
        if (text.isEmpty()) {
            throw new CipherException("Введено порожній текст.");
        }
        String result = encrypting
                ? cipher.encrypt(text, key)
                : cipher.decrypt(text, key);

        output.println(SEPARATOR);
        output.println("Вхідний текст: " + text);
        output.println("Результат:     " + result);
        output.println("Оброблено символів: " + text.length());
        output.println(SEPARATOR);
    }

    /** Перетворює вміст одного файлу і зберігає результат в інший. */
    private void transformFile(boolean encrypting) throws CipherException {
        File source = new File(readLine(PROMPT_SOURCE_FILE).trim());
        File target = new File(readLine(PROMPT_TARGET_FILE).trim());

        String text = fileService.read(source);
        if (text.isEmpty()) {
            throw new CipherException("Файл «" + source.getName() + "» порожній.");
        }
        String result = encrypting
                ? cipher.encrypt(text, key)
                : cipher.decrypt(text, key);
        fileService.write(target, result);

        output.println(SEPARATOR);
        output.println("Прочитано файл: " + source.getAbsolutePath());
        output.println("Вміст файлу:");
        output.println(text);
        output.println("Результат перетворення:");
        output.println(result);
        output.println("Записано файл: " + target.getAbsolutePath());
        output.println("Оброблено символів: " + text.length());
        output.println(SEPARATOR);
    }

    /** @return введений рядок або null, якщо введення завершилося */
    private String readLine(String prompt) {
        output.print(prompt);
        output.flush();
        if (!input.hasNextLine()) {
            output.println();
            return null;
        }
        return input.nextLine();
    }
}

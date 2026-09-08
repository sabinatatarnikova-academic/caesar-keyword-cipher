package cipher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Читання та запис текстових файлів. Усі помилки вводу-виводу
 * перетворюються на {@link CipherException} із зрозумілим повідомленням,
 * тому інтерфейс програми не працює з класами вводу-виводу напряму.
 */
public final class TextFileService {

    private final Charset charset;

    public TextFileService(Charset charset) {
        this.charset = charset;
    }

    public String read(File file) throws CipherException {
        if (!file.exists()) {
            throw new CipherException("Файл «" + file.getName()
                    + "» не знайдено.");
        }
        if (file.isDirectory()) {
            throw new CipherException("«" + file.getName()
                    + "» є каталогом, а не текстовим файлом.");
        }
        if (!file.canRead()) {
            throw new CipherException("Немає прав на читання файлу «"
                    + file.getName() + "».");
        }
        try {
            return new String(Files.readAllBytes(file.toPath()), charset);
        } catch (IOException exception) {
            throw new CipherException("Не вдалося прочитати файл «"
                    + file.getName() + "»: " + exception.getMessage(), exception);
        }
    }

    public void write(File file, String content) throws CipherException {
        if (file.exists() && !file.canWrite()) {
            throw new CipherException("Немає прав на запис у файл «"
                    + file.getName() + "».");
        }
        try {
            Files.write(file.toPath(), content.getBytes(charset));
        } catch (IOException exception) {
            throw new CipherException("Не вдалося записати файл «"
                    + file.getName() + "»: " + exception.getMessage(), exception);
        }
    }

    public Charset getCharset() {
        return charset;
    }
}

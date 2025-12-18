package MEDT.MEDT.vista.TextFormatters;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class TimeFormatter implements UnaryOperator<TextFormatter.Change> {

    // Regex permisiva:
    // Permite que la cadena empiece con un tiempo válido, pero no exige que termine.
    // HH:mm(Opcional: :ss)
    // El formato: \d{1,2}(:\d{1,2}(:\d{1,2})?)?
    private static final String TIME_FORMAT_PERMISSIVE =
            "^\\d{0,2}(:\\d{0,2}(:\\d{0,2})?)?$";

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        // 1. Manejar cadena vacía (permite borrar todo)
        if (newText.isEmpty()) {
            return change;
        }

        // 2. Límite de longitud (8 para HH:mm:ss, 5 para HH:mm)
        // Para ser más genéricos, permitiremos hasta 8 caracteres (ej. 23:59:59)
        if (newText.length() > 8) {
            return null;
        }

        // 3. Validación de formato permisivo
        if (newText.matches(TIME_FORMAT_PERMISSIVE)) {

            // 4. Validación de separadores (Asegurar que el usuario no escriba 1030)
            if (newText.length() >= 3 && newText.charAt(2) != ':') {
                return null; // El tercer caracter debe ser ':' si se llega a esa longitud
            }
            if (newText.length() >= 6 && newText.charAt(5) != ':') {
                return null; // El sexto caracter debe ser ':' si se llega a esa longitud
            }

            return change;
        }

        return null; // El texto no coincide con el patrón permisivo
    }

    public static TextFormatter<String> createTimeFormatter() {
        return new TextFormatter<>(new TimeFormatter());
    }
}

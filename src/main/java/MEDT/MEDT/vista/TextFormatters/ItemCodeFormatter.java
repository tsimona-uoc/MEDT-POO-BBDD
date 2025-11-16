package MEDT.MEDT.vista.TextFormatters;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class ItemCodeFormatter implements UnaryOperator<TextFormatter.Change> {

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        // Regex: comienza con una letra mayúscula, seguido de 1 o más dígitos
        if (newText.matches("[A-A]\\d*")) {
            return change;
        }
        return null; // descarta cualquier cambio inválido
    }
}

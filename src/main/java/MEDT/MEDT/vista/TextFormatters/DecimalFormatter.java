package MEDT.MEDT.vista.TextFormatters;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class DecimalFormatter implements UnaryOperator<TextFormatter.Change> {

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*(\\.\\d*)?")) {
            return change;
        }
        return null;
    }
}

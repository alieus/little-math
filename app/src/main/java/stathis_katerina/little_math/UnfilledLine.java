package stathis_katerina.little_math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Katerina on 8/1/2016.
 */
public class UnfilledLine {

    public static enum ElementType {
        CONSTANT, TEXT, LIST, NUMBER, INTEGER, NATURAL;
    }

    public static class Element {
        private String value;
        private final String correctValue;
        private final List<String> options;
        private final ElementType type;

        public Element(String value) {
            this.value = value;
            options = Collections.EMPTY_LIST;
            type = ElementType.CONSTANT;
            correctValue = value;
        }

        public Element(List<String> options, String defaultValue, String correctValue) {
            this.options = new ArrayList<>(options);
            value = defaultValue;
            type = ElementType.LIST;
            this.correctValue = correctValue;
        }

        public Element(String value, ElementType type, String correctValue) {
            this.value = value;
            this.type = type;
            options = Collections.EMPTY_LIST;
            this.correctValue = correctValue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCorrectValue() {
            return correctValue;
        }

        public List<String> getOptions() {
            return options;
        }

        public ElementType getType() {
            return type;
        }

        public boolean isCorrect() {
            if (type == ElementType.NUMBER) {
                return correctValue == null && value == null
                        || (Math.abs(Double.parseDouble(value) - Double.parseDouble(correctValue)) < 0.01);
            } else {
                return Objects.equals(value, correctValue);
            }
        }
    }

    private final List<Element> elements;

    public UnfilledLine() {
        elements = new ArrayList<>();
    }

    public UnfilledLine(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public List<Element> getElements() {
        return elements;
    }
}
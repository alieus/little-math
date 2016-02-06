package stathis_katerina.little_math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model that represents a line of (mathematical) text that is requested to be filled out, correctly, by the user.
 * The line may have some parts already filled.
 * The unfilled parts (elements) may be of various types such as numbers, free text or selection from a list.
 */
public class UnfilledLine {

    public static enum ElementType {

        /** Part of the line that is already filled */
        CONSTANT,
        /** Indicates that text of no special format is expected */
        TEXT,
        /** Indicates that a text value from a specified set of values is expected */
        LIST,
        /** Indicates that a real number is expected */
        NUMBER,
        /** Indicates that a non-negative real number is expected */
        UNSIGNED_NUMBER,
        /** Indicates that an integer is expected */
        INTEGER,
        /** Indicates that a non-negative integer is expected */
        NATURAL;
    }

    /**
     * Atomic part of the line that is either pre-filled or expected to be filled by the user.
     */
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

        /**
         * Returns the current value. May be changed by a call to {@link #setValue(String)}.
         * @return the current value
         */
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Returns the correct (expected) value. {@code null} indicates that all values are considered
         * accepted.
         * @return the correct value
         */
        public String getCorrectValue() {
            return correctValue;
        }

        /**
         * Returns the possible values if the element is of type {@link stathis_katerina.little_math.UnfilledLine.ElementType#LIST LIST},
         * an empty list otherwise
         * @return the list of possible values or an empty list
         */
        public List<String> getOptions() {
            return options;
        }

        public ElementType getType() {
            return type;
        }

        /**
         * Returns {@code true} iff this element is filled correctly. For most types, this is determined
         * by string equality between the value and the correct value. For floating point types
         * (NUMBER and UNSIGNED_NUMBER) this determined by their numerical difference, specifically,
         * by the formula: abs(value - correctValue) < 0.01.
         * @return {@code true} iff this element is filled correctly
         */
        public boolean isCorrect() {
            if (type == ElementType.NUMBER || type == ElementType.UNSIGNED_NUMBER) {
                try {
                    return correctValue == null
                            || value != null && !value.isEmpty()
                            && (Math.abs(Double.parseDouble(value) - Double.parseDouble(correctValue)) < 0.01);
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return correctValue == null || value != null && value.equals(correctValue);
            }
        }

        /**
         * Returns {@code true} iff this element has a value even if it is wrong.
         * @return {@code true} iff this element has a value
         */
        public boolean isFilled() {
            switch (type) {
                case NUMBER:
                case UNSIGNED_NUMBER:
                case INTEGER:
                case NATURAL:
                    if (value == null) {
                        return false;
                    }
                    try {
                        Double.parseDouble(value);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                default:
                    return value != null && !value.isEmpty();
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

    public boolean isAllCorrect() {
        for (Element elem : elements) {
            if (! elem.isCorrect()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllFilled() {
        for (Element elem : elements) {
            if (! elem.isFilled()) {
                return false;
            }
        }
        return true;
    }
}
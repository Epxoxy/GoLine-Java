package widget.helpers;

public class MarkExFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -1L;

    public MarkExFormatException () {
        super();
    }

    public MarkExFormatException (String s) {
        super (s);
    }

    public static MarkExFormatException forInputString(String s) {
        return new MarkExFormatException("For input string: \"" + s + "\"");
    }
}
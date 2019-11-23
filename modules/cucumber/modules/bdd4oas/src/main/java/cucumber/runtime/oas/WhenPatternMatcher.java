package cucumber.runtime.oas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhenPatternMatcher {
    private String when;

    private List<String> texts = new ArrayList<>();
    private List<String> args = new ArrayList<>();

    public WhenPatternMatcher(String when) {
        this.when = when;

        int ix = when.indexOf('{');
        while (ix >= 0) {
            int ix2 = when.indexOf('}', ix + 1);
            if (ix2 > ix + 1) {
                texts.add(when.substring(0, ix).trim());
                args.add(when.substring(ix + 1, ix2).trim());

                if (when.length() > ix2 + 1) {
                    when = when.substring(ix2 + 1).trim();
                    ix = when.indexOf('{');
                } else break;
            } else {
                if (!args.isEmpty()) {
                    texts.add(when);
                }
                break;
            }
        }
        if (when.length() > 0) {
            texts.add(when);
        }
    }

    public boolean matches(String text) {
        if (args.isEmpty()) {
            return when.equalsIgnoreCase(text);
        }

        try {
            return extractArguments(text).size() == args.size();
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, String> extractArguments(String text) throws Exception {
        HashMap<String, String> result = new HashMap<>();

        if (args.isEmpty()) {
            return result;
        }

        // single argument - no text?
        if (args.size() == 1 && texts.isEmpty()) {
            result.put(args.get(0), text);
            return result;
        }

        int ix = 0;
        while (ix < texts.size() && text.startsWith(texts.get(ix))) {
            text = text.substring(texts.get(ix).length()).trim();

            // extract argument
            if (text.length() > 0) {
                if (text.startsWith("\"")) {
                    int ix2 = text.indexOf('"', 1);

                    while (ix2 != -1 && text.charAt(ix2 - 1) == '\\') {
                        text = text.substring(0, ix2 - 1) + text.substring(ix2);
                        ix2 = text.indexOf('"', ix2);
                    }

                    if (ix2 == -1) {
                        return result;
                    } else {
                        result.put(args.get(ix), text.substring(1, ix2));
                        text = text.substring(ix2 + 1).trim();
                    }
                } else {
                    int ix2 = text.indexOf(' ');
                    if (ix2 > 0) {
                        result.put(args.get(ix), text.substring(0, ix2));
                        text = text.substring(ix2).trim();
                    } else {
                        return result;
                    }
                }
            }

            ix++;
        }

        if (ix == texts.size()) {
            return result;
        } else throw new Exception("Mismatch in text [" + texts.get(ix) + "]");
    }

    public boolean hasArguments() {
        return !args.isEmpty();
    }

    public Map<String, String> getOperationArguments(String stepText) {
        try {
            return extractArguments(stepText);
        } catch (Exception e) {
            return null;
        }
    }
}

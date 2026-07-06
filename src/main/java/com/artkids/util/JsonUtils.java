package com.artkids.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static Object parse(String json) {
        return new Parser(json == null ? "" : json).parse();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseObject(String json) {
        Object parsed = parse(json);
        if (parsed instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> parseArray(String json) {
        Object parsed = parse(json);
        if (parsed instanceof List<?> list) {
            return (List<Object>) list;
        }
        return List.of();
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String string) {
            return "\"" + escape(string) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof Map<?, ?> map) {
            List<String> entries = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                entries.add(stringify(String.valueOf(entry.getKey())) + ":" + stringify(entry.getValue()));
            }
            return "{" + String.join(",", entries) + "}";
        }
        if (value instanceof Collection<?> collection) {
            List<String> values = new ArrayList<>();
            for (Object item : collection) {
                values.add(stringify(item));
            }
            return "[" + String.join(",", values) + "]";
        }
        return stringify(String.valueOf(value));
    }

    public static String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    public static int asInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value == null || String.valueOf(value).isBlank()) {
            return 0;
        }
        return new BigDecimal(String.valueOf(value)).intValue();
    }

    public static boolean asBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        return value != null && Boolean.parseBoolean(String.valueOf(value));
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static final class Parser {
        private final String json;
        private int index;

        private Parser(String json) {
            this.json = json.trim();
        }

        private Object parse() {
            skipWhitespace();
            if (index >= json.length()) {
                return null;
            }
            return parseValue();
        }

        private Object parseValue() {
            skipWhitespace();
            if (index >= json.length()) {
                return null;
            }
            char current = json.charAt(index);
            if (current == '{') {
                return parseObject();
            }
            if (current == '[') {
                return parseArray();
            }
            if (current == '"') {
                return parseString();
            }
            if (current == 't' || current == 'f') {
                return parseBoolean();
            }
            if (current == 'n') {
                index += 4;
                return null;
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            index++;
            skipWhitespace();
            while (index < json.length() && json.charAt(index) != '}') {
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (index < json.length() && json.charAt(index) == ',') {
                    index++;
                    skipWhitespace();
                }
            }
            expect('}');
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            index++;
            skipWhitespace();
            while (index < json.length() && json.charAt(index) != ']') {
                list.add(parseValue());
                skipWhitespace();
                if (index < json.length() && json.charAt(index) == ',') {
                    index++;
                    skipWhitespace();
                }
            }
            expect(']');
            return list;
        }

        private String parseString() {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (index < json.length()) {
                char current = json.charAt(index++);
                if (current == '"') {
                    break;
                }
                if (current == '\\' && index < json.length()) {
                    char escaped = json.charAt(index++);
                    builder.append(switch (escaped) {
                        case '"' -> '"';
                        case '\\' -> '\\';
                        case '/' -> '/';
                        case 'b' -> '\b';
                        case 'f' -> '\f';
                        case 'n' -> '\n';
                        case 'r' -> '\r';
                        case 't' -> '\t';
                        case 'u' -> parseUnicode();
                        default -> escaped;
                    });
                } else {
                    builder.append(current);
                }
            }
            return builder.toString();
        }

        private char parseUnicode() {
            String hex = json.substring(index, Math.min(index + 4, json.length()));
            index += Math.min(4, hex.length());
            return (char) Integer.parseInt(hex, 16);
        }

        private Boolean parseBoolean() {
            if (json.startsWith("true", index)) {
                index += 4;
                return true;
            }
            index += 5;
            return false;
        }

        private Number parseNumber() {
            int start = index;
            while (index < json.length()) {
                char current = json.charAt(index);
                if ((current >= '0' && current <= '9') || current == '-' || current == '+'
                        || current == '.' || current == 'e' || current == 'E') {
                    index++;
                } else {
                    break;
                }
            }
            String number = json.substring(start, index);
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                return new BigDecimal(number);
            }
            return Long.parseLong(number);
        }

        private void expect(char expected) {
            skipWhitespace();
            if (index < json.length() && json.charAt(index) == expected) {
                index++;
            }
        }

        private void skipWhitespace() {
            while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
                index++;
            }
        }
    }
}

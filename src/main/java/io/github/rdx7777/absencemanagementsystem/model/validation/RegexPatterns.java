package io.github.rdx7777.absencemanagementsystem.model.validation;

import java.util.regex.Pattern;

public class RegexPatterns {

    private static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    static boolean matchesEmailPattern(String email) {
        return emailPattern.matcher(email).matches();
    }
}

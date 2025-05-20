package proguard.obfuscate;

import java.util.regex.Pattern;

/**
 * A very simplistic package rename rule.
 */
public final class PackageRenameRule {
    private final Pattern pattern;
    private final String replacement;

    public PackageRenameRule(Pattern pattern, String replacement) {
        this.pattern = pattern;
        this.replacement = replacement;
    }

    @Override
    public String toString() {
        return pattern + "=>" + replacement;
    }

    public boolean shouldRename(String name) {
        return pattern.matcher(name).find();
    }

    public String getNewClassName(String name) {
        return pattern.matcher(name).replaceAll(replacement);
    }
}

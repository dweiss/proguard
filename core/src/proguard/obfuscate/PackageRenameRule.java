package proguard.obfuscate;

/**
 * A very simplistic package rename rule.
 */
public final class PackageRenameRule
{
    public final String packagePrefix;
    public final String addPrefix;

    public PackageRenameRule(String packagePrefix, String addPrefix)
    {
        this.packagePrefix = packagePrefix;
        this.addPrefix = addPrefix;
    }
    
    @Override
    public String toString()
    {
        return packagePrefix + "=>" + addPrefix;
    }
}

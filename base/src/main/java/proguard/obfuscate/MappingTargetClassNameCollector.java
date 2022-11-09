package proguard.obfuscate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.WarningPrinter;

/**
 * Collects target class name mappings from a class mapping file (so that these
 * names can be avoided during obfuscation).
 *
 * @author Dawid Weiss
 * @see "https://sourceforge.net/p/proguard/bugs/653/"
 */
public class MappingTargetClassNameCollector implements MappingProcessor {
  private final WarningPrinter warningPrinter;

  // Map: [newClassName -> original className]
  private final Map classNameMapping = new LinkedHashMap();

  public MappingTargetClassNameCollector(WarningPrinter warningPrinter) {
    this.warningPrinter = warningPrinter;
  }

  @Override
  public boolean processClassMapping(String className,
                                     String newClassName) {
    String internalNewClassName = ClassUtil.internalClassName(newClassName);
    String otherClassName = (String) classNameMapping.put(internalNewClassName, className);
    if (otherClassName != null && !className.equals(otherClassName)) {
      // TODO: change it to issue warnings to warning printer?
      throw new RuntimeException("Duplicate target '" +
          newClassName + "' for two different classes: '" + className + "' and '" +
          otherClassName + "'.");
    }

    return false;
  }

  @Override
  public void processFieldMapping(String className,
                                  String fieldType,
                                  String fieldName,
                                  String newClassName,
                                  String newFieldName) {
  }

  @Override
  public void processMethodMapping(String className,
                                   int firstLineNumber,
                                   int lastLineNumber,
                                   String methodReturnType,
                                   String methodName,
                                   String methodArguments,
                                   String newClassName,
                                   int newFirstLineNumber,
                                   int newLastLineNumber,
                                   String newMethodName) {
  }

  public Set getReservedClassNames() {
    return classNameMapping.keySet();
  }
}

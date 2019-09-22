/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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

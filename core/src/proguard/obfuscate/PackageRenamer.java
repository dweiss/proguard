package proguard.obfuscate;

import java.util.List;

import proguard.Configuration;
import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;

/**
 * Generates synthetic class/ field/ method mapping. 
 */
public class PackageRenamer implements ClassVisitor, MemberVisitor
{
    private final MappingProcessor keeper;
    private String className;
    private List renameRules;

    public PackageRenamer(MappingProcessor keeper, Configuration configuration)
    {
        this.keeper = keeper;
        this.renameRules = configuration.renamePackages;
    }

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        String name = ClassUtil.externalClassName(programClass.getName());
        
        for (int i = 0; i < renameRules.size(); i++)
        {
            PackageRenameRule rule = (PackageRenameRule) renameRules.get(i);
            if (name.startsWith(rule.packagePrefix))
            {
                String newClassName = rule.addPrefix + "." + name;
                this.className = programClass.getName();
                if (keeper.processClassMapping(className, newClassName))
                {
                    programClass.fieldsAccept(this);
                    programClass.methodsAccept(this);
                }
                
                break;
            }
        }
    }

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        keeper.processFieldMapping(
            className, 
            ClassUtil.externalType(programField.getDescriptor(programClass)),
            programField.getName(programClass),
            className,
            programField.getName(programClass));
            
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        keeper.processMethodMapping(
            className,
            0, 
            0,
            ClassUtil.externalMethodReturnType(programMethod.getDescriptor(programClass)),
            programMethod.getName(programClass),
            ClassUtil.externalMethodArguments(programMethod.getDescriptor(programClass)),
            className,
            0,
            0,
            programMethod.getName(programClass));
    }
    
    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        // Skip.
    }
    
    @Override
    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        // Skip.
    }

    @Override
    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        // Skip.
    }
}

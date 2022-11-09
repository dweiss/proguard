/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard.obfuscate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.visitor.ClassCleaner;
import proguard.pass.Pass;
import proguard.util.PrintWriterUtil;

import java.io.*;

public class ObfuscationPreparation implements Pass
{
    private static final Logger logger = LogManager.getLogger(ObfuscationPreparation.class);

    private final Configuration configuration;

    public ObfuscationPreparation(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView) throws IOException
    {
        // We'll apply a mapping, if requested.
        if (configuration.applyMapping != null)
        {
            logger.info("Applying mapping from [{}]...", PrintWriterUtil.fileName(configuration.applyMapping));
        }

        // Check if we have at least some keep commands.
        if (configuration.keep         == null &&
            configuration.applyMapping == null &&
            configuration.printMapping == null)
        {
            throw new IOException("You have to specify '-keep' options for the obfuscation step.");
        }

        // Clean up any old processing info.
        appView.programClassPool.classesAccept(new ClassCleaner());
        appView.libraryClassPool.classesAccept(new ClassCleaner());
    }
}

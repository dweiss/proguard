ProGuard clone with support for package renaming
================================================

This is a fork of ProGuard with custom bug fixes and a tiny extension 
(on the 'renamer' branch) to support easier renaming of entire packages.

Changes compared to plain ProGuard distribution:

  * Bug fix for: applymapping ignores names not present in input libraries
    (https://sourceforge.net/p/proguard/bugs/653/) 
  * Added a 'renamepackage' option to repackage entire packages. For example:
    ```
        -renamepackage com.google=>com.acme.dependencies.google
        -renamepackage com.foo=>com.acme.dependencies.foo
    ```

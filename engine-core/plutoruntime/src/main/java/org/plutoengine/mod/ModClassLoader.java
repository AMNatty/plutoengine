/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.mod;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;
import org.plutoengine.resource.filesystem.ResourceManager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @deprecated Reserved for future use
 * @author 493msi
 */
@Deprecated
public class ModClassLoader
{
    public static void loadJars() throws IOException
    {
        Logger.log(SmartSeverity.MODULE, "Looking for installed mods.");

        var modContainingDir = ResourceManager.MOD_DIRECTORY;

        if (!Files.isDirectory(modContainingDir))
        {
            Files.createDirectories(modContainingDir);

            Logger.logf(SmartSeverity.MODULE, "No mods found in '%s'.%n", modContainingDir.toAbsolutePath());

            return;
        }

        int loadedJars = 0;

        try (var modDirs = Files.list(modContainingDir))
        {
            for (var it = modDirs.filter(Files::isDirectory).iterator(); it.hasNext();)
            {
                var modDir = it.next();

                var modJar = modDir.resolve("mod.jar");

                if (!Files.isRegularFile(modJar))
                {
                    Logger.logf(SmartSeverity.MODULE_WARNING, "Failed to load mod jar file: '%s'.%n", modJar.toAbsolutePath());
                    Logger.log(SmartSeverity.MODULE_WARNING, "Reason: Missing mod jar file.");

                    return;
                }

                var dataDir = modDir.resolve("data");

                if (!Files.isDirectory(dataDir))
                    Files.createDirectories(dataDir);

                classLoadJar(modJar);

                Logger.logf(SmartSeverity.MODULE_PLUS, "Loaded mod jar file: %s%n", modJar.toAbsolutePath());

                loadedJars++;
            }
        }


        Logger.logf(SmartSeverity.MODULE, "Mod loading complete, loaded %d mod/s from %d jar file/s.%n", loadedJars);
    }

    private static PlutoClassLoader classLoadJar(Path jarPath) throws IOException
    {
        try (var jarFile = new JarFile(jarPath.toFile()))
        {
            Enumeration<JarEntry> e = jarFile.entries();

            var classLoader = new PlutoClassLoader();

            while (e.hasMoreElements())
            {
                JarEntry je = e.nextElement();

                if (je.isDirectory())
                {
                    continue;
                }

                var entryName = je.getName();

                var resourcePath = String.format("file:%s!/%s", jarPath.toAbsolutePath(), entryName);
                var resourceURL = new URL("jar", null, resourcePath);

                classLoader.createResource(entryName, resourceURL);

                if (!entryName.endsWith(".class"))
                {
                    continue;
                }

                var is = jarFile.getInputStream(je);
                var classBytes = is.readAllBytes();
                is.close();

                var className = entryName
                    .replaceAll("\\.class$", "")
                    .replace('/', '.');

                classLoader.createClass(className, classBytes);
            }

            return classLoader;
        }
    }

    public static class PlutoClassLoader extends ClassLoader
    {
        private final Map<String, Class<?>> classMap;
        private final Map<String, URL> resourceMap;

        private PlutoClassLoader()
        {
            this.classMap = new HashMap<>();
            this.resourceMap = new HashMap<>();
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
        {
            var clazz = this.classMap.get(name);

            if (clazz == null)
            {
                throw new ClassNotFoundException(String.format("Undefined class: '%s'", name));
            }

            return clazz;
        }

        public Collection<Class<?>> getClasses()
        {
            return Collections.unmodifiableCollection(classMap.values());
        }

        @Override
        protected URL findResource(String name)
        {
            return this.resourceMap.get(name);
        }

        @Override
        protected Enumeration<URL> findResources(String name)
        {
            var resource = this.findResource(name);
            return resource == null ? Collections.emptyEnumeration() : Collections.enumeration(List.of(resource));
        }

        private void createClass(String name, byte[] data)
        {
            var clazz = this.defineClass(name, data, 0, data.length);
            this.resolveClass(clazz);
            this.classMap.put(name, clazz);
        }

        private void createResource(String name, URL url)
        {
            this.resourceMap.put(name, url);
        }
    }
}

package cz.tefek.io.modloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.tefek.io.asl.resource.ResourceHelper;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.Severity;

/**
 * Unzips mod packages from the packages folder into the mods folder. WIP
 *
 * @author 493msi
 */
public class ModInstaller
{
    public static void unpackNewMods()
    {
        Logger.log("[Pluto Mod Loader] Looking for new mod packages to install.");

        File f = new File(ResourceHelper.GLOBAL_ROOT + "packages/");
        if (!f.exists())
        {
            f.mkdir();

            Logger.log("[Pluto Mod Loader] Package folder does not exist, creating it and aborting unpacking.");

            return;
        }

        ArrayList<String> files = new ArrayList<String>(Arrays.asList(f.list()));

        if (files.size() == 0)
        {
            Logger.log("[Pluto Mod Loader] No mod package found.");
        }
        else
        {
            Logger.log("[Pluto Mod Loader] Found " + files.size() + " mod packages.");

            for (String file : files)
            {
                Logger.log("[Pluto Mod Loader] Mod package found: " + file + ", installing it.");

                try
                {
                    extract(file);
                }
                catch (IOException e)
                {
                    Logger.log(Severity.ERROR, "Unpacking of " + file + " failed!");
                    Logger.logException(e);
                }

                new File(ResourceHelper.GLOBAL_ROOT + "packages/" + file).delete();
            }
        }
    }

    private static void extract(String filepath) throws IOException
    {
        byte[] buffer = new byte[2048];

        InputStream fileInput = null;
        fileInput = new FileInputStream(ResourceHelper.GLOBAL_ROOT + "packages/" + filepath);

        ZipInputStream stream = new ZipInputStream(fileInput);
        String outdir = ResourceHelper.GLOBAL_ROOT + "mods/";

        if (!new File(outdir).exists())
        {
            new File(outdir).mkdir();
        }

        try
        {
            String filename = filepath.replaceAll("\\.zip$", "");

            new File(outdir + filename).mkdir();

            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null)
            {
                String outpath = outdir + filename + "/" + entry.getName();
                FileOutputStream output = null;
                try
                {
                    output = new FileOutputStream(outpath);
                    int len = 0;
                    while ((len = stream.read(buffer)) > 0)
                    {
                        output.write(buffer, 0, len);
                    }
                }
                finally
                {
                    if (output != null)
                    {
                        output.close();
                    }
                }
            }
        }
        finally
        {
            stream.close();
        }
    }
}

package cz.tefek.io.pluto.pp;

public class PlutoPackage
{
    public final String id;

    protected String name;
    protected String version;
    protected int build;
    protected String description;
    protected String iconURL;
    protected String author;
    protected Class<?>[] dependencies;
    protected int earliestCompatibleBuild;

    public PlutoPackage(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return this.version;
    }

    void setVersion(String version)
    {
        this.version = version;
    }

    public int getBuild()
    {
        return this.build;
    }

    void setBuild(int build)
    {
        this.build = build;
    }

    public String getDescription()
    {
        return this.description;
    }

    void setDescription(String description)
    {
        this.description = description;
    }

    public String getIconURL()
    {
        return this.iconURL;
    }

    void setIconURL(String iconURL)
    {
        this.iconURL = iconURL;
    }

    public String getAuthor()
    {
        return this.author;
    }

    void setAuthor(String author)
    {
        this.author = author;
    }

    public Class<?>[] getDependencies()
    {
        return this.dependencies;
    }

    public boolean isBackwardsCompatibleWithBuild(int build)
    {
        return build > this.earliestCompatibleBuild;
    }

    public boolean isMod()
    {
        return false;
    }
}

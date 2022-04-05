package org.plutoengine.mod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ext.NioPathDeserializer;
import org.plutoengine.address.VirtualAddress;
import org.plutoengine.resource.filesystem.EnumBackingFileSystem;

import java.nio.file.Path;
import java.util.Map;

public record ModManifest(String displayName,
                          String description,
                          String author,
                          @JsonDeserialize(keyAs = VirtualAddress.class, contentAs = ResourceRootInfo.class) Map<VirtualAddress, ResourceRootInfo> resourceRoots)
{
    public record ResourceRootInfo(
        EnumBackingFileSystem type,
        @JsonDeserialize(using = NioPathDeserializer.class) Path path
    )
    {

    }
}

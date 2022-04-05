package org.plutoengine.resource.filesystem;

import org.plutoengine.address.VirtualAddress;

record UnresolvedResourcePath(
    VirtualAddress modID,
    VirtualAddress containerID,
    boolean relative,
    VirtualAddress pathAddress,
    String extension
)
{
}

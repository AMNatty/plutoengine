package org.plutoengine.io.property;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class YAMLPropertiesReader
{
    public static Map<String, String> loadFromFile(Path file) throws IOException, YAMLException
    {
        try (var br = Files.newBufferedReader(file))
        {
            var yaml = new Yaml(new Constructor() {
                private void recursivelyBuildTree(Map<String, String> map, String accessor, Node node)
                {
                    if (node instanceof MappingNode mappingNode)
                    {
                        var kvps = mappingNode.getValue();

                        for (var kvp : kvps)
                        {
                            var key = kvp.getKeyNode();

                            if (key.getTag() != Tag.STR || !(key instanceof ScalarNode))
                                throw new YAMLException("All keys in property trees must be strings!");

                            var valueNode = kvp.getValueNode();

                            var newAccessorFormat = valueNode instanceof MappingNode ? "%s%s." : "%s%s";

                            this.recursivelyBuildTree(map, newAccessorFormat.formatted(accessor, super.constructScalar((ScalarNode) key)), valueNode);
                        }
                    }
                    else if (node instanceof ScalarNode scalarNode)
                    {
                        map.put(accessor, super.constructScalar(scalarNode));
                    }
                    else
                    {
                        throw new YAMLException("Invalid node tag: %s".formatted(node.getTag()));
                    }
                }

                @Override
                protected Object constructObject(Node node)
                {
                    var propertyMap = new LinkedHashMap<String, String>();
                    this.recursivelyBuildTree(propertyMap, "", node);
                    return propertyMap;
                }
            });

            return yaml.load(br);
        }
    }
}

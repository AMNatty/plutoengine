package cz.tefek.io.asl.resource;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ResourceAddressTypeAdapter extends TypeAdapter<ResourceAddress>
{
    @Override
    public void write(JsonWriter out, ResourceAddress value) throws IOException
    {
        out.value(String.valueOf(value));
    }

    @Override
    public ResourceAddress read(JsonReader in) throws IOException
    {
        var strVal = in.nextString();
        return "null".equalsIgnoreCase(strVal) ? null : ResourceAddress.parse(strVal);
    }

}

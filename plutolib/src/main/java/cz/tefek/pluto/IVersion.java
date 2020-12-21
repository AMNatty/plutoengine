package cz.tefek.pluto;

public interface IVersion<T> extends Comparable<T>
{
    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}

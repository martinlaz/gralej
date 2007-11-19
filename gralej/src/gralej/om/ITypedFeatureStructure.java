package gralej.om;

public interface ITypedFeatureStructure extends IEntity {
    String typeName();
    Iterable<IFeatureValuePair> featureValuePairs();
    boolean isSpecies();
}

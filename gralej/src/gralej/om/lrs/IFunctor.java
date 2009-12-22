package gralej.om.lrs;

/**
 *
 * @author Martin
 */
public interface IFunctor extends INamedTerm {
    Iterable<ITerm> args();
    ITerm arg(int i);
    int arity();
}

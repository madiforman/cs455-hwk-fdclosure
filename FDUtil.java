import java.util.Set;
import java.util.HashSet;
import java.util.*;
/**
 * This utility class is not meant to be instantitated, and just provides some
 * useful methods on FD sets.
 * 
 * @author Madison Sanchez-Forman
 * @version October 28, 2022
 */
public final class FDUtil {

  /**
   * Resolves all trivial FDs in the given set of FDs
   * 
   * @param fdset (Immutable) FD Set
   * @return a set of trivial FDs with respect to the given FDSet
   */
  public static FDSet trivial(final FDSet fdset) {
    FDSet copy = new FDSet(fdset);
    FDSet trivial_set = new FDSet();
    FD trivials = null;
    for(FD f : copy.getSet()){
      Set<Set<String>> power_set = powerSet(f.getLeft());
      for(Set<String> inner_set : power_set){
        if(!inner_set.isEmpty()){
          trivials = new FD(f.getLeft(), inner_set);
          trivial_set.add(trivials);
       }
      }
    }
    return trivial_set;
  }
  /**
   * Augments every FD in the given set of FDs with the given attributes
   * 
   * @param fdset FD Set (Immutable)
   * @param attrs a set of attributes with which to augment FDs (Immutable)
   * @return a set of augmented FDs
   */
  public static FDSet augment(final FDSet fdset, final Set<String> attrs) {
    FDSet augmented_set = new FDSet(fdset);
    for(FD F: augmented_set.getSet()){
      F.addToLeft(attrs);
      F.addToRight(attrs);
    }
    return augmented_set;
  }

  /**
   * Exhaustively resolves transitive FDs with respect to the given set of FDs
   * 
   * @param fdset (Immutable) FD Set
   * @return all transitive FDs with respect to the input FD set
   */
  public static FDSet transitive(final FDSet fdset) {
    // TODO: Examine each pair of FDs in the given set. If the transitive property
    // holds on the pair of FDs, then generate the new FD and add it to a new FDSet.
    // Repeat until no new transitive FDs are found.
    FDSet copy = new FDSet(fdset);
    FDSet IDK = new FDSet();
    for(FD F: copy.getSet()){
      Set<String> left = F.getLeft();
      Set<String> right = F.getRight();
  System.out.println(F.getRight());
        System.out.println(F.getLeft());
      

    }


    return null;
  }

  /**
   * Generates the closure of the given FD Set
   * 
   * @param fdset (Immutable) FD Set
   * @return the closure of the input FD Set
   */
  public static FDSet fdSetClosure(final FDSet fdset) {
    // TODO: Use the FDSet copy constructor to deep copy the given FDSet

    // TODO: Generate new FDs by applying Trivial and Augmentation Rules, followed
    // by Transitivity Rule, and add new FDs to the result.
    // Repeat until no further changes are detected.

    return null;
  }

  /**
   * Generates the power set of the given set (that is, all subsets of
   * the given set of elements)
   * 
   * @param set Any set of elements (Immutable)
   * @return the power set of the input set
   */
  @SuppressWarnings("unchecked")
  public static <E> Set<Set<E>> powerSet(final Set<E> set) {

    // base case: power set of the empty set is the set containing the empty set
    if (set.size() == 0) {
      Set<Set<E>> basePset = new HashSet<>();
      basePset.add(new HashSet<>());
      return basePset;
    }

    // remove the first element from the current set
    E[] attrs = (E[]) set.toArray();
    set.remove(attrs[0]);

    // recurse and obtain the power set of the reduced set of elements
    Set<Set<E>> currentPset = FDUtil.powerSet(set);

    // restore the element from input set
    set.add(attrs[0]);

    // iterate through all elements of current power set and union with first
    // element
    Set<Set<E>> otherPset = new HashSet<>();
    for (Set<E> attrSet : currentPset) {
      Set<E> otherAttrSet = new HashSet<>(attrSet);
      otherAttrSet.add(attrs[0]);
      otherPset.add(otherAttrSet);
    }
    currentPset.addAll(otherPset);
    return currentPset;
  }
}
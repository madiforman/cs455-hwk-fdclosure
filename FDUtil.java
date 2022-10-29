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
    FDSet copy = new FDSet(fdset); //copy for iterating
    FDSet trivial_set = new FDSet(); // where the final set will be returned
    for(FD fd : copy.getSet()){
      Set<Set<String>> power_set = powerSet(fd.getLeft()); 
      for(Set<String> inner_set : power_set){
        if(inner_set.size() > 0){
          trivial_set.add(new FD(fd.getLeft(), inner_set));
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
    for(FD f1: fdset){
      FD fd = new FD(f1);
      fd.addToLeft(attrs);
      fd.addToRight(attrs);
      augmented_set.add(fd);
    }
    return augmented_set;
  }

/**
  * Exhaustively resolves transitive FDs with respect to the given set of FDs
  * 
  * @param fdset (Immutable) FD Set
  * @return all transitive FDs with respect to the input FD set
  */
public static FDSet transitive(final FDSet fdset){
  FDSet copy = new FDSet(fdset);
  FDSet other_copy = new FDSet(fdset);
  for(FD f1: fdset){
    for(FD f2: fdset){
      if(is_transitive(f1, f2)){
        FD new_fd = new FD(f1.getLeft(), f2.getRight());
        copy.add(new_fd);

      }
    }
  }
  return copy;
}
/**
  * Checks if two fds have a transitive relationship
  * 
  * @param FD f1
  * @param FD f2
  * @return true if transitive relationship holds false otherwise
  */
public static boolean is_transitive(FD f1, FD f2){
  if(f2.leftContains(f1.getRight())){
    return true;
  }
  return false;
}
/**
  * gets all attributes from a given FDSet
  * 
  * @param FDSet fdset
  * @return Set<Set<String>> power set of all attributes within fdset
  */
public static Set<Set<String>> get_attributes(FDSet fdset){
  Set<String> attributes = new HashSet<>();
  for(FD fd : fdset){
    attributes.addAll(fd.getLeft());
    attributes.addAll(fd.getRight());
  }
  Set<Set<String>> all_attributes = powerSet(attributes);
  return all_attributes;
}
  /**
   * Generates the closure of the given FD Set
   * 
   * @param fdset (Immutable) FD Set
   * @return the closure of the input FD Set
   */
  public static FDSet fdSetClosure(final FDSet fdset) {
    FDSet copy = new FDSet(fdset); //copy input set
    boolean flag = true; //flag used to stop iterating when needed
    do {
      FDSet trivial_set = trivial(copy); //i store the trivial set, augmented set, and transitive set in temporary variables to not lose/change information in copy
      FDSet augment_set = new FDSet(copy);
      FDSet transitive_set = transitive(copy);

      Set<Set<String>> all_attributes = get_attributes(copy);
      for(Set<String> inner_set : all_attributes){
        if(inner_set.size()!= 0){ //skip empty
          augment_set = augment(copy, inner_set); //augment fdset with each attribute in the power set (besides empty ones)
        }
      }
      if(!copy.getSet().containsAll(trivial_set.getSet())){ //if copy doesnt contain the trivial set
        copy.addAll(trivial_set);
      } 
      else if(!copy.getSet().containsAll(augment_set.getSet())){ //or the augmented set
        copy.addAll(augment_set);
      }
     else if(!copy.getSet().containsAll(transitive_set.getSet())){ //or the transitive set
        copy.addAll(transitive_set);
      } else {
        flag = false; //if it did contain those, we have finished changing copy and have found the set closure of fdset
      }
    } while(flag);
    return copy; 
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
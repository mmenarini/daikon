package daikon.inv.unary;

import daikon.*;
import daikon.inv.*;
import daikon.derive.unary.*;

import java.util.*;

// *****
// Automatically generated from Bound.java.cpp
// *****

// One reason not to combine LowerBound and Upperbound is that they have
// separate justifications:  one may be justified when the other is not.

// What should we do if there are few values in the range?
// This can make justifying that invariant easier, because with few values
// naturally there are more instances of each value.
// This might also make justifying that invariant harder, because to get more
// than (say) twice the expected number of samples (under the assumption of
// uniform distribution) requires many samples.
// Which of these dominates?  Is the behavior what I want?

public class UpperBoundCore  implements java.io.Serializable {

  final static int required_samples_at_bound = 3;

  // max1  >  max2  >  max3 
  public long max1  = Long.MIN_VALUE ;
  int num_max1  = 0;
  long max2  = Long.MIN_VALUE ;
  int num_max2  = 0;
  long max3  = Long.MIN_VALUE ;
  int num_max3  = 0;
  long min  = Long.MAX_VALUE ;

  int samples = 0;

  Invariant wrapper;

  public UpperBoundCore (Invariant wrapper) {
    this.wrapper = wrapper;
  }

  public String repr() {
    return "max1=" + max1 
      + ", num_max1=" + num_max1 
      + ", max2=" + max2 
      + ", num_max2=" + num_max2 
      + ", max3=" + max3 
      + ", num_max3=" + num_max3 
      + ", min=" + min ;
  }

  public void add_modified(long value, int count) {
    samples += count;

    // System.out.println("UpperBoundCore"  + varNames() + ": "
    //                    + "add(" + value + ", " + modified + ", " + count + ")");

    long v = value;

    if (v <  min ) min  = v;

    if (v == max1 ) {
      num_max1  += count;
    } else if (v >  max1 ) {
      max3  = max2 ;
      num_max3  = num_max2 ;
      max2  = max1 ;
      num_max2  = num_max1 ;
      max1  = v;
      num_max1  = count;
    } else if (v == max2 ) {
      num_max2  += count;
    } else if (v >  max2 ) {
      max3  = max2 ;
      num_max3  = num_max2 ;
      max2  = v;
      num_max2  = count;
    } else if (v == max3 ) {
      num_max3  += count;
    } else if (v >  max3 ) {
      max3  = v;
      num_max3  = count;
    }
  }

  public double computeProbability() {
    if (num_max1  < required_samples_at_bound)
      return Invariant.PROBABILITY_UNKNOWN;

    long modulus = 1;
    // Need to reinstate this at some point.
    // {
    //   for (Iterator itor = wrapper.ppt.invs.iterator(); itor.hasNext(); ) {
    //     Invariant inv = (Invariant) itor.next();
    //     if ((inv instanceof Modulus) && inv.justified()) {
    //       modulus = ((Modulus) inv).modulus;
    //       break;
    //     }
    //   }
    // }

    // Accept a bound if:
    //  * it contains more than twice as many elements as it ought to by
    //    chance alone, and that number is at least 3.
    //  * it and its predecessor/successor both contain more than half
    //    as many elements as they ought to by chance alone, and at
    //    least 3.

    // If I used Math.abs, the order of arguments to minus would not matter.
    long range = - (min  - max1 ) + 1;
    double avg_samples_per_val = ((double) wrapper.ppt.num_mod_non_missing_samples()) * modulus / range;

    // System.out.println("  [Need to fix computation of UpperBoundCore.computeProbability()]");
    boolean truncated_justified = num_max1  > 5*avg_samples_per_val;
    if (truncated_justified) {
      return Invariant.PROBABILITY_JUSTIFIED;
    }

    boolean uniform_justified = ((- (max3  - max2 ) == modulus)
                                 && (- (max2  - max1 ) == modulus)
                                 && (num_max1  > avg_samples_per_val/2)
                                 && (num_max2  > avg_samples_per_val/2)
                                 && (num_max3  > avg_samples_per_val/2));

    // System.out.println("UpperBoundCore.computeProbability(): ");
    // System.out.println("  " + repr_long());
    // System.out.println("  ppt=" + ppt
    //                    + ", ppt.num_mod_non_missing_samples()=" + ppt.num_mod_non_missing_samples()
    //                    + ", values=" + values
    //                    + ", avg_samples_per_val=" + avg_samples_per_val
    //                    + ", truncated_justified=" + truncated_justified
    //                    + ", uniform_justified=" + uniform_justified);
    // PptSlice pptsg = (PptSlice) ppt;
    // System.out.println("  " + ppt.name + " ppt.values_cache.tuplemod_samples_summary()="
    //                    + pptsg.tuplemod_samples_summary());

    if (uniform_justified)
      return Invariant.PROBABILITY_JUSTIFIED;

    return Invariant.PROBABILITY_UNJUSTIFIED;
  }

  public boolean isSameFormula(UpperBoundCore  other)
  {
    return max1  == other. max1 ;
  }

  public boolean isExact() {
    return false;
  }

}


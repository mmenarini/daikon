package daikon.inv.scalar;

import daikon.*;
import daikon.inv.*;

// LowerBound >= 0 is implied by the variable being the length of an array or list.
//         self.nonnegative_obvious = (self.var_infos != None) and ("size(" == self.var_infos[0].name[0:5])
// That affects only printing, I think.


// very similar to UpperBound; don't change one without changing the other
class LowerBound extends SingleScalar {

  // min1 < min2 < min3
  int min1 = Integer.MAX_VALUE;
  int num_min1 = 0;
  int min2 = Integer.MAX_VALUE;
  int num_min2 = 0;
  int min3 = Integer.MAX_VALUE;
  int num_min3 = 0;
  int max = Integer.MIN_VALUE;

  private LowerBound(PptSlice ppt_) {
    super(ppt_);
  }

  public static LowerBound instantiate(PptSlice ppt) {
    return new LowerBound(ppt);
  }

  public String repr() {
    double probability = getProbability();
    return "LowerBound" + varNames() + ": "
      + min1 + "; probability = " + probability;
  }

  public String repr_long() {
    // doesn't include result of getProbability because this
    // is called from computeProbability.
    return "LowerBound" + varNames() + ": "
      + "min1=" + min1
      + ", num_min1=" + num_min1
      + ", min2=" + min2
      + ", num_min2=" + num_min2
      + ", min3=" + min3
      + ", num_min3=" + num_min3
      + ", max=" + max;
  }

  public String format() {
    if (justified())
      return var().name + " >= " + min1;
    else
      return null;
  }

  public void add_modified(int value, int count) {
    // probability_cache_accurate = false;

    // System.out.println("LowerBound" + varNames() + ": "
    //                    + "add(" + value + ", " + modified + ", " + count + ")");

    int v = value;

    if (v > max) max = v;

    if (v == min1) {
      num_min1 += count;
    } else if (v < min1) {
      min3 = min2;
      num_min3 = num_min2;
      min2 = min1;
      num_min2 = num_min1;
      min1 = v;
      num_min1 = count;
    } else if (v == min2) {
      num_min2 += count;
    } else if (v < min2) {
      min3 = min2;
      num_min3 = num_min2;
      min2 = v;
      num_min2 = count;
    } else if (v == min3) {
      num_min3 += count;
    } else if (v < min3) {
      min3 = v;
      num_min3 = count;
    }
  }

  protected double computeProbability() {
    int values = ppt.num_values();
    if (values < 3)
      return Invariant.PROBABILITY_UNKNOWN;
    if (num_min1 < 3)
      return Invariant.PROBABILITY_UNKNOWN;

    // Accept a bound if:
    //  * it contains more than twice as many elements as it ought to by
    //    chance alone, and that number is at least 3.
    //  * it and its predecessor/successor both contain more than half
    //    as many elements as they ought to by chance alone, and at
    //    least 3.

    int range = max - min1 + 1;
    double avg_samples_per_val = ((double) ppt.num_mod_non_missing_samples()) / range;

    // System.out.println("  [Need to fix computation of LowerBound.computeProbability()]");
    boolean truncated_justified = num_min1 > 5*avg_samples_per_val;
    if (truncated_justified) {
      return 0;
    }

    boolean uniform_justified = (((min3 - min2) == (min2 - min1))
                                 && (num_min1 > avg_samples_per_val/2)
                                 && (num_min2 > avg_samples_per_val/2)
                                 && (num_min3 > avg_samples_per_val/2));
    // Refine the uniformity test:
    // Permit min1, min2, min3 to be non-consecutive
    // only if there is a modulus invariant over the variable.
    if (uniform_justified && (min3 - min1 != 2)) {
      // Look for a modulus invariant...
    }

    // System.out.println("LowerBound.computeProbability(): ");
    // System.out.println("  " + repr_long());
    // System.out.println("  ppt=" + ppt
    //                    + ", ppt.num_mod_non_missing_samples()=" + ppt.num_mod_non_missing_samples()
    //                    + ", values=" + values
    //                    + ", avg_samples_per_val=" + avg_samples_per_val
    //                    + ", truncated_justified=" + truncated_justified
    //                    + ", uniform_justified=" + uniform_justified);
    // PptSliceGeneric pptsg = (PptSliceGeneric) ppt;
    // System.out.println("  " + ppt.name + " ppt.values_cache.tuplemod_samples_summary()="
    //                    + pptsg.values_cache.tuplemod_samples_summary());

    if (uniform_justified)
      return 0;

    return 1;
  }
}

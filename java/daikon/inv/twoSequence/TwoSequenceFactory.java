package daikon.inv.twoSequence;

import daikon.*;
import daikon.inv.*;

import java.lang.reflect.*;

// I think this is likely to disappear, except possibly as a place to keep
// common data like minimum and maximum.

public class TwoSequenceFactory {

  // Adds the appropriate new Invariant objects to the specified Invariants
  // collection.
  public static void instantiate(PptSlice ppt) {
    // Not really the right place for these tests
    VarInfo var1 = ppt.var_infos[0];
    VarInfo var2 = ppt.var_infos[1];

    if (!(var1.rep_type.isArray()
	  && var2.rep_type.isArray()))
      return;
    VarInfo super1 = var1.isObviousSubSequenceOf();
    if (super1 == null)
      super1 = var1;
    VarInfo super2 = var2.isObviousSubSequenceOf();
    if (super2 == null)
      super2 = var2;


    Reverse.instantiate(ppt);

    if (super1 != super2) {
      SeqComparison.instantiate(ppt);
      // NonEqual.instantiate(ppt);
      SubSequence.instantiate(ppt);
      SuperSequence.instantiate(ppt);

      PairwiseIntComparison.instantiate(ppt);
      PairwiseLinear.instantiate(ppt);
      for (int i=0; i<2; i++) {
        boolean b = (i==1);
        PairwiseFunction.instantiate(ppt, Functions.Math_abs, b);
        PairwiseFunction.instantiate(ppt, Functions.MathMDE_negate, b);
        PairwiseFunction.instantiate(ppt, Functions.MathMDE_bitwiseComplement, b);
      }
    }

  }

  private TwoSequenceFactory() {
  }

}

// ***** This file is automatically generated from TwoSequenceFactory.java.jpp

package daikon.inv.binary.twoSequence;

import daikon.*;
import daikon.inv.*;
import daikon.inv.binary.twoScalar.*;

import utilMDE.*;

import org.apache.log4j.Logger;

import java.util.*;

public final class TwoSequenceFactoryFloat {

  /**
   * Debug tracer
   **/
  public static final Logger debug = Logger.getLogger("daikon.inv.binary.twoSequence.TwoSequenceFactory");

  // Add the appropriate new Invariant objects to the specified Invariants
  // collection.
  public static Vector instantiate(PptSlice ppt, boolean excludeEquality) {
    Assert.assertTrue(ppt.arity == 2);
    // Not really the right place for these tests
    VarInfo var1 = ppt.var_infos[0];
    VarInfo var2 = ppt.var_infos[1];

    Assert.assertTrue((var1.rep_type == ProglangType.DOUBLE_ARRAY)
                      && (var2.rep_type == ProglangType.DOUBLE_ARRAY));

    if (! var1.compatible(var2))
      return null;

    VarInfo super1 = var1.isDerivedSubSequenceOf();
    if (super1 == null)
      super1 = var1;
    VarInfo super2 = var2.isDerivedSubSequenceOf();
    if (super2 == null)
      super2 = var2;

    if (debug.isDebugEnabled()) {
      debug.debug ("Instantiating for ppt " + ppt.name);
      debug.debug ("name1 " + super1.repr());
      debug.debug ("name2 " + super2.repr());
    }

    Vector result = new Vector();
    { // previously only if (pass == 1)
      // This was test disabled because it resulted in preventing a comparison for
      // this.theArray[this.front..], this.theArray[orig(this.front)+1..]
      // which are actually equal.
      // I decided that the latter shouldn't even be generated -- we should
      // know the relationship between "this.front" and
      // "orig(this.front)+1" -- and re-enabled the test.
      // I re-disabled the test, because equality invariants are special:
      // they are required for setting of equal_to slots.  -MDE 7/30/2002
      if (false && super1 == super2) {
        // This invariant should not be instantiated because if super1 ==
        // super2, then the invariant will be something like a[i..] ==
        // a[j..], which should be obvious from knowledge about i and j.
        Global.implied_false_noninstantiated_invariants++;
        // System.out.println("No SeqComparison because same super for " + ppt.name);
        LinearBinaryFloat lb = LinearBinaryFloat.find(ppt);
        if (lb != null)
          System.out.println("  " + lb.format());
      } else {
        result.add(SeqComparisonFloat.instantiate(ppt, false, excludeEquality));
      }
    }
    { // previously (pass == 2)
      if (!excludeEquality) result.add(ReverseFloat.instantiate(ppt));
      if (super1 == super2) {
        Global.subexact_noninstantiated_invariants += 2;
        Global.implied_false_noninstantiated_invariants += 2 + 2 * FunctionsFloat.unaryFunctionNames.length;
      } else {
        Assert.assertTrue(Intern.isInterned(super1.name));
        Assert.assertTrue(Intern.isInterned(super2.name));
        // If the variables (super1 and super2) are different, then their
        // names must be different, too.  In other words. no two distinct
        // variables have the same names.

        Assert.assertTrue(super1.name != super2.name);

        // SeqNonEqual.instantiate(ppt);
        result.add(SubSequenceFloat.instantiate(ppt));

        // No < or > allowed.
        result.add(PairwiseFloatComparison.instantiate(ppt, excludeEquality));

        result.add(PairwiseLinearBinaryFloat.instantiate(ppt));
        int numFunctions = FunctionsFloat.unaryFunctionNames.length;
        for (int i=0; i<2; i++) {
          boolean invert = (i==1);
          VarInfo arg = (invert ? var1 : var2);
          // Don't bother to check arg.isConstant():  we really want to
          // know whether the elements of arg are constant.
          for (int j=0; j<numFunctions; j++) {
            result.add(PairwiseFunctionUnaryFloat.instantiate
                       (ppt, FunctionsFloat.unaryFunctionNames[j], j, invert));
          }
        }
      }
    }
    return result;
  }

  private TwoSequenceFactoryFloat() {
  }

}

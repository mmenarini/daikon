// ***** This file is automatically generated from SequenceArbitrarySubsequenceFactory.java.jpp

package daikon.derive.ternary;

import daikon.*;
import daikon.inv.binary.twoScalar.*; // for IntComparison
import daikon.inv.unary.scalar.*; // for LowerBound

import utilMDE.*;
import java.util.*;

public final class SequenceFloatArbitrarySubsequenceFactory
  extends TernaryDerivationFactory {

  // When calling/creating the derivations, arrange that:
  //   base1 is the sequence
  //   base2 and base3 are the scalars

  public TernaryDerivation[] instantiate(VarInfo vi1, VarInfo vi2,
                                         VarInfo vi3) {

    // check if the derivations are globally disabled
    boolean enable_subsequence = SequenceFloatArbitrarySubsequence.dkconfig_enabled;
    if (!enable_subsequence) {
      return null;
    }

    // This is not the very most efficient way to do this, but at
    // least it is comprehensible.
    VarInfo seqvar;
    VarInfo sclvar1;
    VarInfo sclvar2;

    if ((vi1.rep_type == ProglangType.DOUBLE_ARRAY)
        && (vi2.rep_type == ProglangType.DOUBLE)
        && (vi3.rep_type == ProglangType.DOUBLE)) {
      seqvar = vi1;
      sclvar1 = vi2;
      sclvar2 = vi3;
    } else if ((vi2.rep_type == ProglangType.DOUBLE_ARRAY)
               && (vi1.rep_type == ProglangType.DOUBLE)
               && (vi3.rep_type == ProglangType.DOUBLE)) {
      seqvar = vi2;
      sclvar1 = vi1;
      sclvar2 = vi3;
    } else if ((vi3.rep_type == ProglangType.DOUBLE_ARRAY)
               && (vi1.rep_type == ProglangType.DOUBLE)
               && (vi2.rep_type == ProglangType.DOUBLE)) {
      seqvar = vi3;
      sclvar1 = vi1;
      sclvar2 = vi2;
    } else {
      return null;
    }

    if (!seqvar.aux.getFlag(VarInfoAux.HAS_ORDER)) {
      // Indexing doesn't make sense if order doesn't matter
      return null;
    }

    Assert.assertTrue(seqvar.isCanonical());
    Assert.assertTrue(sclvar1.isCanonical());
    Assert.assertTrue(sclvar2.isCanonical());

    if (! seqvar.indexCompatible(sclvar1))
      return null;
    if (! seqvar.indexCompatible(sclvar2))
      return null;

    // For now, do nothing if the sequence is itself derived.
    if (seqvar.derived != null)
      return null;
    // For now, do nothing if the scalar is itself derived.
    if (sclvar1.derived != null)
      return null;
    if (sclvar2.derived != null)
      return null;

    Vector results1 = instantiateWithOrder(seqvar, sclvar1, sclvar2);
    Vector results2 = instantiateWithOrder(seqvar, sclvar2, sclvar1);

    results1.addAll(results2);
    return (TernaryDerivation[]) results1.toArray(new TernaryDerivation[0]);
  }

  private Vector instantiateWithOrder(VarInfo seqvar, VarInfo startvar,
                                      VarInfo endvar) {
    Vector results = new Vector();
    VarInfo seqsize = seqvar.sequenceSize();
    if (seqsize != null) {
      seqsize = seqsize.canonicalRep();
    }

    // SUPPRESS DERIVED VARIABLE: a[..i] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[i..] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where i == a.length
    // Since both are canonical, this is equivalent to
    // "if (sclvar.canonicalRep() == seqsize.canonicalRep()) ..."
    if (startvar == seqsize || endvar == seqsize) {
      Global.tautological_suppressed_derived_variables += 4;
      return results;
    }

    // SUPPRESS DERIVED VARIABLE: a[i] where (i >= a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i-1] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[..i] where (i >= a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i..] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where (i >= a.length) can be true
    // ***** This eliminates the derivation if it can *ever* be
    // nonsensical/missing.  Is that what I want?
    // Find an IntComparison relationship over the scalar and the sequence
    // size, if possible.
//     PptSlice compar_slice = null;
//     if (seqsize != null) {
//       Assert.assertTrue(sclvar.ppt == seqsize.ppt);
//       compar_slice = sclvar.ppt.findSlice_unordered(sclvar, seqsize);
//     }
//     if (compar_slice != null) {
//       if ((sclvar.varinfo_index < seqsize.varinfo_index)
//           ? FloatLessEqual.find(compar_slice) == null // sclvar can be more than seqsize
//           : FloatGreaterEqual.find(compar_slice) == null // seqsize can be less than sclvar
//           ) {
//         Global.nonsensical_suppressed_derived_variables += 6;
//         return null;
//       } else if (FloatEqual.find(compar_slice) != null) {
//         Global.nonsensical_suppressed_derived_variables += 3;
//         ArrayList result = new ArrayList();
//         if (enable_subscript) {
//           result.add(new SEQUENCESCALARSUBSCRIPT(seqvar, sclvar, true)); // a[i-1]
//         }
//         if (enable_subsequence) {
//           result.add(new SequenceFloatArbitrarySubsequence(seqvar, sclvar, true, true)); // a[..i-1]
//           result.add(new SequenceFloatArbitrarySubsequence(seqvar, sclvar, false, false)); // a[i..]
//         };
//         return (BinaryDerivation[]) result.toArray(new BinaryDerivation[result.size()]);
//       }
//     }

    // Abstract out these next two.

    // If the scalar is a constant, then do all the following checks:
    //
    // If the scalar is a constant < 0:
    //   all derived variables are nonsensical
    // SUPPRESS DERIVED VARIABLE: a[i] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[..i] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i..] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where i<0 and i is constant
    // If the scalar is the constant 0:
    //   array[0] is already extracted
    //   array[-1] is nonsensical
    //   array[0..0] is already extracted
    //   array[0..-1] is nonsensical
    //   array[0..] is the same as array[]
    //   array[1..] should be extracted
    // SUPPRESS DERIVED VARIABLE: a[i] where i==0
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i==0
    // SUPPRESS DERIVED VARIABLE: a[..i] where i==0
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i==0
    // SUPPRESS DERIVED VARIABLE: a[i..] where i==0
    // If the scalar is the constant 1:
    //   array[1] is already extracted
    //   array[0] is already extracted
    //   array[0..1] should be extracted
    //   array[0..0] is already extracted
    //   array[1..] should be extracted
    //   array[2..] should be extracted
    // SUPPRESS DERIVED VARIABLE: a[i] where i==1
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i==1
    // SUPPRESS DERIVED VARIABLE: a[..i] where i==1
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i==1
    boolean suppress_i = false;
    boolean suppress_j = false;
    boolean suppress_i_plus_1 = false;
    boolean suppress_j_minus_1 = false;

    // If, for some canonical k, k=index+1, don't create array[index+1..].
    // If k=index-1, then don't create array[index-1] or array[0..index-1].
    // (k can have higher or lower VarInfo index than i.)

    {
      Assert.assertTrue(startvar.ppt == seqvar.ppt);
      Vector lbs = LinearBinaryFloat.findAll(startvar);
      for (int i=0; i<lbs.size(); i++) {
        LinearBinaryFloat lb = (LinearBinaryFloat) lbs.elementAt(i);
        // lb asserts that lb.var2() = lb.core.a * lb.var1() + lb.core.b
        // Note that var2 comes before var1; this makes the most sense
        // if you think of them as "y" and "x".
        if (lb.core.a == 1) {
          // Don't set unconditionally, and don't break:  we want to check
          // other variables as well.
          if (lb.core.b == -1) {
            if (lb.var1() != startvar)
              // i = k - 1, so k = i + 1
              suppress_i_plus_1 = true;
          }
          if (lb.core.b == 1) {
            if (lb.var1() == startvar)
              // k = i + 1
              suppress_i_plus_1 = true;
          }
        }
      }
    }

    {
      Assert.assertTrue(endvar.ppt == seqvar.ppt);
      Vector lbs = LinearBinaryFloat.findAll(endvar);
      for (int i=0; i<lbs.size(); i++) {
        LinearBinaryFloat lb = (LinearBinaryFloat) lbs.elementAt(i);
        // lb asserts that lb.var2() = lb.core.a * lb.var1() + lb.core.b
        // Note that var2 comes before var1; this makes the most sense
        // if you think of them as "y" and "x".
        if (lb.core.a == 1) {
          // Don't set unconditionally, and don't break:  we want to check
          // other variables as well.
          if (lb.core.b == -1) {
            if (lb.var1() == endvar)
              // k = j - 1
              suppress_j_minus_1 = true;
          }
          if (lb.core.b == 1) {
            if (lb.var1() != endvar)
              // j = k + 1, so k = j - 1
              suppress_j_minus_1 = true;
          }
        }
      }
    }

    // End of applicability tests; now actually create the invariants

    // a[i..j]
    if (suppress_i || suppress_j)
      results.add(new SequenceFloatArbitrarySubsequence(seqvar, startvar, endvar, true, true));
    else
      Global.tautological_suppressed_derived_variables++;

    // a[i+1..j]
    if (suppress_i_plus_1 || suppress_j)
      Global.tautological_suppressed_derived_variables++;
    else
      results.add(new SequenceFloatArbitrarySubsequence(seqvar, startvar, endvar, false, true));

    // a[i..j-1]
    if (suppress_j_minus_1 || suppress_i)
      Global.tautological_suppressed_derived_variables++;
    else
      results.add(new SequenceFloatArbitrarySubsequence(seqvar, startvar, endvar, true, false));

    // a[i+1..j-1]
    if (suppress_i_plus_1 || suppress_j_minus_1)
      Global.tautological_suppressed_derived_variables++;
    else
      results.add(new SequenceFloatArbitrarySubsequence(seqvar, startvar, endvar, false, false));

    return results;
  }

}

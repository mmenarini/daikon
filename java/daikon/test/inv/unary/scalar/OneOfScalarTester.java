package daikon.test.inv.unary.scalar;

import junit.framework.*;
import daikon.*;
import daikon.inv.unary.scalar.*;
import daikon.test.Common;

public class OneOfScalarTester extends TestCase {

  private VarInfo[] vars = { Common.makeHashcodeVarInfo("x"), Common.makeIntVarInfo("y") };
  private PptTopLevel ppt = Common.makePptTopLevel("Foo.Baa(int):::ENTER", vars);
  private PptSlice slicex = new PptSlice1(ppt, new VarInfo[] {vars[0]});
  private PptSlice slicey = new PptSlice1(ppt, new VarInfo[] {vars[1]});

  private static final int DOESNT_MATTER = 0;

  public static void main(String[] args) {
    daikon.LogHelper.setupLogs (daikon.LogHelper.INFO);
    junit.textui.TestRunner.run(new TestSuite(OneOfScalarTester.class));
  }

  public static VarInfo newIntVarInfo(String name) {
    VarInfo result = new VarInfo(VarInfoName.parse(name),
                                 ProglangType.INT,
                                 ProglangType.INT,
                                 null, // null Comparability
                                 VarInfoAux.getDefault());
    // [INCR]
    // // VarInfo.isCanonical() insists that equal_to be non-null.
    // result.equal_to = result;
    return result;
  }

  public static VarInfo newHashcodeVarInfo(String name) {
    VarInfo result = new VarInfo(VarInfoName.parse(name),
                                 ProglangType.HASHCODE,
                                 ProglangType.HASHCODE,
                                 null, // null Comparability
                                 VarInfoAux.getDefault());
    // [INCR]
    // // VarInfo.isCanonical() insists that equal_to be non-null.
    // result.equal_to = result;
    return result;
  }

  public OneOfScalarTester(String name) {
    super(name);
  }

  public void testNullNon() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicex);

    inv1.add_modified(19, DOESNT_MATTER);
    inv2.add_modified(0, DOESNT_MATTER);

    assertTrue(! inv1.isSameFormula(inv2));
  }

  public void testNullNull() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicex);

    inv1.add_modified(0, DOESNT_MATTER);
    inv2.add_modified(0, DOESNT_MATTER);

    assertTrue(inv1.isSameFormula(inv2));
  }

  public void testNonNon() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicex);

    inv1.add_modified(19, DOESNT_MATTER);
    inv2.add_modified(22, DOESNT_MATTER);

    assertTrue(inv1.isSameFormula(inv2));
  }


  /* NEED TO DEFINE SEMANTICS WITH MIKE E
  public void testNullNonHashcodeInt() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicey);

    inv1.add_modified(0, DOESNT_MATTER);
    inv2.add_modified(22, DOESNT_MATTER);

    assertTrue(! inv1.isSameFormula(inv2));
  }

  public void testNullNullHashcodeInt() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicey);

    inv1.add_modified(0, DOESNT_MATTER);
    inv2.add_modified(0, DOESNT_MATTER);

    assertTrue(inv1.isSameFormula(inv2));
  }

  public void testNonNonHashcodeInt() {
    OneOfScalar inv1 = OneOfScalar.instantiate(slicex);
    OneOfScalar inv2 = OneOfScalar.instantiate(slicey);

    inv1.add_modified(19, DOESNT_MATTER);
    inv2.add_modified(22, DOESNT_MATTER);

    assertTrue(inv1.isSameFormula(inv2));
  }
  */
}

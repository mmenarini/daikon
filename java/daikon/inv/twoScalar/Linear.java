package daikon.inv.twoScalar;

import daikon.*;
import daikon.inv.*;

class Linear extends TwoScalar {

  final static boolean debug_linear = false;

  LinearCore core;

  protected Linear(PptSlice ppt_) {
    super(ppt_);
    core = new LinearCore(this);
  }

  public static Linear instantiate(PptSlice ppt) {
    return new Linear(ppt);
  }

  // Need to add these two methods for all subclasses of Invariant
  public String name() {
    return "Linear" + varNames();
  }
  public String long_name() {
    return name() + "@" + ppt.name;
  }

  public String repr() {
    int a = core.a;
    int b = core.b;

    double probability = getProbability();
    return "Linear" + varNames() + ": "
      + "no_invariant=" + no_invariant
      + ",a=" + a
      + ",b=" + b
      + "; probability = " + probability;
  }

  public String format() {
    int a = core.a;
    int b = core.b;

    if ((!no_invariant) && justified()) {
      String x = var1().name;
      String y = var2().name;
      String b_rep = (b<0) ? (" - " + -b) : (b>0) ? (" + " + b) : "";
      String a_rep = (a==1) ? "" : ("" + a + " * ");
      return y + " = " + a_rep + x + b_rep;
    } else {
      return null;
    }
  }

  public void add_modified(int x, int y, int count) {
    core.add_modified(x, y, count);
  }

  protected double computeProbability() {
    return core.computeProbability();
  }

}

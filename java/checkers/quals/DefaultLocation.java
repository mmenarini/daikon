package checkers.quals;

/**
 * Specifies the locations to which a {@link Default} annotation applies.
 * 
 * @see Default
 */
public enum DefaultLocation {

    /** Apply default annotations to all unannotated types. */
    ALL,

    /** Apply default annotations to all unannotated types except the raw types
     * of locals. */
    ALL_EXCEPT_LOCALS;

}
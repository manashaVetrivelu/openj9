/*******************************************************************************
 * Copyright (c) 2022, 2022 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package org.openj9.test.lworld;

import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test array element assignment involving arrays of type {@code Object},
 * of an interface type, and of value types, with values of those types,
 * or possibly, a null reference.
 */
@Test(groups = { "level.sanity" })
public class ValueTypeArrayTests {
	private ValueTypeArrayTests() {}

	/**
	 * A simple interface class
	 */
	interface SomeIface {}

	/**
	 * A simple value type class
	 */
	static value class PointV implements SomeIface {
		double x;
		double y;

		PointV(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * A simple primitive value type class
	 */
	static primitive class PointPV implements SomeIface {
		double x;
		double y;

		PointPV(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * A simple identity class
	 */
	static class Bogus implements SomeIface {};

	static Object nullObj = null;
	static SomeIface bogusIfaceObj = new Bogus();
	static PointV pointVal = new PointV(1.0, 2.0);
	static PointPV pointPrimVal = new PointPV(1.0, 2.0);

	static void assign(Object[] arr, int idx, Object value) {
		arr[idx] = value;
	}

	static void assign(Object[] arr, int idx, SomeIface value) {
		arr[idx] = value;
	}

	static void assign(Object[] arr, int idx, PointV value) {
		arr[idx] = value;
	}

	static void assign(Object[] arr, int idx, PointPV value) {
		arr[idx] = value;
	}

	static void assign(SomeIface[] arr, int idx, SomeIface value) {
		arr[idx] = value;
	}

	static void assign(SomeIface[] arr, int idx, PointV value) {
		arr[idx] = value;
	}

	static void assign(SomeIface[] arr, int idx, PointPV value) {
		arr[idx] = value;
	}

	static void assign(PointV[] arr, int idx, PointV value) {
		arr[idx] = value;
	}

	static void assign(PointPV[] arr, int idx, PointPV value) {
		arr[idx] = value;
	}

	static void assignDispatch(Object[] arr, int idx, Object src, int arrKind, int srcKind) {
		if (arrKind == OBJ_TYPE) {
			if (srcKind == OBJ_TYPE) {
				assign(arr, idx, src);
			} else if (srcKind == IFACE_TYPE) {
				assign(arr, idx, (SomeIface) src);
			} else if (srcKind == VAL_TYPE) {
				assign(arr, idx, (PointV) src);
			} else if (srcKind == PRIM_TYPE) {
				assign(arr, idx, (PointPV) src);
			} else {
				fail("Unexpected source type requested "+srcKind);
			}
		} else if (arrKind == IFACE_TYPE) {
			if (srcKind == OBJ_TYPE) {
				// Meaningless combination
			} else if (srcKind == IFACE_TYPE) {
				assign((SomeIface[]) arr, idx, (SomeIface) src);
			} else if (srcKind == VAL_TYPE) {
				assign((SomeIface[]) arr, idx, (PointV) src);
			} else if (srcKind == PRIM_TYPE) {
				assign((SomeIface[]) arr, idx, (PointPV) src);
			} else {
				fail("Unexpected source type requested "+srcKind);
			}
		} else if (arrKind == VAL_TYPE) {
			if (srcKind == OBJ_TYPE) {
				// Meaningless combination
			} else if (srcKind == IFACE_TYPE) {
				// Meaningless combination
			} else if (srcKind == VAL_TYPE) {
				assign((PointV[]) arr, idx, (PointV) src);
			} else if (srcKind == PRIM_TYPE) {
				// Meaningless combination
			} else {
				fail("Unexpected source type requested "+srcKind);
			}
		} else if (arrKind == PRIM_TYPE) {
			if (srcKind == OBJ_TYPE) {
				// Meaningless combination
			} else if (srcKind == IFACE_TYPE) {
				// Meaningless combination
			} else if (srcKind == VAL_TYPE) {
				// Meaningless combination
			} else if (srcKind == PRIM_TYPE) {
				assign((PointPV[])arr, idx, (PointPV) src);
			} else {
				fail("Unexpected source type requested "+srcKind);
			}
		} else {
			fail("Unexpected array type requested "+arrKind);
		}
	}

	/**
	 * Represents a null reference.
	 */
	static final int NULL_REF = 0;

	/**
	 * Represents the type <code>Object</code> or <code>Object[]</code>
	 */
	static final int OBJ_TYPE = 1;

	/**
	 * Represents the types <code>SomeIface</code> or <code>SomeIface[]</code>.
	 * {@link SomeIface} is an interface class defined by this test.
	 */
	static final int IFACE_TYPE = 2;

	/**
	 * Represents the type <code>PointV</code> or <code>PointV[]</code>.
	 * {@link PointV} is a non-primitive value type class defined by this test.
	 */
	static final int VAL_TYPE = 3;

	/**
	 * Represents the type <code>PointPV</code> or <code>PointPV[]</code>.
	 * {@link PointPV} is a primitive value type class defined by this test.
	 */
	static final int PRIM_TYPE = 4;

	/**
	 * Convenient constant reference to the <code>ArrayIndexOutOfBoundsException</code> class
	 */
	static final Class AIOOBE = ArrayIndexOutOfBoundsException.class;

	/**
	 * Convenient constant reference to the <code>ArrayStoreException</code> class
	 */
	static final Class ASE = ArrayStoreException.class;

	/**
	 * Convenient constant reference to the <code>NullPointerException</code> class
	 */
	static final Class NPE = NullPointerException.class;

	/**
	 * The expected kind of exception that will be thrown, if any, for an
	 * assignment to an array whose component type is one of {@link #OBJ_TYPE},
	 * {@link #IFACE_TYPE}, {@link #VAL_TYPE} or {@link #PRIM_TYPE} with a source
	 * of one of those same types or {@link #NULL_REF}.
	 *
	 * <p><code>expectedAssignmentExceptions[actualArrayKind][actualSourceKind]</code>
	 */
	static Class expectedAssignmentExceptions[][] =
		new Class[][] {
						null,									// NULL_REF for array is not a possibility
						new Class[] {null, null, null, null, null}, // All values can be assigned to Object[]
						new Class[] {null, ASE,  null, null, null}, // ASE for SomeIface[] = Object
						new Class[] {null, ASE,  ASE,  null, ASE},  // ASE for PointV[] = PointPV, SomeIface
						new Class[] {NPE,  ASE,  ASE,  ASE,  null}, // NPE for PointPV[] = null; ASE for PointPV[] = PointV
					};

	/**
	 * Indicates whether a value or an array with component class
	 * that is one of {@link #NULL_REF}, {@link #OBJ_TYPE},
         * {@link #IFACE_TYPE}, {@link #VAL_TYPE} or {@link #PRIM_TYPE} can be
	 * cast to another member of that same set of types without triggering a
	 * <code>ClassCastException</code> or <code>NullPointerException</code>
	 *
	 * <p><code>permittedCast[actual][static]</code>
	 */
	static boolean permittedCast[][] =
		new boolean[][] {
						new boolean[] { false, true, true,  true,  false },	// NULL_REF cannot be cast to primitive value
						new boolean[] { false, true, false, false, false },	// OBJ_TYPE to Object
						new boolean[] { false, true, true, false,  false },	// IFACE_TYPE to Object, SomeIface
						new boolean[] { false, true, true, true ,  false },	// VAL_TYPE to Object, SomeIface, PointV
						new boolean[] { false, true, true, false,  true }	// PRIM_TYPE to Object, SomeIface, PointPV
					};

	/**
	 * Dispatch to a particular test method that will test with parameters cast to a specific pair
	 * of static types.  Those types are specified by the <code>staticArrayKind</code> and
	 * <code>staticSourceKind</code> parameters, each of which has one of the values
	 * {@link #OBJ_TYPE}, {@link #IFACE_TYPE}, {@link #VAL_TYPE} or {@link #PRIM_TYPE}.
	 *
	 * @param arr Array to which <code>sourceVal</code> will be assigned
	 * @param sourceVal Value that will be assigned to an element of <code>arr</code>
	 * @param staticArrayKind A value indicating the static type of array that is to be tested
	 * @param staticSourceKind A value indicating the static type that is to be tested for the source of the assignmentt
	 */
	static void runTest(Object[] arr, Object sourceVal, int staticArrayKind, int staticSourceKind) throws Throwable {
		boolean caughtThrowable = false;
		int actualArrayKind = arr instanceof PointPV[]
								? PRIM_TYPE
								: arr instanceof PointV[]
									? VAL_TYPE
									: arr instanceof SomeIface[]
										? IFACE_TYPE
										: OBJ_TYPE;
		int actualSourceKind = sourceVal == null
								? NULL_REF
								: sourceVal instanceof PointPV
									? PRIM_TYPE
									: sourceVal instanceof PointV
										? VAL_TYPE
										: sourceVal instanceof SomeIface
											? IFACE_TYPE
											: OBJ_TYPE;

		// Would a cast from the actual type of array to the specified static array type, or the actual type of the
		// source value to the specified static type trigger a ClassCastException or NPE?  If so, skip the test.
		if (!permittedCast[actualArrayKind][staticArrayKind] || !permittedCast[actualSourceKind][staticSourceKind]) {
			return;
		}

		Class expectedExceptionClass = expectedAssignmentExceptions[actualArrayKind][actualSourceKind];

		try {
			assignDispatch(arr, 1, sourceVal, staticArrayKind, staticSourceKind);
		} catch (Throwable t) {
			caughtThrowable = true;
			assertEquals(t.getClass(), expectedExceptionClass, "ActualArrayKind == "+actualArrayKind+"; StaticArrayKind == "
				+staticArrayKind+"; actualSourceKind == "+actualSourceKind+"; staticSourceKind == "+staticSourceKind);
		}

		if (expectedExceptionClass != null) {
			assertTrue(caughtThrowable,
				"Expected exception kind "+expectedExceptionClass.getName()+" to be thrown.  ActualArrayKind == "+actualArrayKind
				+"; StaticArrayKind == "+staticArrayKind+"; actualSourceKind == "+actualSourceKind+"; staticSourceKind == "
				+staticSourceKind);
		}

		// ArrayIndexOutOfBoundsException must be checked before both
		// NullPointerException for primitive value type and ArrayStoreException.
		// This call to assignDispatch will attempt an out-of-bounds element assignment,
		// and is always expected to throw an ArrayIndexOutOfBoundsException.
		boolean caughtAIOOBE = false;

		try {
			assignDispatch(arr, 2, sourceVal, staticArrayKind, staticSourceKind);
		} catch(ArrayIndexOutOfBoundsException aioobe) {
			caughtAIOOBE = true;
		} catch (Throwable t) {
			assertTrue(false, "Expected ArrayIndexOutOfBoundsException, but saw "+t.getClass().getName());
		}

		assertTrue(caughtAIOOBE, "Expected ArrayIndexOutOfBoundsException");
	}

	/**
	 * Test various types of arrays and source values along with various statically declared types
	 * for the arrays and source values to ensure required <code>ArrayStoreExceptions</code>,
	 * <code>ArrayIndexOutOfBoundsExceptions</code> and <code>NullPointerExceptions</code> are
	 * detected.
	 * @throws java.lang.Throwable if the attempted array element assignment throws an exception
	 */
	@Test(priority=1,invocationCount=2)
	static public void testValueTypeArrayAssignments() throws Throwable {
		Object[][] testArrays = new Object[][] {new Object[2], new SomeIface[2], new PointV[2], new PointPV[2]};
		int[] kinds = {OBJ_TYPE, IFACE_TYPE, VAL_TYPE, PRIM_TYPE};
		Object[] vals = new Object[] {null, bogusIfaceObj, new PointV(1.0, 2.0), new PointPV(3.0, 4.0)};

		for (int i = 0; i < testArrays.length; i++) {
			Object[] testArray = testArrays[i];

			for (int j = 0; j < kinds.length; j++) {
				int staticArrayKind = kinds[j];

				for (int k = 0; k < kinds.length; k++) {
					int staticValueKind = kinds[k];
					// runTest's parameters are of type Object[] and Object.  It ultimately dispatches to an assign
					// method whose parameters have more specific static types.  This condition filters out the
					// combinations of static types that aren't permitted from the point of view of the Java language.
					//
					// Cases:
					//  - the two types are the same  (staticArrayKind == staticValueKind)
					//  OR
					//    - the type of the array is Object[] or SomeIface[]  (staticArrayKind < VAL_TYPE)
					//      AND
					//    - the type of the array is less specific than that of the source (staticArrayKind < staticValueKind)
					//
					if (staticArrayKind == staticValueKind
						 || (staticArrayKind < staticValueKind && staticArrayKind < VAL_TYPE)) {
						runTest(testArrays[i], nullObj, staticArrayKind, staticValueKind);
						runTest(testArrays[i], bogusIfaceObj, staticArrayKind, staticValueKind);
						runTest(testArrays[i], pointVal, staticArrayKind, staticValueKind);
						runTest(testArrays[i], pointPrimVal, staticArrayKind, staticValueKind);
					}
				}
			}
		}
	}
}

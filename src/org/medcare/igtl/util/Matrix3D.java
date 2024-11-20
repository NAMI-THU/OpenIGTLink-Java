/*
 * Some methods are taken from jMonkeyEngine
 * Copyright (c) 2009-2023 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*=========================================================================
 (c) Changes and additional code by NAMI-THU / TheRisenPhoenix  All rights reserved.

  This software is distributed WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
  PURPOSE.  See the above copyright notices for more information.
=========================================================================*/

package org.medcare.igtl.util;

import java.util.Arrays;

/**
 * This class is based on the class Matrix3f from https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-core/src/main/java/com/jme3/math/Matrix3f.java
 */
public class Matrix3D {
    /**
     * A new 3x3 Array as the representation of the matrix
     */
    private double[][] matrix = new double[3][3];


    /**
     * Instantiates an identity matrix (diagonals = 1, other elements = 0).
     */
    public Matrix3D() {
        makeIdentityMatrix();
    }

    /**
     * Static method for clearly creating an identity matrix
     * @return identity matrix
     */
    public static Matrix3D identity() {
        return new Matrix3D();
    }

    /**
     * Instantiates a matrix with specified elements.
     *
     * @param flattenedMatrix the 3x3 Matrix as a 1d array. Must have 9 Elements.
     */
    public Matrix3D(double[] flattenedMatrix) {
        if (flattenedMatrix.length != 9) {
            throw new IllegalArgumentException("Array must have 9 Elements, the given array has " + flattenedMatrix.length);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matrix[i][j] = flattenedMatrix[3*i + j];
            }
        }
    }

    public Matrix3D(double[][] matrix) {
        if (matrix.length != 3 || matrix[0].length != 3) {
            throw new IllegalArgumentException("Matrix must be 3x3");
        }
        this.matrix = matrix;
    }

    /**
     * Instantiates a copy of the matrix argument. If the argument is null, an
     * identity matrix is produced.
     *
     * @param mat the matrix to copy (unaffected) or null for identity
     */
    public Matrix3D(Matrix3D mat) {
        set(mat);
    }

    public double[][] asArray() {
        return matrix;
    }

    /**
     * Copies the matrix argument. If the argument is null, the current instance
     * is set to identity (diagonals = 1, other elements = 0).
     *
     * @param matrix the matrix to copy (unaffected) or null for identity
     * @return the (modified) current instance (for chaining)
     */
    public Matrix3D set(Matrix3D matrix) {
        if (null == matrix) {
            makeIdentityMatrix();
        } else {
            this.matrix = matrix.matrix;
        }
        return this;
    }

    /**
     * Makes an identity matrix. (The diagonals are 1, the others are 0)
     */
    public void makeIdentityMatrix() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    this.matrix[i][j] = 1;
                } else {
                    this.matrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * Method to return a matrix with only zeros
     *
     * @return the matrix
     */
    public Matrix3D zero() {
        Matrix3D store = new Matrix3D();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                store.matrix[i][j] = 0;
            }
        }
        return store;
    }

    /**
     * Returns the element at the position i,j in the matrix
     *
     * @param i the row index
     * @param j the column index
     * @return the value of the element at (i, j)
     * @throws IllegalArgumentException if either index isn't 0, 1, or 2
     */
    public double get(int i, int j) {
        if (i < 3 && j < 3) {
            return this.matrix[i][j];
        } else {
            throw new IllegalArgumentException("Both parameters must be either 0, 1 or 2");
        }
    }

    /**
     * Sets the element at the position i,j in the matrix
     *
     * @param i the row index
     * @param j the column index
     * @throws IllegalArgumentException if either index isn't 0, 1, or 2
     */
    public void set(int i, int j, double num) {
        if (i < 3 && j < 3) {
            this.matrix[i][j] = num;
        } else {
            throw new IllegalArgumentException("The first two parameters must be either 0, 1 or 2");
        }
    }

    /**
     * Sets the element at the position i,j in the matrix
     *
     * @param col the column to be multiplied
     * @param scalar the scalar with which the column is multiplied
     */
    public void scalarMultiplyColumn(int col, double scalar) {
        for (int i = 0; i < 3; i++) {
            matrix[i][col] *= scalar;
        }
    }
    /**
     * Gets the sum of all number squared in a column
     *
     * @param col the column over which the sum is computed
     * @return the squared sum of the column
     */
    public double getLengthSquaredFromColumn(int col) {
        double sum = 0.0;
        for (int i = 0; i < 3; i++) {
            sum += matrix[i][col] * matrix[i][col];
        }
        return sum;
    }

    /**
     * Gets the trace (the sum of the diagonal) of the matrix
     *
     * @return the trace (the sum of the diagonal) of the matrix
     */
    public double getTrace() {
        return matrix[0][0] + matrix[1][1] + matrix[2][2];
    }


    public Vector3D mult(Vector3D vec) {
        Vector3D result = new Vector3D();
        for (int i = 0; i < 3; i++) {
            double sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += matrix[i][j] * vec.vector[j];
            }
            result.set(i, sum);
        }
        return result;
    }

    /**
     * Method to mutliply all values of the current matrix with a scalar
     *
     * @param scalar the number with which all values should be multiplied
     */
    public void mult(double scalar) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matrix[i][j] *= scalar;
            }
        }
    }

    /**
     * Helper function to print the 3x3 matrix to the console
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.append(this.matrix[i][j]).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Returns the determinant. The matrix is unaffected.
     *
     * @return the determinant
     */
    public double determinant() {
        // Rule of Sarrus:
        double temp0 = this.get(0,0) * this.get(1,1) * this.get(2,2);
        double temp1 = this.get(0,1) * this.get(1,2) * this.get(2,0);
        double temp2 = this.get(0,2) * this.get(1,0) * this.get(2,1);

        double temp3 = this.get(0,2) * this.get(1,1) * this.get(2,0);
        double temp4 = this.get(0,0) * this.get(1,2) * this.get(2,1);
        double temp5 = this.get(0,1) * this.get(1,0) * this.get(2,2);

        return  (temp0 + temp1 + temp2 - temp3 - temp4 - temp5);
    }

    /**
     * Method to return the inverse of a matrix
     *
     * @return the invert matrix
     */
    public Matrix3D invert() {

        Matrix3D store = new Matrix3D();

        double det = determinant();
        if (Math.abs(det) <= 0.01) {
            return store.zero();
        }

        store.set(0,0, get(1,1) * get(2,2) - get(1,2) * get(2,1));
        store.set(0,1, get(0,2) * get(2,1) - get(0,1) * get(2,2));
        store.set(0,2, get(0,1) * get(1,2) - get(0,2) * get(1,1));
        store.set(1,0, get(1,2) * get(2,0) - get(1,0) * get(2,2));
        store.set(1,1, get(0,0) * get(2,2) - get(0,2) * get(2,0));
        store.set(1,2, get(0,2) * get(1,0) - get(0,0) * get(1,2));
        store.set(2,0, get(1,0) * get(2,1) - get(1,1) * get(2,0));
        store.set(2,1, get(0,1) * get(2,0) - get(0,0) * get(2,1));
        store.set(2,2, get(0,0) * get(1,1) - get(0,1) * get(1,0));

        store.mult(1f / det);
        return store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix3D)) return false;

        Matrix3D matrix3D = (Matrix3D) o;

        return Arrays.deepEquals(matrix, matrix3D.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}

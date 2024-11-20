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

public class Vector3D {

    /**
     * The 3 components of the vector in an array
     */
    protected double[] vector = new double[3];

    /**
     * Create a vector where all components are set to zero
     */
    public Vector3D() {
        for (int i = 0; i < 3; i++) {
            vector[i] = 0;
        }
    }

    /**
     * Create a vector where the components are set to the parameters
     *
     * @param x the desired x component of the vector
     * @param y the desired y component of the vector
     * @param z the desired z component of the vector
     */
    public Vector3D(double x, double y, double z) {
        vector[0] = x;
        vector[1] = y;
        vector[2] = z;
    }

    /**
     * Create a vector where the components are set to a 3-dimensional array
     *
     * @param arr the vector as a 3d array
     */
    public Vector3D(double[] arr) {
        assert arr.length == vector.length;
        vector[0] = arr[0];
        vector[1] = arr[1];
        vector[2] = arr[2];
    }

    /**
     * Returns a vector full of zeroes
     *
     * @return the zero vector
     */
    public static Vector3D zero() {
        return new Vector3D(0, 0, 0);
    }

    /**
     * Method to set the vectors components
     *
     * @param i   the position of the component
     * @param num the desired value of the component
     */
    public void set(int i, double num) {
        assert i >= 0 && i < vector.length;
        vector[i] = num;
    }

    /**
     * Return the x component of the vector
     *
     * @return the x component of the vector
     */
    public double getX() {
        return vector[0];
    }

    /**
     * Return the y component of the vector
     *
     * @return the y component of the vector
     */
    public double getY() {
        return vector[1];
    }

    /**
     * Return the z component of the vector
     *
     * @return the z component of the vector
     */
    public double getZ() {
        return vector[2];
    }

    /**
     * Returns the magnitude of the vector
     *
     * @return the magnitude of the vector
     */
    public double getMag() {
        double sum = 0;
        for (double v : vector) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }

    /**
     * Normalizes the vector's component to a certain overall vector length
     *
     * @param new_mag the desired magnitude
     */
    public void setMag(double new_mag) {
        double mag = getMag();
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] * new_mag / mag;
        }
    }

    /**
     * Adds a vector to the current vector.
     * Modifies the current vector
     *
     * @param other the other vector to be added to this one
     */
    public void addLocal(Vector3D other) {
        for (int i = 0; i < this.vector.length; i++) {
            this.vector[i] += other.vector[i];
        }
    }

    /**
     * Adds a vector to the current vector.
     * Returns a new Vector
     *
     * @param other the other vector to be added to this one
     * @return the new vector
     */
    public Vector3D add(Vector3D other) {
        Vector3D newVec = new Vector3D();
        for (int i = 0; i < this.vector.length; i++) {
            newVec.vector[i] = this.vector[i] + other.vector[i];
        }
        return newVec;
    }

    /**
     * Subtracts the other vector from this one.
     * Returns a new vector
     *
     * @param other the other vector
     * @return the new vector
     */
    public Vector3D sub(Vector3D other) {
        Vector3D newVec = new Vector3D();
        for (int i = 0; i < this.vector.length; i++) {
            newVec.vector[i] = this.vector[i] - other.vector[i];
        }
        return newVec;
    }

    /**
     * Returns the scalar product of this and another vector
     *
     * @param other the other vector
     * @return the scalar
     */
    public double dot(Vector3D other) {
        double sum = 0;
        for (int i = 0; i < this.vector.length; i++) {
            sum += this.vector[i] * other.vector[i];
        }
        return sum;
    }

    /**
     * Scalar multiply the current vector with a number.
     * Changes the current vector
     *
     * @param scalar the scalar
     */
    public void multiplyLocal(double scalar) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalar;
        }
    }

    /**
     * Get the distance from the current vector to another vector
     *
     * @param other the other vector
     * @return the distance from the current to the other vector
     */
    public double distTo(Vector3D other) {
        double sum = 0;
        for (int i = 0; i < this.vector.length; i++) {
            sum += Math.pow(this.vector[i] - other.vector[i], 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Helper function to show the vector on the console
     */
    public String toString() {
        return "(" + vector[0] + ", " + vector[1] + ", " + vector[2] + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3D vector3D)) return false;

        return Arrays.equals(vector, vector3D.vector);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vector);
    }
}

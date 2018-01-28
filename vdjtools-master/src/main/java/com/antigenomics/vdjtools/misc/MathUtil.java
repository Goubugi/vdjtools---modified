/*
 * Copyright (c) 2015, Bolotin Dmitry, Chudakov Dmitry, Shugay Mikhail
 * (here and after addressed as Inventors)
 * All Rights Reserved
 *
 * Permission to use, copy, modify and distribute any part of this program for
 * educational, research and non-profit purposes, by non-profit institutions
 * only, without fee, and without a written agreement is hereby granted,
 * provided that the above copyright notice, this paragraph and the following
 * three paragraphs appear in all copies.
 *
 * Those desiring to incorporate this work into commercial products or use for
 * commercial purposes should contact the Inventors using one of the following
 * email addresses: chudakovdm@mail.ru, chudakovdm@gmail.com
 *
 * IN NO EVENT SHALL THE INVENTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE INVENTORS HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE INVENTORS HAS
 * NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. THE INVENTORS MAKES NO REPRESENTATIONS AND EXTENDS NO
 * WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY
 * PATENT, TRADEMARK OR OTHER RIGHTS.
 */

package com.antigenomics.vdjtools.misc;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

import java.util.Random;

/**
 * Fast Java implementation for mathematical routines used in VDJtools
 */
public class MathUtil {
    /**
     * Upper limit on current precision of RepSeq
     */
    public static final double JITTER = 1e-9, JITTER_LOG10 = Math.log10(JITTER);

    private static final double HALF_LOG_2_PI = 0.5 * FastMath.log(2.0 * FastMath.PI);
    private static final long[] FACTORIALS = new long[]{
            1l, 1l, 2l,
            6l, 24l, 120l,
            720l, 5040l, 40320l,
            362880l, 3628800l, 39916800l,
            479001600l, 6227020800l, 87178291200l,
            1307674368000l, 20922789888000l, 355687428096000l,
            6402373705728000l, 121645100408832000l, 2432902008176640000l};

    /**
     * Computes the logarithm of n!/(n-k)!. Implemented based on apache math 3
     */
    public static double logFactorialRatio(final int n, final int k) {
        if (k > n)
            throw new IllegalArgumentException("k should be less or equal to n");

        return fastLogFactorial(n) - fastLogFactorial(n - k);
    }

    private static double fastLogFactorial(final long x) {
        return x < FACTORIALS.length ? FastMath.log(FACTORIALS[(int) x]) : fastLogGamma(x);
    }

    /**
     * Internal. Implemented based on apache math 3
     *
     * @param x
     * @return
     */
    private static double fastLogGamma(final double x) {
        double sum = Gamma.lanczos(x);
        double tmp = x + Gamma.LANCZOS_G + .5;
        return ((x + .5) * FastMath.log(tmp)) - tmp +
                HALF_LOG_2_PI + FastMath.log(sum / x);
    }

    public static double distanceCorrelation(final double[][] x, final double[][] y) {
        int n = x.length;
        if (n != y.length)
            throw new IndexOutOfBoundsException("x and y should be of same length");

        double[][] a = new double[n][n], b = new double[n][n];
        double[] a1m = new double[n], a2m = new double[n], b1m = new double[n], b2m = new double[n];
        double am = 0, bm = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double aa = euclDist(x[i], x[j]), bb = euclDist(y[i], y[j]);

                    a[i][j] = aa;
                    b[i][j] = bb;

                    a1m[i] += aa;
                    a2m[j] += aa;

                    b1m[i] += bb;
                    b2m[j] += bb;

                    am += aa;
                    bm += bb;
                }
            }
        }

        double[][] A = new double[n][n], B = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = a[i][j] - (a1m[i] - a2m[j] - am / n) / n;
                B[i][j] = b[i][j] - (b1m[i] - b2m[j] - bm / n) / n;
            }
        }

        double dCovXY = 0, dVarX = 0, dVarY = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double AA = A[i][j], BB = B[i][j];
                dCovXY += AA * BB;
                dVarX += AA * AA;
                dVarY += BB * BB;
            }
        }

        return Math.sqrt(dCovXY) / Math.sqrt(Math.sqrt(dVarX * dVarY));
    }

    private static double euclDist(final double[] xx, final double[] yy) {
        int n = xx.length;
        if (n != yy.length)
            throw new IndexOutOfBoundsException("x and y should be of same length");

        double dist = 0;

        for (int i = 0; i < n; i++) {
            dist += xx[i] * yy[i];
        }

        return Math.sqrt(dist);
    }

    public static <T> void shuffle(final T[] arr) {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            T tmp = arr[index];
            arr[index] = arr[i];
            arr[i] = tmp;
        }
    }

    public static double JSD(final double[] pArr, final double[] qArr) throws Exception {
        int n = pArr.length;

        if (n != qArr.length)
            throw new RuntimeException("Input histograms must be of same length");

        double pSum = 0, qSum = 0;

        for (int i = 0; i < n; i++) {
            pSum += pArr[i];
            qSum += qArr[i];
        }

        double jsd = 0;
        for (int i = 0; i < n; i++) {
            double p = pArr[i] / pSum, q = qArr[i] / qSum,
                    m = (p + q) / 2.0;
            jsd += (p > 0 ? (Math.log(p / m) * p) : 0d) + (q > 0 ? (Math.log(q / m) * q) : 0d);
        }

        return jsd / 2.0 / Math.log(2.0);
    }
}

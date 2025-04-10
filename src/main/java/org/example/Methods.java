package org.example;

import java.awt.*;

/**
 * A utility class for applying geometric transformations (rotation, deformation, reflection, translation)
 * to a set of 2D points using matrix operations. Also provides conversions between logical and screen coordinates.
 */
public class Methods {

    // Logical coordinate bounds
    static double X1 = -5;
    static double X2 = 5;
    static double Y1 = -5;
    static double Y2 = 5;

    // Screen coordinate bounds
    int I1, I2, J1, J2;

    // Transformation matrices
    double[][] rotateMatrix;
    double[][] deformationMatrix;
    double[][] reflectionMatrix;
    double[][] translationMatrix;

    /**
     * Constructor initializing the screen coordinate bounds.
     *
     * @param I1 left screen coordinate
     * @param I2 right screen coordinate
     * @param J1 top screen coordinate
     * @param J2 bottom screen coordinate
     */
    public Methods(int I1, int I2, int J1, int J2) {
        this.I1 = I1;
        this.I2 = I2;
        this.J1 = J1;
        this.J2 = J2;
    }

    /**
     * Converts logical X coordinate to screen coordinate I.
     */
    public int fromXtoI(double x) {
        return I1 + (int)((x - X1) / (X2 - X1)*(I2 - I1));
    }

    /**
     * Converts logical Y coordinate to screen coordinate J.
     */
    public int fromYtoJ(double y) {
        return J2 + (int)((y - Y1) / (Y2 - Y1) * (J1 - J2));
    }

    /**
     * Converts screen coordinate I to logical X.
     */
    public double fromItoX(int i) {
        return X1 + (i - I1) * (X2 - X1) / (I2 - I1);
    }

    /**
     * Converts screen coordinate J to logical Y.
     */
    public double fromJtoY(int j) {
        return Y1 + (J2 - j) * (Y2 - Y1) / (J2 - J1);
    }

    /**
     * Applies the selected transformation matrix to a set of points.
     *
     * @param points        array of points to transform
     * @param tag           transformation type (0 = rotate, 1 = deform, 2 = reflect, 3 = translate)
     * @param countOfPoints number of points in the array
     */
    public void transformCoordinates(Point[] points, int tag, int countOfPoints) {
        double[][] coordinate = new double[countOfPoints][3]; // logical coordinates with homogeneous component
        int[][] vector = new int[countOfPoints][2];           // screen coordinates

        // Convert points from screen to logical coordinates
        for (int i = 0; i < countOfPoints; i++) {
            vector[i][0] = points[i].x;
            vector[i][1] = points[i].y;
        }
        for (int i = 0; i < countOfPoints; i++) {
            coordinate[i][0] = fromItoX(vector[i][0]);
            coordinate[i][1] = fromJtoY(vector[i][1]);
            coordinate[i][2] = 1; // homogeneous coordinate
        }

        // Apply the selected matrix transformation
        switch (tag) {
            case 0:
                matrixMultiplication(coordinate, countOfPoints, rotateMatrix);
                break;
            case 1:
                matrixMultiplication(coordinate, countOfPoints, deformationMatrix);
                break;
            case 2:
                matrixMultiplication(coordinate, countOfPoints, reflectionMatrix);
                break;
            case 3:
                matrixMultiplication(coordinate, countOfPoints, translationMatrix);
                break;
        }

        // Convert coordinates back from logical to screen
        for (int i = 0; i < countOfPoints; i++) {
            vector[i][0] = fromXtoI(coordinate[i][0]);
            vector[i][1] = fromYtoJ(coordinate[i][1]);
        }

        // Update original point array
        for (int i = 0; i < countOfPoints; i++) {
            points[i].x = vector[i][0];
            points[i].y = vector[i][1];
        }
    }

    /**
     * Multiplies a matrix of coordinates by a 3x3 transformation matrix.
     *
     * @param matrix1        the points matrix (countOfPoints x 3)
     * @param countOfPoints  number of points
     * @param matrix2        the transformation matrix (3x3)
     */
    public void matrixMultiplication(double[][] matrix1, int countOfPoints, double[][] matrix2) {
        double[][] result = new double[countOfPoints][3];
        for (int i = 0; i < countOfPoints; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        // Copy result back into matrix1
        for (int i = 0; i < countOfPoints; i++)
            System.arraycopy(result[i], 0, matrix1[i], 0, 3);
    }

    /**
     * Constructs a transformation matrix based on the tag and parameters.
     *
     * @param tag           matrix type (0 = rotation, 1 = deformation, 2 = reflection, 3 = translation)
     * @param matrixParams  parameters for the matrix (angle, scale, etc.)
     */
    public void matrixConstruct(int tag, double... matrixParams) {
        switch (tag) {
            case 0: // Rotation matrix
                rotateMatrix = new double[3][3];
                rotateMatrix[0][0] = rotateMatrix[1][1] = Math.cos(matrixParams[0]);
                rotateMatrix[0][1] = Math.sin(matrixParams[0]);
                rotateMatrix[1][0] = -Math.sin(matrixParams[0]);
                rotateMatrix[2][2] = 1;
                for (int i = 0; i < 2; i++) {
                    rotateMatrix[i][2] = rotateMatrix[2][i] = 0;
                }
                break;
            case 1: // Deformation (scaling) matrix
                deformationMatrix = new double[3][3];
                deformationMatrix[2][2] = 1;
                for (int i = 0; i < 2; i++) {
                    deformationMatrix[i][i] = matrixParams[i];
                    deformationMatrix[i][2] = deformationMatrix[2][i] = 0;
                }
                break;
            case 2: // Reflection matrix
                reflectionMatrix = new double[3][3];
                reflectionMatrix[2][2] = 1;
                for (int i = 0; i < 2; i++) {
                    reflectionMatrix[i][i] = matrixParams[i];
                    reflectionMatrix[i][2] = reflectionMatrix[2][i] = 0;
                }
                break;
            case 3: // Translation matrix
                translationMatrix = new double[3][3];
                translationMatrix[0][0] = translationMatrix[1][1] = 1;
                translationMatrix[0][1] = translationMatrix[1][0] = 0;
                translationMatrix[2][2] = 1;
                for (int i = 0; i < 2; i++) {
                    translationMatrix[i][2] = 0;
                    translationMatrix[2][i] = matrixParams[i] / 10; // normalized offset
                }
                break;
        }
    }
}

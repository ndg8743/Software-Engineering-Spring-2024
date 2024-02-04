package org.legacy;

import java.awt.geom.Path2D;
import java.math.BigInteger;

public class BigIntRectangle extends Path2D.Double {

    private BigInteger posX;
    private BigInteger posY;
    private BigInteger width;
    private BigInteger height;

    public BigIntRectangle(BigInteger posX, BigInteger posY, BigInteger width, BigInteger height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        createPath();
    }

    public void createPath() {
        // Move to starting point
        moveTo(posX.doubleValue(), posY.doubleValue());

        // Line to other points to create a rectangle
        lineTo(posX.add(width).doubleValue(), posY.doubleValue());
        lineTo(posX.add(width).doubleValue(), posY.add(height).doubleValue());
        lineTo(posX.doubleValue(), posY.add(height).doubleValue());

        // Close the path to create a rectangle
        closePath();
    }

}

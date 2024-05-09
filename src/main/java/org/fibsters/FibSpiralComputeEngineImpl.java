package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibSpiralComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.util.BigIntUtil;
import org.legacy.BigIntRectangle;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class FibSpiralComputeEngineImpl implements FibSpiralComputeEngine {

    private ComputeJobStatus status;
    private int[] fibonacci;
    private int startIndex;
    private int endIndex;
    private InputPayload inputPayload; // used for reference potentially(has uuid)

    private OutputPayloadImpl outputPayload;

    private int chunk;
    private int maxElement;
    private double posX;
    private double posY;
    private int angle;
    private double scaledFib;
    private double currentFib;

    private static final int WIDTH = 2000;
    private static final int HEIGHT = 2000;

    private String fileName = "";

    public FibSpiralComputeEngineImpl(OutputPayloadImpl outputPayload, int chunk) {
        this.status = ComputeJobStatus.UNSTARTED;
        this.startIndex = 0;

        this.outputPayload = outputPayload;
        this.chunk = chunk;
        this.fibonacci = this.outputPayload.getFibCalcResultsInteger2dList().get(this.chunk);
        this.endIndex = this.fibonacci.length - 1;
        this.maxElement = this.fibonacci.length;
    }

    @Override
    public void setInputPayload(InputPayload inputPayload) {
        this.inputPayload = inputPayload;
    }

    @Override
    public OutputPayload getOutputPayload() {
        return this.outputPayload;
    }

    @Override
    public ComputeJobStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(ComputeJobStatus status) {
        this.status = status;
    }

    @Override
    public int getTotalSize() {
        return this.fibonacci.length;
    }

    @Override
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public InputPayload getInputPayload() {
        return this.inputPayload;
    }

    @Override
    public ComputeJob clone() {
        FibSpiralComputeEngineImpl clone = new FibSpiralComputeEngineImpl(this.outputPayload, this.chunk);

        clone.setStatus(this.status);
        clone.setStartIndex(this.startIndex);
        clone.setEndIndex(this.endIndex);
        clone.setInputPayload(this.inputPayload);
        clone.setOutputPayload(this.outputPayload);
        clone.setChunk(this.chunk);

        return clone;
    }

    @Override
    public int getChunk() {
        return this.chunk;
    }

    @Override
    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    @Override
    public void setOutputPayload(OutputPayloadImpl outputPayload) {
        this.outputPayload = outputPayload;
    }

    @Override
    public void run() {
        this.status = ComputeJobStatus.RUNNING;
        this.generateValues();

        //draw();

        this.status = ComputeJobStatus.COMPLETED;
    }

    private void generateValues() {
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        int x = 0;
        int y = 0;
        int largestFib = fibonacci[maxElement - 1];
        int largestFib2 = fibonacci[maxElement - 2];
        double scale = 1.0 * WIDTH / (largestFib2 + largestFib);

        for (int i = 0; i < maxElement; i++) {
            int currentFib = fibonacci[i];
            int previousFib = i - 1 < 0 ? 0 : fibonacci[i - 1];
            int previousPreviousFib = i - 2 < 0 ? 0 : fibonacci[i - 2];

            // Calculate next position and angle

            int[] deltaXY = new int[2];

            switch (angle) {
                case 0:
                    deltaXY[0] = -previousPreviousFib;
                    deltaXY[1] = -currentFib;
                    break;
                case 90:
                    deltaXY[0] = -currentFib;
                    break;
                case 180:
                    deltaXY[1] = previousFib;
                    break;
                case 270:
                    deltaXY[0] = previousFib;
                    deltaXY[1] = -previousPreviousFib;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.angle);
            }

            System.out.println(currentFib + ", " + previousFib + ", " + previousPreviousFib);
            System.out.println("Angle: " + angle);
            System.out.println("DeltaXY: " + deltaXY[0] + ", " + deltaXY[1]);

            x += deltaXY[0];
            y += deltaXY[1];

            double scaledX = (x * scale) + (centerX);
            double scaledY = (y * scale) + (centerY);
            double scaledFib = (currentFib * scale);

            //executor.submit(new FibonacciFractalGenerator.FractalDrawingTask(image, scaledX, scaledY, angle, scaledFib, currentFib));
            this.posX = scaledX;
            this.posY = scaledY;
            this.scaledFib = scaledFib;
            this.currentFib = currentFib;

            draw();

            this.angle += 90;
            this.angle %= 360;
        }
    }

    private void draw() {
        BufferedImage image = this.outputPayload.getOutputImage(this.chunk); //image to write to
        //TODO: do the writing that's in the legacy class

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.BLUE);

        String debugMessage = "[Fib] (Draw) - ";

        System.out.println(debugMessage + " start angle: " + angle);

        synchronized (image) {
            // draw a dot at the center of the image
            double arcX = posX;
            double arcY = posY;

            if (angle == 0) {
                arcX = posX - scaledFib;
                arcY = posY;

                graphics.setColor(Color.GRAY);

                System.out.println(debugMessage + " <gray> " + "arcX: " + arcX + ", arcY: " + arcY);
            } else if (angle == 90) {
                arcX = posX;
                arcY = posY;

                graphics.setColor(Color.WHITE);

                System.out.println(debugMessage + " <white> " + "arcX: " + arcX + ", arcY: " + arcY);
            } else if (angle == 180) {
                arcX = posX;
                arcY = posY - scaledFib;

                graphics.setColor(Color.ORANGE);

                System.out.println(debugMessage + " <orange> " + "arcX: " + arcX + ", arcY: " + arcY);
            } else if (angle == 270) {
                arcX = posX - scaledFib;
                arcY = posY - scaledFib;

                graphics.setColor(Color.GREEN);

                System.out.println(debugMessage + " <green> " + "arcX: " + arcX + ", arcY: " + arcY);
            }

            Shape shape = new BigIntRectangle(BigIntUtil.toBigInt(posX), BigIntUtil.toBigInt(posY), BigIntUtil.toBigInt(scaledFib), BigIntUtil.toBigInt(scaledFib));

            graphics.draw(shape);
            graphics.setColor(Color.RED);

            Arc2D arc = new Arc2D.Double(arcX, arcY, 2 * scaledFib - 1, 2 * scaledFib - 1, angle, 90, Arc2D.OPEN);

            graphics.draw(arc);

            graphics.drawString(Double.toString(currentFib), (int) (posX + scaledFib / 2), (int) (posY + scaledFib / 2));
        }

        graphics.dispose();
    }

    public void saveBuffer() {
        for (int i = 0; i < this.outputPayload.getFibCalcResultsInteger2dList().size(); i++) {
            BufferedImage image = this.outputPayload.getOutputImage(i); //image to write to

            this.fileName = "fib" + i;

            saveImage(image, fileName + ".png");
        }
    }

    public void saveImage(BufferedImage image, String fileName) {
        try {
            ImageIO.write(image, "png", new File(fileName));

            System.out.println("Fibonacci fractal image saved as " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

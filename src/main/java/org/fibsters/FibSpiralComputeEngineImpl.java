package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.FibSpiralComputeEngine;
import org.fibsters.interfaces.InputPayload;
import org.fibsters.interfaces.OutputPayload;
import org.fibsters.util.BigIntUtil;
import org.legacy.BigIntRectangle;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class FibSpiralComputeEngineImpl implements FibSpiralComputeEngine {

    private ComputeJobStatus status;
    private int[] fibonacci;
    private int startIndex;
    private int endIndex;
    private InputPayload inputPayload; // used for reference potentially(has uuid)

    private OutputPayloadImpl outputPayload;

    private int chunk;

    public FibSpiralComputeEngineImpl(OutputPayloadImpl outputPayload) {
        this.status = ComputeJobStatus.UNSTARTED;
        this.startIndex = 0;
        this.endIndex = 0;
        this.outputPayload = outputPayload;
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
        FibSpiralComputeEngineImpl clone = new FibSpiralComputeEngineImpl(this.outputPayload);

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
        BufferedImage image = this.outputPayload.getOutputImage(); //image to write to
        //TODO: do the writing that's in the legacy class

        // TODO: change this!
        double posX = 0.0;
        double posY = 0.0;
        int angle = 0;
        double scaledFib = 0.0;
        double currentFib = 0.0;

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.BLUE);

        synchronized (image) {
            // draw a dot at the center of the image
            double arcX = posX;
            double arcY = posY;

            if (angle == 0) {
                arcX = posX - scaledFib;
                arcY = posY;
                graphics.setColor(Color.GRAY);
            } else if (angle == 90) {
                arcX = posX;
                arcY = posY;
                graphics.setColor(Color.WHITE);
            } else if (angle == 180) {
                arcX = posX;
                arcY = posY - scaledFib;
                graphics.setColor(Color.ORANGE);
            } else if (angle == 270) {
                arcX = posX - scaledFib;
                arcY = posY - scaledFib;
                graphics.setColor(Color.GREEN);
            }

            Shape shape = new BigIntRectangle(BigIntUtil.toBigInt(posX), BigIntUtil.toBigInt(posY), BigIntUtil.toBigInt(scaledFib), BigIntUtil.toBigInt(scaledFib));

            graphics.draw(shape);
            graphics.setColor(Color.RED);

            Arc2D arc = new Arc2D.Double(arcX, arcY, 2 * scaledFib - 1, 2 * scaledFib - 1, angle, 90, Arc2D.OPEN);

            graphics.draw(arc);

            graphics.drawString(Double.toString(currentFib), (int) (posX + scaledFib / 2), (int) (posY + scaledFib / 2));
        }

        graphics.dispose();

        this.status = ComputeJobStatus.COMPLETED;
    }

}

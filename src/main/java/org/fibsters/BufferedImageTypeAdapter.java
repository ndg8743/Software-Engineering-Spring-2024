package org.fibsters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Base64;

public class BufferedImageTypeAdapter extends TypeAdapter<BufferedImage> {

    public enum ImageType {

        PNG,
        NULL

    }

    ImageType imageType;

    public BufferedImageTypeAdapter() {
        imageType = ImageType.PNG;
    }

    public BufferedImageTypeAdapter(ImageType imageType) {
        this.imageType = imageType;
    }

    @Override
    public void write(JsonWriter jsonWriter, BufferedImage image) throws IOException {
        if (this.imageType == ImageType.NULL) {
            jsonWriter.nullValue();
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String imageExtension = "png";

        if (this.imageType == ImageType.PNG) {
            imageExtension = "png";
        }

        ImageIO.write(image, imageExtension, bos);

        byte[] imageBytes = bos.toByteArray();
        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

        jsonWriter.value(imageBase64);
    }

    @Override
    public BufferedImage read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        byte[] imageBytes = Base64.getDecoder().decode(jsonReader.nextString());
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

}
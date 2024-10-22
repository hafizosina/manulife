package com.manulife.id.util;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.exception.BadRequestException;
import com.manulife.id.exception.ProcessException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Utils {
    public static byte[] decodeBase64ToImage(String base64Image) {
        try {
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }
            return Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid base64 encoding", ResponseCode.GENERAL_INVALID_FORMAT);
        }
    }

    public static void validateImage(byte[] byteImage ){

        if (byteImage != null) {

            int maxSizeInBytes = 1_048_576;
            if (byteImage.length > maxSizeInBytes) {
                throw new BadRequestException("Image size exceeds the 1MB limit", ResponseCode.FILE_SIZE_TO_LARGE);
            }

            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteImage));
                if (bufferedImage == null) {
                    throw new BadRequestException("Invalid image format", ResponseCode.GENERAL_INVALID_FORMAT);
                }
            } catch (IOException e) {
                throw new ProcessException("Error processing image", ResponseCode.GENERAL_ERROR);
            }
        }else {
            throw new BadRequestException("File is null", ResponseCode.GENERAL_NOT_FOUND);
        }
    }
}

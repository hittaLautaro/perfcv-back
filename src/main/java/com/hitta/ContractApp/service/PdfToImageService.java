package com.hitta.ContractApp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class PdfToImageService {

    private static final int DPI = 150; // Resolution for the image
    private static final String IMAGE_FORMAT = "PNG";

    public byte[] convertFirstPageToImage(MultipartFile pdfFile) throws IOException {
        try (InputStream inputStream = pdfFile.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {

            if (document.getNumberOfPages() == 0) {
                throw new IllegalArgumentException("PDF has no pages");
            }

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, DPI);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, IMAGE_FORMAT, baos);
            byte[] imageBytes = baos.toByteArray();
            return imageBytes;
        } catch (IOException e) {
            log.error("error converting PDF to image", e);
            throw new IOException("Failed to convert PDF to image: " + e.getMessage(), e);
        }
    }

    public InputStream convertFirstPageToImageStream(MultipartFile pdfFile) throws IOException {
        byte[] imageBytes = convertFirstPageToImage(pdfFile);
        return new ByteArrayInputStream(imageBytes);
    }
}

package com.hitta.ContractApp.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContractService {

    public List<String> upload(MultipartFile file) throws Exception {
        String text = extractTextFromPdf(file);
        List<String> fields = extractPlaceholders(text);
        return fields;
    }

    public String extractTextFromPdf(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             RandomAccessRead rar = new RandomAccessReadBuffer(inputStream);
             PDDocument document = Loader.loadPDF(rar)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }


    public List<String> extractPlaceholders(String text) {
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(text);
        List<String> placeholders = new ArrayList<>();
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders;
    }
}

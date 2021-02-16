package ru.mpei.requests.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileService {
    public XWPFDocument openFile(String name) throws IOException {
        File file = new File("C:/username/document.docx");
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        return new XWPFDocument(fis);
    }
}

package ru.gb.ConverterBot.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileUploadController {

    // Endpoint to receive MP4 files from users
    @PostMapping("/uploadMP4")
    public ResponseEntity<?> uploadMP4File(@RequestParam("file") MultipartFile file) {
        try {
            // Save the MP4 file to a specified location
            String fileName = file.getOriginalFilename();
            String filePath = "/path/to/save/" + fileName;
            file.transferTo(new File(filePath));

            // Convert MP4 to MP3
            String mp3FilePath = performMp4ToMp3Conversion(filePath);

            // Send the MP3 file back to the user for download
            if(mp3FilePath != null) {
                File mp3File = new File(mp3FilePath);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", mp3File.getName());
                return new ResponseEntity<>(new FileSystemResource(mp3File), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to convert MP4 to MP3", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String performMp4ToMp3Conversion(String inputFilePath) {
        String outputFilePath = "/path/to/output.mp3";

        try {
            String ffmpegCommand = "ffmpeg -i " + inputFilePath + " -vn -acodec libmp3lame -ar 44100 -ac 2 -b:a 192k " + outputFilePath;
            Process process = Runtime.getRuntime().exec(ffmpegCommand);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null; // Return null if conversion fails
        }

        return outputFilePath;
    }
}
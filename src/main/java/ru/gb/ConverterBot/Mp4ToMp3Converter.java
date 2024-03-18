package ru.gb.ConverterBot;

import java.io.IOException;

public class Mp4ToMp3Converter {

    public static void main(String[] args) {
        String mp4FilePath = "input.mp4";
        String mp3FilePath = "output.mp3";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", mp4FilePath, "-vn", "-acodec", "libmp3lame", "-ar", "44100", "-ac", "2", "-ab", "192k", mp3FilePath);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("MP4 to MP3 conversion completed.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

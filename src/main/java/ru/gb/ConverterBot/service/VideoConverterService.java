package ru.gb.ConverterBot.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class VideoConverterService {

    @Autowired
    private FFmpeg ffmpeg;

    public void convertMp4ToMp3(String inputFilePath, String outputFilePath) {
        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(String.valueOf(new File(inputFilePath)))
                    .addOutput(new File(outputFilePath).toURI())
                    .setFormat("mp3")
                    .setAudioCodec("libmp3lame")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
            executor.createJob(builder).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

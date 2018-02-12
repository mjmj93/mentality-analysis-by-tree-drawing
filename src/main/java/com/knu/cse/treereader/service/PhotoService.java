package com.knu.cse.treereader.service;

import com.knu.cse.treereader.ExeptionHandling.StorageException;
import com.knu.cse.treereader.ExeptionHandling.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
public class PhotoService {

    private final Path rootLocation = Paths.get("/Users/minjae/Desktop/servertest");

    public void store(MultipartFile file, @RequestParam("id")String fileId) {

        //넘어온 스트링을 꺼내서
        String filename = fileId;
        //파일네임으로 주면
        //StringUtils.cleanPath(file.getOriginalFilename());
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
            String s = "/Users/minjae/Desktop/servertest/"+filename;

            out.write(s);
            out.newLine();

            out.close();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        try {
            if (file.isEmpty()) {
                System.out.println("Failed to store empty file " + filename);
//				throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                System.out.println("Cannot store file with relative path outside current directory " + filename);
                // This is a security check
//				throw new StorageException(
//						"Cannot store file with relative path outside current directory "
//								+ filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println(filename);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to store file " + filename);
//			throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read stored files");
//			throw new StorageException("Failed to read stored files", e);
        }
        return null;
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                System.out.println("Could not read file: " + filename);
//				throw new StorageFileNotFoundException(
//						"Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            System.out.println("Could not read file: " + filename);
//			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
        return null;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not initialize storage");
//			throw new StorageException("Could not initialize storage", e);
        }
    }

}


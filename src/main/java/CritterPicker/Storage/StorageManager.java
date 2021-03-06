package CritterPicker.Storage;

import CritterPicker.Storage.Exceptions.StorageException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
public class StorageManager {

    private final Path rootLocation = Paths.get("src/main/resources/static/upload-dir");

    public void store(MultipartFile file){
        try {
            if(file.isEmpty()){
                throw new StorageException("Failed to store empty file!");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
            if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){
                throw new StorageException("Cannot store file outside current directory!");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e){
            throw new StorageException("Failed to store file!", e);
        }
    }

    public boolean checkFilename(String filename){
        File file = new File(rootLocation + "/" + filename);
        return file.exists();
    }

    public void deleteFile(String filename){
        File file = new File(rootLocation + "/" + filename);
        file.delete();
    }
}

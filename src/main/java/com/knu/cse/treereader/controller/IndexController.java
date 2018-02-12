package com.knu.cse.treereader.controller;


import com.knu.cse.treereader.model.Photo;
import com.knu.cse.treereader.model.User;
import com.knu.cse.treereader.service.PhotoService;
import com.knu.cse.treereader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//url 로 요청이 들어오면 매핑되는 메소드를 찾아서 수행.


@RestController
public class IndexController {
    // GET(조회), POST(추가), PUT(수정), DELETE(삭제) http method
    //브라우져에서 날리는건 무조건 get

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @GetMapping("/users")
    public List<User> getUser() {
        return userService.findAll();
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

    //request 날릴때
    @PostMapping("/users")
    public User saveUser(@RequestBody User user) {
        System.out.println("POST: /users:" + user);
        return userService.save(user);
    }

    @GetMapping("/users/{userId}/{password}") // {}이렇게 되어있는게 pathvariable
    public User getUserByUserId(@PathVariable String userId, @PathVariable String password) {
        return userService.findByUserIdAndPassword(userId, password);
    }

    @PutMapping("/users/{id}")
    public User putUserbyId(@PathVariable Integer id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @GetMapping("/photo")
    public Stream<Path> getPhoto() {
        return photoService.loadAll();
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", photoService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(IndexController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = (Resource) photoService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/download/{user}", produces = "text/plain")
    public String getResult(@PathVariable String user) {

        System.out.println("download");
        String s = "";
        String temp;

        try {
            String path = "/Users/minjae/Desktop/result/"+user+".txt";
            BufferedReader in = new BufferedReader(new FileReader(path));

            while((temp = in.readLine())!= null)
                s = s + temp + "\n";
        } catch (Exception e) {e.printStackTrace();}
        //텍스트를 읽어서 리턴
        System.out.println(s);
        return s;
        //반환값을 바꾸면댄다
    }

    @PostMapping("/upload")
    public void handleFileUpload(@RequestParam("picture") MultipartFile file,@RequestParam("id") String fileId,
                                   RedirectAttributes redirectAttributes)
    {
        photoService.store(file,fileId+".jpg");


        System.out.println("upload");
//        String s="";
//        String temp;
//
//        try {
//            String path = "/Users/minjae/Desktop/result/"+fileId+".txt";
//            BufferedReader in = new BufferedReader(new FileReader(path));
//
//            while((temp = in.readLine())!= null)
//                s = s + temp + "\n";
//        } catch (Exception e) {e.printStackTrace();}
//
//
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");
//        //텍스트를 읽어서 리턴
//
//
//        return s;
//        //반환값을 바꾸면댄다
    }
}

//@PathVariable 찾자
//@RequestBody 바디값에대한 요청
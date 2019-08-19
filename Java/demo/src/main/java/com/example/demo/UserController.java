package com.example.demo;


import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        if (isValidPassword(user.getPassword()) && !containsUser(user))
            return userRepository.save(user);
        return null;
    }

    @PostMapping("/login")
    public Boolean confirmUser(@Valid @RequestBody User user) {
        return containsUser(user) && passwordUser(user);
    }

    @PostMapping("/images")
    public TextResponse addImage(@Valid @RequestBody Image image) {
        System.out.print(image.getImage());
        String b64 = image.getImage().replace("\n", "");
        //System.out.print(b64);


        decodeToImage(b64);
        //
//        try {
//            return processImage();
//        } catch (IOException io) {
//            return "Error processing the image";
//        }
        return new TextResponse("okay");
    }

//    private void decodeImage(String image) throws IOException {
//        System.out.print(image);
//
//        byte[] bytes = image.getBytes();
//        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//        BufferedImage bImage2 = ImageIO.read(bis);
//        ImageIO.write(bImage2, "jpg", new File("C:\\Users\\iri\\Desktop\\demo\\script\\temp.jpg"));
//        System.out.println("image created");
//    }

    private static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            File file = new File("C:\\Users\\iri\\Desktop\\demo\\script\\temp.jpg");
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }


    private String processImage() throws IOException {
        String command = "python C:\\Users\\iri\\Documents\\GitHub\\BachelorThesis\\Java\\thesis\\script\\ocr.py";
        Process p = Runtime.getRuntime().exec(command);


        BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = bfr.readLine()) != null) {
            text.append(line);
        }
        return text.toString();
    }

    private Boolean containsUser(User user) {
        for (User element : userRepository.findAll()) {
            if (element.getUsername().equals(user.getUsername()))
                return true;
        }
        return false;
    }

    private Boolean passwordUser(User user) {
        for (User element : userRepository.findAll()) {
            if (element.getPassword().equals(user.getPassword()))
                return true;
        }
        return false;
    }

    private Boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}

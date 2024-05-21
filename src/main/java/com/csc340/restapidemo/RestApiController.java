package com.csc340.restapidemo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestApiController {

    private static final String FILE_PATH = "students.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Integer, Student> studentDatabase = new HashMap<>();

    public RestApiController() {
        loadStudentsFromFile();
    }

    private void loadStudentsFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                studentDatabase = objectMapper.readValue(file, new TypeReference<Map<Integer, Student>>() {});
            }
        } catch (IOException e) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, "Failed to load students from file", e);
        }
    }

    private void saveStudentsToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), studentDatabase);
        } catch (IOException e) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, "Failed to save students to file", e);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "Dora") String name) {
        return "Hola, soy " + name;
    }

    @GetMapping("students/all")
    public Object getAllStudents() {
        if (studentDatabase.isEmpty()) {
            studentDatabase.put(1, new Student(1, "sample1", "csc", 3.86));
        }
        return studentDatabase.values();
    }

    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentDatabase.get(id);
    }

    @PostMapping("students/create")
    public Object createStudent(@RequestBody Student student) {
        studentDatabase.put(student.getId(), student);
        saveStudentsToFile();
        return studentDatabase.values();
    }

    @PutMapping("students/update/{id}")
    public Object updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
        // Ensure the ID in the path variable matches the ID in the request body
        if (!studentDatabase.containsKey(id)) {
            return "Student with ID " + id + " does not exist.";
        }

        // Update the student's ID from the request body if necessary
        updatedStudent.setId(id);

        // Update the student in the database
        studentDatabase.put(id, updatedStudent);
        saveStudentsToFile();

        return "Student updated successfully.";
    }

    @DeleteMapping("students/delete/{id}")
    public Object deleteStudent(@PathVariable int id) {
        if (!studentDatabase.containsKey(id)) {
            return "Student with ID " + id + " does not exist.";
        }

        studentDatabase.remove(id);
        saveStudentsToFile();
        return studentDatabase.values();
    }



    @GetMapping("/quote")
    public Object getQuote() {
        try {
            String url = "https://api.quotable.io/random";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jSonQuote = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jSonQuote);

            String quoteAuthor = root.get("author").asText();
            String quoteContent = root.get("content").asText();
            System.out.println("Author: " + quoteAuthor);
            System.out.println("Quote: " + quoteContent);

            return root;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, null, ex);
            return "error in /quote";
        }
    }

    @GetMapping("/univ")
    public Object getUniversities() {
        try {
            String url = "http://universities.hipolabs.com/search?name=sports";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);

            for (JsonNode rt : root) {
                String name = rt.get("name").asText();
                String country = rt.get("country").asText();
                System.out.println(name + ": " + country);
            }

            return root;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, null, ex);
            return "error in /univ";
        }
    }

    @GetMapping("/weather")
    public Object getWeather(@RequestParam(value = "city", defaultValue = "London") String city) {
        try {
            String apiKey = "2665d6bf2317594fcb3763d99ce8d46a"; // Replace with your OpenWeatherMap API key
            // DO NOT SEARCH THIS KEY ON THE BROWSER SEARCH THE KEY IN THE COMMENTS
            // RIGHT BELOW THIS
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            // HERE IS WHAT THE KEY SHOULD LOOK LIKE.
            // THIS KEY! --> COPY AND PASTE THE FOLLOWING:
            // http://api.openweathermap.org/data/2.5/weather?q=London&appid=2665d6bf2317594fcb3763d99ce8d46a
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonWeather = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonWeather);

            // Parse the response to extract relevant information
            String weatherDescription = root.get("weather").get(0).get("description").asText();
            double temperature = root.get("main").get("temp").asDouble();
            int humidity = root.get("main").get("humidity").asInt();

            // Print the extracted information to the system output
            System.out.println("Weather in " + city + ": " + weatherDescription);
            System.out.println("Temperature: " + temperature + "K");
            System.out.println("Humidity: " + humidity + "%");

            // Return the parsed response
            Map<String, Object> weatherInfo = new HashMap<>();
            weatherInfo.put("description", weatherDescription);
            weatherInfo.put("temperature", temperature);
            weatherInfo.put("humidity", humidity);

            return weatherInfo;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, null, ex);
            return "error in /weather";
        }
    }
}

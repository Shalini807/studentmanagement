package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.api.db.Database;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.StudentService;

import javax.inject.Inject;
import java.sql.*;

public class StudentController extends Controller {
    private StudentService studentService;
    private FormFactory formFactory;
    @Inject
    public StudentController(StudentService studentService, FormFactory formFactory) {
        this.studentService = studentService;
        this.formFactory = formFactory;
    }

    @Inject
    Database db;

    public Result connection() {
        try (Connection connection = db.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
                while (resultSet.next()){
                    System.out.println(resultSet.getString("idstudent"));
                }
            }
        } catch (SQLException e) {
            return internalServerError("Failed to connect to the database");
        }

        return ok("Successfully connected to the database");
    }


    public Result getStudent() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String address = resultSet.getString("address");
            result.add(JsonNodeFactory.instance.objectNode().put("id", id).put("name", name).put("email",email).put("address",address));
        }
        return ok(result);
    }

    public Result getStudentById(int id) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE id=?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String address = resultSet.getString("address");
            result.put("id", id).put("name", name).put("email",email).put("address",address);
        }
        return ok(result);
    }

    public Result addStudent(JsonNode body) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO student(name,email,address) VALUES (?,?,?)");
        statement.setString(1, body.get("name").textValue());
        statement.setString(2, body.get("email").textValue());
        statement.setString(3, body.get("address").textValue());
        statement.executeUpdate();
        return ok();
    }
//    public Result addStudent(Http.Request request){
//        Form<Student> form = formFactory.form(Student.class).bindFromRequest(request);
//        return ok(studentService.addStudent(form.get()).toString());
//    }

    //    @Autowired
//    private StudentRepository studentRepository;


//    @Inject
//    private RestHighLevelClient client = new RestHighLevelClient(
//        RestClient.builder(
//                new HttpHost("localhost", 9200, "http")
//        )
//    );


//    @PostMapping
//    public Result create(Http.Request request) {
//        JsonNode json = request.body().asJson();
//        Student student = Json.fromJson(json, Student.class);
//        studentRepository.save(student);
//        return ok(Json.toJson(student));
//    }
//
//    @GetMapping
//    public Result get(Long id){
//        Student student = studentRepository.findById(id).orElse(null);
//        if (student == null) {
//            return notFound();
//        }
//        return ok(Json.toJson(student));
//    }
//
//    @PutMapping
//    public Result update(Long id, Http.Request request){
//        Student student = studentRepository.findById(id).orElse(null);
//        if (student == null){
//            return notFound();
//        }
//        JsonNode json = request.body().asJson();
//        student = Json.fromJson(json, Student.class);
//        student.setId(id);
//        studentRepository.save(student);
//        return ok(Json.toJson(student));
//    }
//
//    @DeleteMapping
//    public Result delete(Long id){
//        studentRepository.deleteById(id);
//        return noContent();
//    }
}

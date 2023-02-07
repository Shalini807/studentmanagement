package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.api.db.Database;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
//import service.StudentService;

import javax.inject.Inject;
import java.sql.*;

public class StudentController extends Controller {
//    private StudentService studentService;
    private FormFactory formFactory;
//    @Inject
//    public StudentController(StudentService studentService, FormFactory formFactory) {
//        this.studentService = studentService;
//        this.formFactory = formFactory;
//    }

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

    public Result addStudent(Http.Request request) {
        JsonNode json = request.body().asJson();
        int id = json.get("id").asInt();
        String name = json.get("name").asText();
        String email = json.get("email").asText();
        String address = json.get("address").asText();

        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "INSERT INTO student (id, name, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();
            return ok("Data inserted successfully");
        } catch (SQLException e) {
            return internalServerError("Error inserting data: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
        }
    }
    public Result updateStudent(Http.Request request) {
        JsonNode json = request.body().asJson();
        int id = json.get("id").asInt();
        String name = json.get("name").asText();
        String email = json.get("email").asText();
        String address = json.get("address").asText();
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "UPDATE student SET name=?, email=?, address=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, address);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            return ok("Data updated successfully");
        } catch (SQLException e) {
            return internalServerError("Error updating data: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    public Result deleteStudent(int id) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String checkSql = "SELECT * FROM student WHERE id=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return notFound("Data not found with id " + id);
            }
            String sql = "DELETE FROM student WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return ok("Data deleted successfully");
        } catch (SQLException e) {
            return internalServerError("Error deleting data: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
        }
    }




}

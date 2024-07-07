package com.project.config;

import com.project.dao.StudentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseConfig {

    private final Connection connection;

    public DatabaseConfig() {
        try {
            Properties properties = new Properties();
            properties.load(DatabaseConfig.class.getResourceAsStream("/application.properties"));

            Class.forName(properties.getProperty("db.class"));
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StudentDao createStudent(StudentDao studentDao) {

        final String query = """
                INSERT INTO students (id, first_name, last_name, middle_name, birth_date, student_group)
                 VALUES (?, ?, ?, ?, ?, ?)""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            studentDao.setId(UUID.randomUUID());

            preparedStatement.setString(1, String.valueOf(studentDao.getId()));
            preparedStatement.setString(2, studentDao.getFirstName());
            preparedStatement.setString(3, studentDao.getLastName());
            preparedStatement.setString(4, studentDao.getMiddleName());
            preparedStatement.setDate(5, Date.valueOf(studentDao.getBirthDate()));
            preparedStatement.setString(6, studentDao.getGroup());

            preparedStatement.executeUpdate();
            return studentDao;
        } catch (SQLException e) {
            return null;
        }
    }

    public void deleteStudentById(UUID studentId) {
        String query = "DELETE FROM students WHERE uid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentId.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StudentDao> getStudents() {

        List<StudentDao> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                StudentDao student = new StudentDao();
                student.setId(UUID.fromString(resultSet.getString("uid")));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                student.setGroup(resultSet.getString("student_group"));
                students.add(student);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;

    }

}

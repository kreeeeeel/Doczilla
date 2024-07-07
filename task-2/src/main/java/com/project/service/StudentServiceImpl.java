package com.project.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.config.DatabaseConfig;
import com.project.dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class StudentServiceImpl implements StudentService {

    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StudentServiceImpl() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void createStudent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setContentType("application/json");

            StudentDao studentResponse = databaseConfig.createStudent(
                    objectMapper.readValue(httpServletRequest.getInputStream(), StudentDao.class)
            );

            if (studentResponse != null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(httpServletResponse.getWriter(), studentResponse);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JsonMappingException | JsonParseException | JsonGenerationException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteStudent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String id = httpServletRequest.getParameter("id");
        if (id != null) {
            UUID uuid = UUID.fromString(id);
            databaseConfig.deleteStudentById(uuid);
        }
        httpServletResponse.setStatus(id != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void get(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {

            httpServletResponse.setContentType("application/json");
            List<StudentDao> allStudent = databaseConfig.getStudents();

            objectMapper.writeValue(httpServletResponse.getWriter(), allStudent);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        } catch (IOException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

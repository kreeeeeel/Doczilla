package com.project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface StudentService {
    void createStudent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void deleteStudent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void get(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}

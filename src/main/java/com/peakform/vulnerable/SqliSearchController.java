package com.peakform.vulnerable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vuln/search")
public class SqliSearchController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public String search(@RequestParam(required = false, defaultValue = "") String term, Model model) {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT id, full_name, age, suburb, email, medical_note, available_slot "
            + "FROM member WHERE full_name LIKE '%" + term + "%'";

        model.addAttribute("executedSql", sql);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getObject("id"));
                row.put("fullName", rs.getObject("full_name"));
                row.put("age", rs.getObject("age"));
                row.put("suburb", rs.getObject("suburb"));
                row.put("email", rs.getObject("email"));
                row.put("medicalNote", rs.getObject("medical_note"));
                row.put("availableSlot", rs.getObject("available_slot"));
                results.add(row);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("results", results);
        model.addAttribute("term", term);
        return "vuln/search";
    }
}

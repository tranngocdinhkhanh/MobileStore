package khanhtnd.mobilestore;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class MobileStoreApplication {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    public static void main(String[] args) {
        createDatabaseIfNotExists();
        SpringApplication.run(MobileStoreApplication.class, args);
    }
    private static void createDatabaseIfNotExists() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/";
        String dbName = "mobile_store";
        String username = "root";
        String password = "123456";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            // Tạo database nếu chưa tồn tại
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + dbName +
                    " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Database '" + dbName + "' đã được kiểm tra và tạo nếu chưa tồn tại.");

        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra/tạo database: " + e.getMessage());
        }
    }
}

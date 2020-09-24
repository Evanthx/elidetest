package com.usermind.elidetest;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.usermind.elidetest.dropwizard.DropWizardService;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AppMain {

    public static void main(String[] args) throws Exception {

        //Swagger throws a lot of reflection errors looking for APIs ... this just turns those off
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.reflections");
        rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);


        String url = "jdbc:h2:mem:";

        try (Connection con = DriverManager.getConnection(url);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery("SELECT 1+1")) {

            if (rs.next()) {

                System.out.println(rs.getInt(1));
            }

        } catch (SQLException ex) {
            System.out.println("FAILED");

        }


        new DropWizardService().run(args);

    }

}

package com.adachina.cfgcenter.demo;

import com.adachina.cfgcenter.config.BaseGenericConfig;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author
 */
public class XmlApplication {

  public static void main(String[] args) throws IOException {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

    BaseGenericConfig baseGenericConfig = context.getBean(BaseGenericConfig.class);

    System.out.println("XmlApplication Demo. Input any key except quit to print the values. Input quit to exit.");
    while (true) {
      System.out.print("> ");
      String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if (!Strings.isNullOrEmpty(input) && input.trim().equalsIgnoreCase("quit")) {
        System.exit(0);
      }

      String  test = baseGenericConfig.getProperty(input);
      System.out.println(test);

    }
  }
}

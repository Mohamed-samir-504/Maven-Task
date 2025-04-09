package com.mvntest.sumerge.user.boundary;

import com.mvntest.sumerge.AbstractIT;
import com.mvntest.sumerge.repositories.user.boundary.UserRepository;
import com.mvntest.sumerge.repositories.user.entity.UserDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GreetingResourceIT extends AbstractIT {

    public static String BASE_URI = "/users";
    public static String EXPECTED_GREETING = "Hi";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void greeting_test1() throws Exception {
        userRepository.deleteAll();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/add/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andReturn();
        MockHttpServletResponse mvcResponse = mvcResult.getResponse();
        String strResponse = mvcResponse.getContentAsString();

        await().atMost(2, SECONDS).until(() ->
                userRepository.findAll().size() > 0);

        List<UserDocument> userList =  userRepository.findAll();

        Assertions.assertTrue(userList.size() == 1 && userList.get(0).getName().equals("Ahmed Yousry"));
    }
}

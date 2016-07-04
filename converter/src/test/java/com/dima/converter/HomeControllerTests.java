//package com.dima.converter;
//
//import com.dima.converter.controller.HomeController;
//import com.dima.converter.model.jpa.History;
//import com.dima.converter.model.Registration;
//import com.dima.converter.model.jpa.Role;
//import com.dima.converter.service.converter.TransitiveCurrencyConverter;
//import com.dima.converter.service.currentuser.CurrentUserDetailsService;
//import com.dima.converter.service.user.HistoryServiceImpl;
//import com.dima.converter.service.user.UserService;
//import com.dima.converter.service.user.UserServiceImpl;
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.authentication.CachingUserDetailsService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.rules.SpringClassRule;
//import org.springframework.test.context.junit4.rules.SpringMethodRule;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import java.util.Date;
//
//import static java.util.Collections.singletonList;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
//@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration
//@WebAppConfiguration
//public class HomeControllerTests {
//
//    @ClassRule
//    public static final SpringClassRule SPRING_RULE = new SpringClassRule();
//
//    @Rule
//    public final SpringMethodRule springRule = new SpringMethodRule();
//
//    @Autowired
//    WebApplicationContext wac;
//
//    @Autowired
//    HomeController controller;
//
//    MockMvc mockMvc;
//
////    @Autowired
////    UserServiceImpl userService;
//
//    @Mock
//    TransitiveCurrencyConverter conversionService;
//
//    @Mock
//    HistoryServiceImpl historyService;
//
//    @Before
//    public void setUp() {
//        mockMvc = webAppContextSetup(wac).build();
//        ReflectionTestUtils.setField(controller, "service", conversionService);
//        ReflectionTestUtils.setField(controller, "historyService", historyService);
//        UserService v = wac.getBean(UserService.class);
//        Registration r = new Registration();
//        r.setUsername("username");
//        r.setPassword("aa");
//        r.setRole(Role.USER);
//        r.setEmail("a@b.c");
//        r.setBirthday(new Date());
//        v.create(r);
//    }
//
//    private History createHistoryRecord(String user, Date date){
//        History history = new History();
//        history.setTimestamp(date);
//        history.setBase("aaa");
//        history.setQuote("bbb");
//        history.setAmount(12);
//        history.setResult(21);
//        history.setUsername(user);
//        return history;
//    }
//
//    @Test
//    @WithUserDetails("username")
////    @WithMockUser("username")
//    public void shouldDisplayRepositoryItemsInitially() throws Exception {
//
//        when(historyService.getUserHistory(any())).thenReturn(singletonList(createHistoryRecord("username", new Date())));
//
//        mockMvc.perform(get("/home"))
//                .andExpect(view().name("home"))
//                .andExpect(model().attribute("history", hasSize(1)));
//    }
//
//    @Configuration
//    @EnableWebMvc
//    static class TestConfiguration {
//
//        @Bean
//        public HomeController homeController() {
//            return new HomeController(null, null);
//        }
//    }
//}
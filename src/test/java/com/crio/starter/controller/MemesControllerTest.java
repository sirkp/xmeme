package com.crio.starter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;
import com.crio.starter.service.MemesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class MemesControllerTest {
  
  public static final String MEME_ENDPOINT = "/memes";

  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private MemesService memesService;

  @InjectMocks
  private MemesController memesController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(memesController).build();
  }

  @Test
  public void wronglySpelledPostMemeTest() throws Exception {
  
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();
    
    //misspelled name
    String jsonStringRequest1 = "{\"nam\": \"Dummy1\", \"url\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response1 = mvc.perform(
          post(uri.toString()).content(jsonStringRequest1).accept(APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

    //misspelled url
    String jsonStringRequest2 = "{\"name\": \"Dummy1\", \"ul\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response2 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest2).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());
    
    //misspelled caption
    String jsonStringRequest3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\"" 
        + ", \"capton\": \"Dummy1/\"}";
    MockHttpServletResponse response3 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest3).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());
  }

  @Test
  public void missingParamsPostMemeTest() throws Exception {
  
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();
    
    //missing name
    String jsonStringRequest1 = "{\"url\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response1 = mvc.perform(
          post(uri.toString()).content(jsonStringRequest1).accept(APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

    //missing url
    String jsonStringRequest2 = "{\"name\": \"Dummy1\"" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response2 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest2).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());
    
    //missing caption
    String jsonStringRequest3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\"}";
    MockHttpServletResponse response3 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest3).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());
  }

  @Test
  public void nullParamsPostMemeTest() throws Exception {
  
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();
    
    //null name
    String jsonStringRequest1 = "{\"name\": null, \"url\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response1 = mvc.perform(
          post(uri.toString()).content(jsonStringRequest1).accept(APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response1.getStatus());

    //null url
    String jsonStringRequest2 = "{\"name\": \"Dummy1\", \"url\": null" 
        + ", \"caption\": \"Dummy1/\"}";
    MockHttpServletResponse response2 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest2).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatus());
    
    //null caption
    String jsonStringRequest3 = "{\"name\": \"Dummy1\", \"url\": \"https://www.google.co/\"" 
        + ", \"caption\": null}";
    MockHttpServletResponse response3 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest3).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response3.getStatus());

    //All null
    String jsonStringRequest4 = "{\"name\": null, \"url\": null" 
        + ", \"caption\": null}";
    MockHttpServletResponse response4 = mvc.perform(
        post(uri.toString()).content(jsonStringRequest4).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response4.getStatus());
    
  }

  @Test
  public void isValidUrlTest() {
    String url1 = "htt://www.google.com";
    assertFalse(MemesController.isValidUrl(url1));

    String url2 = "http://www.google.com";
    assertTrue(MemesController.isValidUrl(url2));
    
    String url3 = "https://www.google.com";
    assertTrue(MemesController.isValidUrl(url3));
  }

  @Test
  public void postMemeSuccessTest() throws Exception {

    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

    String reqBody = "{\"name\": \"Pradeep\", \"url\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Pawri Ho rhi/\"}";
        
    Meme meme = new Meme("1", "pradeep", "https://www.google.com", "Party");
    when(memesService.postMemeService(any(MemePostRequest.class)))
        .thenReturn(meme);
        
    MockHttpServletResponse response = mvc.perform(
        post(uri.toString()).content(reqBody).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("{\"id\":\"" + meme.getId() + "\"}", response.getContentAsString());

    String reqBodyAnother = "{\"name\": \"\",\"url\": \"https://www.google.com\",\"caption\": \"\"}";
    
    response = mvc.perform(
        post(uri.toString()).content(reqBodyAnother).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("{\"id\":\"" + meme.getId() + "\"}", response.getContentAsString());
  }

  @Test
  public void postMemeDuplicateTest() throws Exception {

    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

    String reqBody = "{\"name\": \"Pradeep\", \"url\": \"https://www.google.co/\"" 
        + ", \"caption\": \"Pawri Ho rhi/\"}";
        
    when(memesService.postMemeService(any()))
        .thenReturn(null);
        
    MockHttpServletResponse response = mvc.perform(
        post(uri.toString()).content(reqBody).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    
    assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
  }

  @Test
  public void postMemeInvalidUrlTest() throws Exception {

    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT).build().toUri();

    String reqBody = "{\"name\": \"Pradeep\", \"url\": \"htts://www.google.co/\"" 
        + ", \"caption\": \"Pawri Ho rhi/\"}";
        
    MockHttpServletResponse response = mvc.perform(
        post(uri.toString()).content(reqBody).accept(APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void getMemeSuccessTest() throws Exception {
    Meme meme = new Meme("1", "pradeep", "https://www.google.com", "Party");
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT + "/" + meme.getId())
        .build().toUri();
    
    when(memesService.getMemeWithIdService(eq(meme.getId())))
      .thenReturn(meme);

    MockHttpServletResponse response = mvc.perform(
        get(uri.toString()).accept(APPLICATION_JSON_VALUE)
        ).andReturn().getResponse();
  
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains(meme.getId()));
  }

  @Test
  public void getMemeNotFoundTest() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT + "/" + "23")
        .build().toUri();
    
    when(memesService.getMemeWithIdService(anyString()))
      .thenReturn(null);

    MockHttpServletResponse response = mvc.perform(
        get(uri.toString()).accept(APPLICATION_JSON_VALUE)
        ).andReturn().getResponse();
  
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void getLatestMemesTest() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(MEME_ENDPOINT)
        .build().toUri();
    
    Meme meme = new Meme("1", "pradeep", "https://www.google.com", "Party");
    
    when(memesService.getLatestMemesService()).thenReturn(Arrays.asList(meme));

    MockHttpServletResponse response = mvc.perform(
        get(uri.toString()).accept(APPLICATION_JSON_VALUE)
        ).andReturn().getResponse();
        
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains(meme.getId()));
  
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
package com.crio.starter.controller;

import com.crio.starter.dto.Meme;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exceptions.InvalidUrlException;
import com.crio.starter.exceptions.MemeNotFoundException;
import com.crio.starter.exchange.MemePostRequest;
import com.crio.starter.exchange.MemePostResponse;
import com.crio.starter.exchange.MemeResponse;
import com.crio.starter.service.MemesService;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemesController {
  public static final String MEME_ENDPOINT = "/memes";
  public static final String SINGLE_MEME_ENDPOINT = MEME_ENDPOINT + "/{id}";

  @Autowired
  private MemesService memesService;

  @GetMapping(MEME_ENDPOINT)
  public ResponseEntity<List<Meme>> getLatestMemes() {
    System.out.println("MemesController::getLatestMemes");
    List<Meme> memes = memesService.getLatestMemesService();
    return ResponseEntity.ok().body(memes);
  }

  public static boolean isValidUrl(String url) {
    UrlValidator urlValidator = new UrlValidator();
    return urlValidator.isValid(url);
  }

  @PostMapping(MEME_ENDPOINT)
	public ResponseEntity<MemePostResponse> postMeme(@Valid @RequestBody
			MemePostRequest memePostRequest) throws DuplicateMemeException,
			InvalidUrlException {

    System.out.println("MemesController::postMeme");  
    if (!isValidUrl(memePostRequest.getUrl())) {
      throw new InvalidUrlException();
    }  
    Meme meme = memesService.postMemeService(memePostRequest);
    if (meme == null) {
      throw new DuplicateMemeException();
    }
    return ResponseEntity.ok().body(new MemePostResponse(meme.getId()));
  }

  @GetMapping(SINGLE_MEME_ENDPOINT)
  public ResponseEntity<Meme> getMeme(@NotNull @PathVariable String id) throws
      MemeNotFoundException {

    System.out.println("MemesController::postMeme");

    Meme meme = memesService.getMemeWithIdService(id);
    if (meme == null) {
      throw new MemeNotFoundException();
    }
    return ResponseEntity.ok().body(meme);
  }

}
package com.crio.starter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
    reason = "A meme with same fields already present")
public class DuplicateMemeException extends Exception {
}
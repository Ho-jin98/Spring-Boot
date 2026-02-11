package com.example.userschedule.config;
import com.example.userschedule.common.CommonResponse;
import com.example.userschedule.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
    //TODO Controller에서 @Valid 검증이 실패하면 MethodArgumentNotValidException 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        //TODO
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(HttpStatus.BAD_REQUEST, errorMessage));
    }

    //TODO getBindingResult() > 에러를 가져온다.
    // getFieldError() >> 에러가 발생한 해당 필드를 가져온다.
    // getBindingResult() + getFieldError() = 검증 오류가 발생하는 모든 필드의 에러 정보(FieldError)들을 리스트(List)
    // 형태로 가져온다. 예를 들어서 writer와 title이 동시에 틀렸다면 이 리스트 안에는 2개의 에러 객체가 들어있다.
    // .map(FieldError::getDefaultMessage) >> 데이터 변환을 하는 중간연산 단계인데
    // getDefaultMessage >> RequestDTO 필드에 적어둔 메세지를 의미하고 이것을 가져와준다.
    // FieldError -> 객체를 받아서 그 안에 있는 문자열 메세지로 갈아끼우는 작업
    // Collectors.joining(", ") -> 최종 연산(합체)단계, 여러개의 문자열 메세지를 다시 하나로 모아주는데,
    // 이 때 "," 쉽표를 구분자로 사용해서 이어붙인다. 공백을 추가해줘서 읽기 편하게 해주면 좋다.

    //TODO CommonResponse DTO를 이용하여 GlobalExceptionHandler를 만드는 것은, Controller 에서의 발생하는 에러를
    // 처리해준다고 생각하고, 나머지 exception 패키지에서 커스텀 에러를 만들어서 사용하는 부분은 Service에서 발생하는
    // 에러들을 처리해준다고 생각하면 그나마 이해하기 편한것 같다.



}

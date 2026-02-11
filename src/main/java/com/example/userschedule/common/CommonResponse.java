package com.example.userschedule.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
//TODO CommonResponse는 DTO를 만들어준 것, 에러를 처리하는 역할이 아니다!!
@Getter
public class CommonResponse<T>{
    private final HttpStatus status;
    private final String message;
    private final T data;
    //TODO 여러 데이터들이 들어올 수 있으니 T 제네릭을 사용

    public CommonResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
//TODO 이런식으로 생성자를 만들어놓으면 fail메서드에서 리턴해줄 때 null을 빼줄 수 있음
//    public CommonResponse(HttpStatus status, String message) {
//        this.status = status;
//        this.message = message;
//        this.data = null;
//    }

    //TODO static 메서드에서 제네릭을 사용할 때는 static <T> 으로 명시를 해줘야됨.
    // 제네릭을 사용한다고 알려준다고 생각하면 될 것 같다.

    //TODO 성공했을 때 new CommonResponse<>(status, message, data) 이 객체를 리턴해줌,
    public static <T> CommonResponse<T> success(HttpStatus status, String message, T data){
        return new CommonResponse<>(status, message, data);
    }
    //TODO 실패했을 때 new CommonResponse<>(status, message, null) 이 객체를 리턴해줌,
    // 실패했을 때는 돌려줄 데이터, 즉 body가 필요없으므로, data는 null
    public static <T> CommonResponse<T> fail(HttpStatus status, String message){
        return new CommonResponse<>(status, message, null);
    }

}

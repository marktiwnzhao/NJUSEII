package common;

import lombok.Builder;
import lombok.Data;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:15
 **/
@Data
@Builder
public class Result {
    private Integer code; //200 or 400
    private String msg; //success or fail
    private Integer total;  //记录的数目
    private Object data;

    public static Result success(Data data, Integer total) {
        return new Result(200, "SUCCESS", total, data);
    }
    public static Result fail() {
        return new Result(400, "FAIL", 0, null);
    }
}

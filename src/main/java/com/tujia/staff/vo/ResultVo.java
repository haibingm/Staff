package com.tujia.staff.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by haibingm on 2018/1/19.
 */
@Data
public class ResultVo implements Serializable{

    private String errCode;

    private String message;

    private Object data;
}
